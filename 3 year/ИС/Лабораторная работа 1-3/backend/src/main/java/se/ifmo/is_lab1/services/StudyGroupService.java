package se.ifmo.is_lab1.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import se.ifmo.is_lab1.dto.batch.BatchRequest;
import se.ifmo.is_lab1.dto.batch.StudyGroupBatchRequest;
import se.ifmo.is_lab1.dto.collection.StudyGroupRequest;
import se.ifmo.is_lab1.dto.collection.UpdateStudyGroupRequest;
import se.ifmo.is_lab1.exceptions.CoordinatesNotFoundException;
import se.ifmo.is_lab1.exceptions.ObjectDontBelongToUserException;
import se.ifmo.is_lab1.exceptions.PersonNotFoundException;
import se.ifmo.is_lab1.exceptions.StudyGroupNotFoundException;
import se.ifmo.is_lab1.messages.collection.StudyGroupResponse;
import se.ifmo.is_lab1.models.*;
import se.ifmo.is_lab1.models.enums.FormOfEducation;
import se.ifmo.is_lab1.models.enums.Role;
import se.ifmo.is_lab1.models.enums.Semester;
import se.ifmo.is_lab1.repositories.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.random;
import static java.lang.System.currentTimeMillis;

@Service
@RequiredArgsConstructor
public class StudyGroupService {

    private final StudyGroupRepository studyGroupRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final LocationRepository locationRepository;
    private final ObjectAuditRepository objectAuditRepository;

    private static Map<Semester, String> semesterMapping = new HashMap<>();
    private static Map<FormOfEducation, String> formOfEducationMapping = new HashMap();

    static {
        semesterMapping.put(Semester.FIRST, "0");
        semesterMapping.put(Semester.SECOND, "1");
        semesterMapping.put(Semester.SEVENTH, "2");
        semesterMapping.put(Semester.EIGHTH, "3");

        formOfEducationMapping.put(FormOfEducation.DISTANCE_EDUCATION, "0");
        formOfEducationMapping.put(FormOfEducation.FULL_TIME_EDUCATION, "1");
        formOfEducationMapping.put(FormOfEducation.EVENING_CLASSES, "2");
    }

    public StudyGroupResponse getStudyGroup(Integer id) {
        StudyGroup studyGroup = studyGroupRepository.findById(id)
                .orElseThrow(StudyGroupNotFoundException::new);
        return modelMapper.map(studyGroup, StudyGroupResponse.class);
    }

