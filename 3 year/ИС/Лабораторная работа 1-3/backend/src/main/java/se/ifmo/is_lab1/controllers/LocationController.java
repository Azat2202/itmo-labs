package se.ifmo.is_lab1.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.ifmo.is_lab1.dto.collection.LocationRequest;
import se.ifmo.is_lab1.messages.collection.LocationResponse;
import se.ifmo.is_lab1.services.LocationService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/collection/location")
@Tag(name = "Location Manipulation Controller", description = "API для управления объектами Location в коллекции")
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public List<LocationResponse> getAllLocations() {
        return locationService.getAllLocations();
    }

    @PostMapping
    public LocationResponse createLocation(@RequestBody @Valid LocationRequest locationRequest) {
        return locationService.createLocation(locationRequest);
    }
}

