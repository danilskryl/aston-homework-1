package org.danilskryl.restapi.service;

import java.util.List;

public interface BaseService<T> {
    T getById(Long id);

    List<T> getAll();

    T save(T t);

    T update(T t);

    boolean remove(Long id);
}
