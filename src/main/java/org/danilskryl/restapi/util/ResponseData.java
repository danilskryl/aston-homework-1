package org.danilskryl.restapi.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
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
