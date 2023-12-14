package org.danilskryl.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDto {
    private OrderDto orderDto;
    private List<Long> productsId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderProductDto that)) return false;
        return Objects.equals(orderDto, that.orderDto) && Objects.equals(productsId, that.productsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderDto, productsId);
    }
}
