package se.ifmo.is_lab1.messages.collection;

import lombok.Data;
import se.ifmo.is_lab1.messages.authentication.UserResponse;

@Data
public class FeedResponse {
    private Long id;

    private java.time.ZonedDateTime creationDate;

    private String feedUrl;

    private Integer batchSize;

    private Boolean isSuccessful;

    private UserResponse user;
}
