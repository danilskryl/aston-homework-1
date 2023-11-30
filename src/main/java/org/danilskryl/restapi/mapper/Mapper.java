package org.danilskryl.restapi.mapper;

public interface Mapper<E, D> {
    E toEntity(D dto);

    D toDto(E market);
}