    @Transactional(readOnly = true)
    public Page<StudyGroupResponse> getAllStudyGroups(Pageable pageable,
                                                      String groupName,
                                                      String adminName,
                                                      Semester semester,
                                                      FormOfEducation formOfEducation) {
        Page<StudyGroup> studyGroups =
                studyGroupRepository.findByFilter(
                        groupName, adminName, semester, formOfEducation, pageable
                );
        return studyGroups.map(s -> modelMapper.map(s, StudyGroupResponse.class));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public StudyGroupResponse createStudyGroup(StudyGroupRequest studyGroupRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        StudyGroup savedStudyGroup = saveStudyGroup(studyGroupRequest, user);
        ObjectAudit objectAudit = new ObjectAudit();
        objectAudit.setTableName("studyGroup");
        objectAudit.setUser(user);
        objectAuditRepository.save(objectAudit);
        return modelMapper.map(savedStudyGroup, StudyGroupResponse.class);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    protected StudyGroup saveStudyGroup(StudyGroupRequest studyGroupRequest, User user) {
        if (!studyGroupRequest.validate()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid study group request");
        }
        if (studyGroupRepository.existsByName(studyGroupRequest.getName())) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Study group with this name already exists");
        }
        StudyGroup requestObject = modelMapper.map(studyGroupRequest, StudyGroup.class);
        if (studyGroupRequest.getGroupAdminId() != null) {
            requestObject.setGroupAdmin(
                    personRepository.findById(studyGroupRequest.getGroupAdminId())
                            .orElseThrow(PersonNotFoundException::new)
            );
        }
        requestObject.setCoordinates(
                coordinatesRepository.findById(studyGroupRequest.getCoordinatesId())
                        .orElseThrow(CoordinatesNotFoundException::new)
        );
        requestObject.setUser(user);
        StudyGroup saved = studyGroupRepository.save(requestObject);
        if(studyGroupRepository.countByName(studyGroupRequest.getName()) != 1)
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Conflict in study group creation try again");
        return saved;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public StudyGroupResponse updateStudyGroup(UpdateStudyGroupRequest studyGroupRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        StudyGroup existingStudyGroup = studyGroupRepository.findById(studyGroupRequest.getId())
                .orElseThrow(StudyGroupNotFoundException::new);

        if (!existingStudyGroup.getIsEditable() ||
                (!user.getRole().equals(Role.ADMIN) &&
                        !existingStudyGroup
                                .getUser()
                                .getId()
                                .equals(user.getId()))) {
            throw new ObjectDontBelongToUserException();
        }
        existingStudyGroup.setCoordinates(
                coordinatesRepository.findById(studyGroupRequest.getCoordinatesId())
                        .orElseThrow(CoordinatesNotFoundException::new)
        );
        if (studyGroupRequest.getGroupAdminId() != null) {
            existingStudyGroup.setGroupAdmin(
                    personRepository.findById(studyGroupRequest.getGroupAdminId())
                            .orElseThrow(PersonNotFoundException::new)
            );
        }
        existingStudyGroup.setName(studyGroupRequest.getName());
        existingStudyGroup.setStudentsCount(studyGroupRequest.getStudentsCount());
        existingStudyGroup.setExpelledStudents(studyGroupRequest.getExpelledStudents());
        existingStudyGroup.setTransferredStudents(studyGroupRequest.getTransferredStudents());
        existingStudyGroup.setFormOfEducation(studyGroupRequest.getFormOfEducation());
        existingStudyGroup.setShouldBeExpelled(studyGroupRequest.getShouldBeExpelled());
        existingStudyGroup.setSemester(studyGroupRequest.getSemester());
        StudyGroup response = studyGroupRepository.save(existingStudyGroup);
        ObjectAudit objectAudit = new ObjectAudit();
        objectAudit.setTableName("studyGroup");
        objectAudit.setUser(user);
        objectAuditRepository.save(objectAudit);
        if (!studyGroupRepository.getReferenceById(response.getId()).equals(response))
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Conflict in study group update try again");
        if(studyGroupRepository.countByName(studyGroupRequest.getName()) != 1)
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Conflict in study group update try again");
        return modelMapper.map(response, StudyGroupResponse.class);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public StudyGroupResponse deleteStudyGroup(Integer objectId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getRole().equals(Role.ADMIN) &&
                !studyGroupRepository.findById(objectId)
                        .orElseThrow(StudyGroupNotFoundException::new)
                        .getUser()
                        .getId()
                        .equals(user.getId())) {
            throw new ObjectDontBelongToUserException();
        }
        StudyGroup studyGroup = studyGroupRepository.findById(objectId)
                .orElseThrow(StudyGroupNotFoundException::new);
        studyGroupRepository.deleteById(objectId);
        if (studyGroup.getGroupAdmin() != null && studyGroup.getGroupAdmin().getStudyGroups().size() == 1) {
            personRepository.deleteById(studyGroup.getGroupAdmin().getId());
            if (studyGroup.getGroupAdmin().getLocation().getPersons().size() == 1) {
                locationRepository.deleteById(studyGroup.getGroupAdmin().getLocation().getId());
            }
        }
        if (studyGroup.getCoordinates().getStudyGroups().size() == 1) {
            coordinatesRepository.deleteById(studyGroup.getCoordinates().getId());
        }
        ObjectAudit objectAudit = new ObjectAudit();
        objectAudit.setTableName("studyGroup");
        objectAudit.setUser(user);
        objectAuditRepository.save(objectAudit);
        return modelMapper.map(studyGroup, StudyGroupResponse.class);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.SUPPORTS)
    public Integer uploadBatch(MultipartFile file, User user) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(BatchRequest.class)
                .with(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        BatchRequest batchRequest = reader.readValue(file.getInputStream());
        List<StudyGroup> batch = batchRequest.getGroups().stream()
                .map(this::parseStudyGroupNestedObjects)
                .map(s -> saveStudyGroup(s, user))
                .toList();
        return batch.size();
    }

    private StudyGroupRequest parseStudyGroupNestedObjects(StudyGroupBatchRequest batchRequest) {
        if (!batchRequest.validate())
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid study group request");
        Coordinates coordinates;
        Location location = Location.builder().build();
        Person groupAdmin;
        if (batchRequest.getCoordinates().getId() != null) {
            coordinates = coordinatesRepository.findById(batchRequest.getCoordinates().getId())
                    .orElseThrow(CoordinatesNotFoundException::new);
        } else {
            coordinates = new Coordinates();
            coordinates.setX(batchRequest.getCoordinates().getX());
            coordinates.setY(batchRequest.getCoordinates().getY());
            coordinates = coordinatesRepository.save(coordinates);
        }
        if (batchRequest.getGroupAdmin().getId() == null) {
            if (batchRequest.getGroupAdmin().getLocation().getId() != null) {
                location = locationRepository.findById(batchRequest.getGroupAdmin().getLocation().getId())
                        .orElseThrow(CoordinatesNotFoundException::new);
            } else {
                location = Location.builder()
                        .x(batchRequest.getGroupAdmin().getLocation().getX())
                        .y(batchRequest.getGroupAdmin().getLocation().getY())
                        .z(batchRequest.getGroupAdmin().getLocation().getZ())
                        .name(batchRequest.getGroupAdmin().getLocation().getName())
                        .build();
                location = locationRepository.save(location);
            }
        }
        if (batchRequest.getGroupAdmin().getId() != null) {
            groupAdmin = personRepository.findById(batchRequest.getGroupAdmin().getId())
                    .orElseThrow(PersonNotFoundException::new);
        } else {
            groupAdmin = Person.builder()
                    .name(batchRequest.getGroupAdmin().getName())
                    .location(location)
                    .eyeColor(batchRequest.getGroupAdmin().getEyeColor())
                    .hairColor(batchRequest.getGroupAdmin().getHairColor())
                    .nationality(batchRequest.getGroupAdmin().getNationality())
                    .weight(batchRequest.getGroupAdmin().getWeight())
                    .build();
            groupAdmin = personRepository.save(groupAdmin);
        }
        StudyGroupRequest studyGroup = StudyGroupRequest.builder()
                .name(batchRequest.getName())
                .coordinatesId(coordinates.getId())
                .studentsCount(batchRequest.getStudentsCount())
                .expelledStudents(batchRequest.getExpelledStudents())
                .transferredStudents(batchRequest.getTransferredStudents())
                .formOfEducation(batchRequest.getFormOfEducation())
                .shouldBeExpelled(batchRequest.getShouldBeExpelled())
                .semester(batchRequest.getSemester())
                .groupAdminId(groupAdmin.getId())
                .isEditable(batchRequest.getIsEditable())
                .build();
        return studyGroup;
    }
}
