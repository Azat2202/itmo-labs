package se.ifmo.is_lab1.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.ifmo.is_lab1.dto.collection.CoordinatesRequest;
import se.ifmo.is_lab1.messages.collection.CoordinatesResponse;
import se.ifmo.is_lab1.services.CoordinatesService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/collection/coordinates")
@Tag(name = "Coordinates Manipulation Controller", description = "API для управления объектами Coordinates в коллекции")
public class CoordinatesController {

    private final CoordinatesService coordinatesService;

    @GetMapping
    public List<CoordinatesResponse> getAllCoordinates() {
        return coordinatesService.getAllCoordinates();
    }

    @PostMapping
    public CoordinatesResponse createCoordinates(@RequestBody @Valid CoordinatesRequest coordinatesRequest) {
        return coordinatesService.createCoordinates(coordinatesRequest);
    }
}

