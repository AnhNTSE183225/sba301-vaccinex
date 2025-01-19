package com.vaccine.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class BaseServiceImp <T, ID> implements BaseService <T, ID> {

    private final JpaRepository<T, ID> repository;

    public BaseServiceImp(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public List<T> findAll() {
        return this.repository.findAll();
    }

    @Override
    public T findById(ID id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public T save(T entity) {
        return this.repository.save(entity);
    }

}

