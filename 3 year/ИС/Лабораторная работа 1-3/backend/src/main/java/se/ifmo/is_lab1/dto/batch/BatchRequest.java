package se.ifmo.is_lab1.dto.batch;

import lombok.Data;
import se.ifmo.is_lab1.dto.collection.StudyGroupRequest;

import java.util.List;

@Data
public class BatchRequest {
    private List<StudyGroupBatchRequest> groups;
}
