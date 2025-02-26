package com.example.is_coursework.services;

import com.example.is_coursework.messages.PollMessage;
import com.example.is_coursework.models.Character;
import com.example.is_coursework.models.*;
import com.example.is_coursework.repositories.CharacterRepository;
import com.example.is_coursework.repositories.PollRepository;
import com.example.is_coursework.repositories.RoomRepository;
import com.example.is_coursework.repositories.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.webjars.NotFoundException;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final PollRepository pollRepository;
    private final ThreadPoolTaskScheduler scheduler;
    private final CharacterRepository characterRepository;

    @Value("${game.settings.poll_time_seconds}")
    private int pollTimeSeconds;

    @Value("${game.settings.places_in_room_to_live_persent}")
    private long placesInRoomToLivePercent;


    public PollMessage createPoll(User user, Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new NotFoundException("Room not found"));
        if (!room.getAdmin().equals(user))
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "You are not admin of this room");
        if (!room.getIsStarted())
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Game is not started");
        if (room.getIsClosed())
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Game is closed");
        if (pollRepository.findFirstByIsOpenIsTrueAndRoom(room).isPresent())
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Poll is already opened");
        Poll prevPoll = pollRepository.findFirstByRoomOrderByCreationTime(room).orElse(null);
        int roundNumber = prevPoll == null ? 1 : prevPoll.getRoundNumber() + 1;
        Poll poll = Poll.builder()
                .room(room)
                .roundNumber(roundNumber)
                .isOpen(true)
                .targetCharacter(null)
                .build();
        pollRepository.save(poll);
//        scheduler.schedule(() -> stopPoll(roomId),
//                poll.getCreationTime().toInstant()
//                        .plus(pollTimeSeconds, ChronoUnit.SECONDS));
        this.checkPolls();
        return poll.toPollMessage();
    }

    public List<PollMessage> getPolls(User user, Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new NotFoundException("Room not found"));
        if (!room.getAdmin().equals(user) &&
                room.getCharacters().stream()
                        .map(Character::getUser)
                        .noneMatch(user::equals)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "You are not a participant of the room");
        }
        List<PollMessage> polls = pollRepository.findAllByRoom(room).stream().map(Poll::toPollMessage).toList();
        this.checkPolls();
        return polls;
    }

    public PollMessage vote(User user, Long roomId, Long targetCharacterId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Room not found"));
        if (!room.getAdmin().equals(user)
                && room.getCharacters().stream()
                .map(Character::getUser).noneMatch(user::equals)) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "You are participant of the room");
        }
        Poll poll = pollRepository.findFirstByIsOpenIsTrueAndRoom(room).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "There is no open poll"));
        Character targetCharacter = room.getCharacters().stream()
                .filter(character -> character.getId().equals(targetCharacterId))
                .findFirst()
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Target user not found"));
        if (Objects.equals(targetCharacter.getUser().getId(), user.getId()))
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Cannot vote against yourself!");
        Character userCharacter = room.getCharacters().stream()
                .filter(character -> character.getUser().equals(user))
                .findFirst()
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found in characters of room"));


        Vote vote = poll.getVotes().stream()
                .filter(v -> v.getCharacter().getUser().equals(user))
                .findFirst()
                .orElse(Vote.builder()
                        .character(userCharacter)
                        .poll(poll)
                        .room(room)
                        .build());
        vote.setTargetCharacter(targetCharacter);
        poll.getVotes().add(vote);
        voteRepository.save(vote);
        pollRepository.save(poll);
        if (poll.getVotes().size() == poll.getRoom().getCharacters().size())
            return stopPoll(roomId).toPollMessage();
        return poll.toPollMessage();
    }

    public void checkPolls() {
        List<Poll> polls = pollRepository.findAllByIsOpenIsTrue();
        for (Poll poll : polls) {
            if (poll.getCreationTime().plusSeconds(pollTimeSeconds).isBefore(ZonedDateTime.now())) {
                stopPoll(poll.getRoom().getId());
            }
        }
    }

    public Poll stopPoll(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new NotFoundException("Room not found"));
        Poll poll1 = pollRepository.findFirstByIsOpenIsTrueAndRoom(room).orElseThrow(() -> new NotFoundException("There is no open poll"));
        Poll poll = pollRepository.getReferenceById(poll1.getId());
        poll.setIsOpen(false);
        if (poll.getVotes().isEmpty()) {
            return pollRepository.save(poll);
        }
        List<Long> allVotes = poll.getVotes().stream()
                .map(Vote::getTargetCharacter)
                .map(Character::getId)
                .toList();
        Map<Long, Long> repetitions = repetitions(allVotes);
        Long maxVoteCount = Collections.max(repetitions.values());
        List<Long> targetCharacterId = repetitions.entrySet().stream()
                .filter(e -> e.getValue().equals(maxVoteCount))
                .map(Map.Entry::getKey)
                .toList();
        if (targetCharacterId.size() > 1) {
            poll.setTargetCharacter(null);
        } else {
            Character targetCharacter = poll.getVotes().stream()
                    .filter(v -> v.getTargetCharacter().getId().equals(targetCharacterId.get(0)))
                    .findFirst()
                    .orElseThrow()
                    .getTargetCharacter();
            poll.setTargetCharacter(targetCharacter);
            targetCharacter.setIsActive(false);
            characterRepository.save(targetCharacter);
            this.checkIfFinished(room);
        }
        poll.setIsOpen(false);
        return pollRepository.save(poll);
    }

    private void checkIfFinished(Room room) {
        List<Character> characters = room.getCharacters();
        int count = characters.size();
        long activeCount = characters.stream().filter(Character::getIsActive).count();
        if (activeCount <= Math.ceil(((float) count * placesInRoomToLivePercent) / 100.0)) {
            this.stopGame(room);
        }
    }


    public void stopGame(Room room) {
        room.setIsStarted(true);
        room.setIsClosed(true);
        roomRepository.save(room);
    }

    private Map<Long, Long> repetitions(List<Long> list) {
        return list.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}
