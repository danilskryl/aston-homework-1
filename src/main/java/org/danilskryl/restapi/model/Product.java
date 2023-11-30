package org.danilskryl.restapi.model;

import lombok.*;

@Getter
@Setter
public class Product extends BaseEntity {
    private String name;
    private String description;
    private Long marketId;
}
