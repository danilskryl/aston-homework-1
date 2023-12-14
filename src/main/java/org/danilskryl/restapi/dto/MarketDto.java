package org.danilskryl.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MarketDto {
    private Long id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarketDto marketDto)) return false;
        return Objects.equals(id, marketDto.id) && Objects.equals(name, marketDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
