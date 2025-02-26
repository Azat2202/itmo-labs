package se.ifmo.is_lab1.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.ifmo.is_lab1.dto.collection.LocationRequest;
import se.ifmo.is_lab1.messages.collection.LocationResponse;
import se.ifmo.is_lab1.models.Location;
import se.ifmo.is_lab1.models.ObjectAudit;
import se.ifmo.is_lab1.models.User;
import se.ifmo.is_lab1.repositories.LocationRepository;
import se.ifmo.is_lab1.repositories.ObjectAuditRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;
    private final ObjectAuditRepository objectAuditRepository;

    @Transactional(readOnly = true)
    public List<LocationResponse> getAllLocations() {
        List<Location> locations = locationRepository.findAll();
        return modelMapper.map(locations, new TypeToken<List<LocationResponse>>(){}.getType());
    }

    @Transactional(rollbackFor = Exception.class)
    public LocationResponse createLocation(LocationRequest locationRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Location location = modelMapper.map(locationRequest, Location.class);
        Location savedLocation = locationRepository.save(location);
        ObjectAudit objectAudit = new ObjectAudit();
        objectAudit.setTableName("location");
        objectAudit.setUser(user);
        objectAuditRepository.save(objectAudit);
        return modelMapper.map(savedLocation, LocationResponse.class);
    }

}
