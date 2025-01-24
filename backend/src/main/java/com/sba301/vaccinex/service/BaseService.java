package com.sba301.vaccinex.service;


import com.sba301.vaccinex.dto.response.PagingResponse;

public interface BaseService<T, ID> {
    PagingResponse findAll(int currentPage, int pageSize);
    T findById(ID id);
    T save(T entity);
}