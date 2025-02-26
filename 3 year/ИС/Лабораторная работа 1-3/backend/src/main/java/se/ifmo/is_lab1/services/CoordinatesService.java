package se.ifmo.is_lab1.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.ifmo.is_lab1.dto.collection.CoordinatesRequest;
import se.ifmo.is_lab1.messages.collection.CoordinatesResponse;
import se.ifmo.is_lab1.models.Coordinates;
import se.ifmo.is_lab1.models.ObjectAudit;
import se.ifmo.is_lab1.models.User;
import se.ifmo.is_lab1.repositories.CoordinatesRepository;
import se.ifmo.is_lab1.repositories.ObjectAuditRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoordinatesService {

    private final CoordinatesRepository coordinatesRepository;
    private final ModelMapper modelMapper;
    private final ObjectAuditRepository objectAuditRepository;

    @Transactional(readOnly = true)
    public List<CoordinatesResponse> getAllCoordinates() {
        List<Coordinates> coordinates = coordinatesRepository.findAll();
        return modelMapper.map(coordinates, new TypeToken<List<CoordinatesResponse>>(){}.getType());
    }

    @Transactional(rollbackFor = Exception.class)
    public CoordinatesResponse createCoordinates(CoordinatesRequest coordinatesRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Coordinates coordinates = modelMapper.map(coordinatesRequest, Coordinates.class);
        Coordinates savedCoordinates = coordinatesRepository.save(coordinates);
        ObjectAudit objectAudit = new ObjectAudit();
        objectAudit.setTableName("coordinates");
        objectAudit.setUser(user);
        objectAuditRepository.save(objectAudit);
        return modelMapper.map(savedCoordinates, CoordinatesResponse.class);
    }

}

