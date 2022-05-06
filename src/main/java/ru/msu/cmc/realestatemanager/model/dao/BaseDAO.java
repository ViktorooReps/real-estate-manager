package ru.msu.cmc.realestatemanager.model.dao;

import java.util.Collection;

public interface BaseDAO<SomeEntity> {
    void add(SomeEntity entity);
    void update(SomeEntity entity);
    void delete(SomeEntity entity);

    SomeEntity getById(Integer id);
}
