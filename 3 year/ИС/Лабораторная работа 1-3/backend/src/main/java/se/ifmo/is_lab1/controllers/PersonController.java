package se.ifmo.is_lab1.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.ifmo.is_lab1.dto.collection.PersonRequest;
import se.ifmo.is_lab1.messages.collection.PersonResponse;
import se.ifmo.is_lab1.services.PersonService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/collection/person")
@Tag(name = "Person Manipulation Controller", description = "API для управления объектами Person в коллекции")
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public List<PersonResponse> getAllPersons() {
        return personService.getAllPersons();
    }

    @PostMapping
    public PersonResponse createPerson(@RequestBody @Valid PersonRequest personRequest) {
        return personService.createPerson(personRequest);
    }
}
