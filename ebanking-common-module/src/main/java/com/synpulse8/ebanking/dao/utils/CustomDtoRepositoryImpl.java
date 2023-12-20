package com.synpulse8.ebanking.dao.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Repository
public class CustomDtoRepositoryImpl<T, X> implements CustomDtoRepository<T, X> {

    @Autowired
    private SearchHelper searchHelper;

    @Override
    public List<X> getList(Class<T> entityClass, Class<X> dtoClass, List<ConditionGroup> conditionGroupList) {
        var fields = dtoClass.getDeclaredFields();
        var fieldList = Arrays.stream(fields).map(Field::getName).toList();
        return searchHelper.searchHelper(conditionGroupList, entityClass, dtoClass, fieldList);
    }

    @Override
    public Page<X> getList(Class<T> entityClass, Class<X> dtoClass, PageRequest pageRequest, List<ConditionGroup> conditionGroupList) {
        var fields = dtoClass.getDeclaredFields();
        var fieldList = Arrays.stream(fields).map(Field::getName).toList();
        return searchHelper.pageableSearchHelper(conditionGroupList, entityClass, dtoClass, fieldList, pageRequest);
    }

}
