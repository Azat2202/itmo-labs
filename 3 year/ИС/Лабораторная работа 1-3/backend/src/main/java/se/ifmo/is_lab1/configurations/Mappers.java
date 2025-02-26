package se.ifmo.is_lab1.configurations;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.ifmo.is_lab1.dto.collection.UpdateStudyGroupRequest;
import se.ifmo.is_lab1.models.StudyGroup;

@Configuration
public class Mappers {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        TypeMap<UpdateStudyGroupRequest, StudyGroup> updateStudyGroupMapper =
                mapper.createTypeMap(UpdateStudyGroupRequest.class, StudyGroup.class);
        updateStudyGroupMapper.addMappings(
                expression -> {
                    expression.map(
                            src -> null, StudyGroup::setCoordinates
                    );
                    expression.map(
                            src -> null, StudyGroup::setGroupAdmin
                    );
                }
        );
        return mapper;
    }
}
