package org.danilskryl.restapi.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductTo {
    private Long id;
    private String name;
    private String description;
    private Long marketId;
}
