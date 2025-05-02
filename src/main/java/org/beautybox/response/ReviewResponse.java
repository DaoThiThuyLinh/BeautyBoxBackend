package org.beautybox.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    String id;
    String userName;
    LocalDateTime createdDate;
    int rating;
    String comment;
    List<childComment> replies;

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static  class childComment{
        String id;
        String userName;
        LocalDateTime createdDate;
        String comment;
    }
}
