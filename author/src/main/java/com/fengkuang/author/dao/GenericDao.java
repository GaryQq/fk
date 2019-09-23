package com.fengkuang.author.dao;

import java.util.List;
import java.util.Map;

public interface GenericDao<T> {

    int insert(T t);

    int update(T t);

    T getById(Long id);

    T getByUserName(String userName);

    List<T> getByParams(Map<String, Object> params);

}
