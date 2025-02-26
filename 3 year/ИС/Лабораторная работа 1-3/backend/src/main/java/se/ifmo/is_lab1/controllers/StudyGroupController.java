package se.ifmo.is_lab1.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.ifmo.is_lab1.dto.collection.StudyGroupRequest;
import se.ifmo.is_lab1.dto.collection.UpdateStudyGroupRequest;
import se.ifmo.is_lab1.messages.collection.FeedResponse;
import se.ifmo.is_lab1.messages.collection.StudyGroupResponse;
import se.ifmo.is_lab1.models.enums.FormOfEducation;
import se.ifmo.is_lab1.models.enums.Semester;
import se.ifmo.is_lab1.services.ObjectStorageService;
import se.ifmo.is_lab1.services.StudyGroupService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/collection/studyGroup")
@Tag(name = "StudyGroup Manipulation Controller", description = "API для управления объектами StudyGroup в коллекции")
public class StudyGroupController {

    private final StudyGroupService studyGroupService;
    private final ObjectStorageService objectStorageService;

    @GetMapping("/{id}")
    public StudyGroupResponse getStudyGroups(@PathVariable Integer id) {
        return studyGroupService.getStudyGroup(id);
    }

    @GetMapping
    public Page<StudyGroupResponse> getAllStudyGroups(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) String groupName,
            @RequestParam(required = false) String adminName,
            @RequestParam(required = false) Semester semester,
            @RequestParam(required = false) FormOfEducation formOfEducation
    ) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return studyGroupService.getAllStudyGroups(pageable, groupName, adminName, semester, formOfEducation);
    }

    @PostMapping
    public StudyGroupResponse createStudyGroup(@RequestBody @Valid StudyGroupRequest studyGroupRequest) {
        return studyGroupService.createStudyGroup(studyGroupRequest);
    }

    @PostMapping(
            path = "/feed",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFeed(@RequestPart(value = "file") MultipartFile file) {
        objectStorageService.upload(file);
    }

    @GetMapping("/feed")
    public Page<FeedResponse> getFeedHistory(@RequestParam(defaultValue = "0") Integer page,
                                             @RequestParam(defaultValue = "10") Integer size,
                                             @RequestParam(defaultValue = "id") String sortBy,
                                             @RequestParam(defaultValue = "asc") String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return objectStorageService.getImportHistory(pageable);
    }

    @DeleteMapping("/{id}")
    public StudyGroupResponse deleteStudyGroup(@PathVariable Integer id) {
        return studyGroupService.deleteStudyGroup(id);
    }

    @PutMapping
    public StudyGroupResponse updateStudyGroup(@RequestBody @Valid UpdateStudyGroupRequest studyGroupRequest) {
        return studyGroupService.updateStudyGroup(studyGroupRequest);
    }
}
