package org.danilskryl.restapi.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MarketTo {
    private Long id;
    private String name;
}
