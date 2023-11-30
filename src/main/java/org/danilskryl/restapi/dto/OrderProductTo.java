package org.danilskryl.restapi.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductTo {
    private OrderTo orderTo;
    private List<Long> productsId;
}
