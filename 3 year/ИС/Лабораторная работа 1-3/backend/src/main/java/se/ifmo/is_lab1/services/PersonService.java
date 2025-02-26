package se.ifmo.is_lab1.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.ifmo.is_lab1.dto.collection.PersonRequest;
import se.ifmo.is_lab1.exceptions.LocationNotFoundException;
import se.ifmo.is_lab1.messages.collection.PersonResponse;
import se.ifmo.is_lab1.models.ObjectAudit;
import se.ifmo.is_lab1.models.Person;
import se.ifmo.is_lab1.models.User;
import se.ifmo.is_lab1.repositories.LocationRepository;
import se.ifmo.is_lab1.repositories.ObjectAuditRepository;
import se.ifmo.is_lab1.repositories.PersonRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final LocationRepository locationRepository;
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final ObjectAuditRepository objectAuditRepository;

    @Transactional(readOnly = true)
    public List<PersonResponse> getAllPersons() {
        List<Person> persons = personRepository.findAll();
        return modelMapper.map(persons, new TypeToken<List<PersonResponse>>(){}.getType());
    }

    @Transactional(rollbackFor = Exception.class)
    public PersonResponse createPerson(PersonRequest personRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Person person = modelMapper.map(personRequest, Person.class);
        person.setLocation(
                locationRepository.findById(personRequest.getLocationId())
                        .orElseThrow(LocationNotFoundException::new)
        );
        Person savedPerson = personRepository.save(person);
        ObjectAudit objectAudit = new ObjectAudit();
        objectAudit.setTableName("person");
        objectAudit.setUser(user);
        objectAuditRepository.save(objectAudit);
        return modelMapper.map(savedPerson, PersonResponse.class);
    }

}

