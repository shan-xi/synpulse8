package com.synpulse8.ebanking.dao.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CustomDtoRepository<T, X> {

    List<X> getList(Class<T> entityClass, Class<X> dtoClass, List<ConditionGroup> conditionGroupList);

    Page<X> getList(Class<T> entityClass, Class<X> dtoClass, PageRequest pageRequest, List<ConditionGroup> conditionGroupList);

}
