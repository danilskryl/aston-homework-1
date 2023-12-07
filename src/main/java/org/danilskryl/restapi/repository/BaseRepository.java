package org.danilskryl.restapi.repository;

import org.danilskryl.restapi.model.BaseEntity;

import java.util.List;

public interface BaseRepository<T extends BaseEntity> {
    T getById(Long id);

    List<T> getAll();

    T save(T t);

    T update(T t);

    boolean remove(Long id);
}
