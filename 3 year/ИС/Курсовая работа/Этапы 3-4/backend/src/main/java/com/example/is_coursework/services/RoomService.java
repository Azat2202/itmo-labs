package com.example.is_coursework.services;

import com.example.is_coursework.messages.RoomMessage;
import com.example.is_coursework.models.Character;
import com.example.is_coursework.models.*;
import com.example.is_coursework.models.enums.SexType;
import com.example.is_coursework.repositories.*;
import com.example.is_coursework.utils.RandomHelper;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final OpenFactsRepository openFactsRepository;
    @Value("${game.generation.min_food_days}")
    private int minFoodDays;

    @Value("${game.generation.max_food_days}")
    private int maxFoodDays;

    @Value("${game.generation.min_stay_days}")
    private int minStayDays;

    @Value("${game.generation.max_stay_days}")
    private int maxStayDays;

    @Value("${game.generation.min_square}")
    private int minSquare;

    @Value("${game.generation.max_square}")
    private int maxSquare;

    @Value("${game.settings.max_user_in_room_count}")
    private int maxUserInRoomCount;



    private final CataclysmRepository cataclysmRepository;
    private final BunkerRepository bunkerRepository;
    private final EquipmentRepository equipmentRepository;
    private final RoomRepository roomRepository;
    private final CharacterRepository characterRepository;
    private final ModelMapper modelMapper;

    public RoomMessage createRoom(User user) {
        Random random = new Random(user.hashCode() + System.currentTimeMillis());
        List<Cataclysm> allCataclysms = cataclysmRepository.findAll();
        List<Equipment> allEquipments = equipmentRepository.findAll();

        Cataclysm cataclysm = allCataclysms.get(random.nextInt(allCataclysms.size()));
        Set<Long> equipmentIds = RandomHelper.getRandomList(random, 2, 0, allEquipments.size());
        String joinCode = RandomHelper.getRandomStringUppercase(random, 6);
        Bunker bunker = Bunker.builder()
                .foodDays(random.nextInt(minFoodDays, maxFoodDays))
                .stayDays(random.nextInt(minStayDays, maxStayDays))
                .square(random.nextInt(minSquare, maxSquare))
                .equipments(allEquipments.stream()
                        .filter(equipment ->
                                equipmentIds.contains(
                                        equipment.getId())
                        ).toList())
                .build();
        Character character = characterRepository.save(generateBaseCharacter(user));
        Room room = Room.builder()
                .cataclysm(cataclysm)
                .bunker(bunker)
                .joinCode(joinCode)
                .admin(user)
                .isStarted(false)
                .isClosed(false)
                .characters(List.of(character))
                .build();

        bunkerRepository.save(bunker);
        roomRepository.save(room);
        return modelMapper.map(room, RoomMessage.class);
    }

    public RoomMessage getRoom(User user, Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Room not found"));
        if (!room.getAdmin().equals(user) &&
                room.getCharacters()
                        .stream()
                        .noneMatch(character -> character.getUser().equals(user))) {
            throw new AccessDeniedException("You are not in this room");
        }
        return modelMapper.map(room, RoomMessage.class);
    }

    public RoomMessage joinRoom(User user, String joinCode) {
        Room room = roomRepository.findByJoinCode(joinCode)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Room not found"));
        if (room.getCharacters()
                .stream()
                .anyMatch(character -> character.getUser().equals(user))) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "You are already in this room");
        }
        if (room.getCharacters().size() >= maxUserInRoomCount) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN,"Room is full");
        }
        Character character = generateBaseCharacter(user);
        character = characterRepository.save(character);
        room.getCharacters().add(character);
        roomRepository.save(room);
        openFactsRepository.save(OpenedFacts.builder().character_id(character.getId()).build());
        return modelMapper.map(room, RoomMessage.class);
    }

    public Character generateBaseCharacter(User user) {
        Random random = new Random(user.hashCode() + System.currentTimeMillis());
        Faker faker = new Faker(new Locale("ru") ,random);
        SexType sexType = faker.bool().bool() ? SexType.MALE : SexType.FEMALE;
        return Character.builder()
                .name(faker.name().firstName())
                .sex(sexType)
                .age(faker.number().numberBetween(18, 85))
                .user(user)
                .isActive(true)
                .build();
    }
}
