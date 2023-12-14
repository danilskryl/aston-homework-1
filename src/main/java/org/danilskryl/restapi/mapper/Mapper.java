package org.danilskryl.restapi.mapper;

public interface Mapper<E, D> {
    E fromDto(D dto);

    D toDto(E market);
}
