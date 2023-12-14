package org.danilskryl.restapi.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Order extends BaseEntity {
    private LocalDateTime orderDate;

    public Order() {
        orderDate = LocalDateTime.now();
    }
}
