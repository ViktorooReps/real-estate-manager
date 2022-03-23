package ru.msu.cmc.realestatemanager.model.dao;

public interface BaseDAO<SomeEntity> {
    void add(SomeEntity entity);
    void update(SomeEntity entity);
    void delete(SomeEntity entity);

    SomeEntity getById(Long id);
}
