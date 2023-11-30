package org.danilskryl.restapi.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ResponseData {
    private int status;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime issueAt;

    public static ResponseData constructResponseData(int status, String message) {
        return new ResponseDataBuilder()
                .status(status)
                .message(message)
                .issueAt(LocalDateTime.now())
                .build();
    }
}
