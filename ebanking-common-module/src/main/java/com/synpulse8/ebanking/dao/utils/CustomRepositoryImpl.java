package com.synpulse8.ebanking.dao.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Repository
public class CustomRepositoryImpl<T> implements CustomRepository<T> {

    @Autowired
    private SearchHelper searchHelper;

    @Override
    public Page<T> getList(Class<T> entityClass, PageRequest pageRequest, ConditionGroup conditionGroup) {
        return searchHelper.pageableSearchHelper(conditionGroup, entityClass, pageRequest);
    }

    @Override
    public Page<T> getList(Class<T> entityClass, PageRequest pageRequest, List<ConditionGroup> conditionGroupList) {
        return searchHelper.pageableSearchHelper(conditionGroupList, entityClass, pageRequest);
    }

    @Override
    public Page<T> getList(Class<T> mainEntityClass, PageRequest pageRequest, ConditionGroup conditionGroup, List<JoinCondition<?>> joinConditionList, LogicOperator joinConditionLogicOperator) {
        return searchHelper.pageableSearchHelperWithJoin(conditionGroup, mainEntityClass, pageRequest, joinConditionList, joinConditionLogicOperator);
    }

    @Override
    public Page<T> getList(Class<T> mainEntityClass, PageRequest pageRequest, ConditionGroup conditionGroup, List<JoinCondition<?>> joinConditionList) {
        return searchHelper.pageableSearchHelperWithJoin(conditionGroup, mainEntityClass, pageRequest, joinConditionList, LogicOperator.AND);
    }

    @Override
    public List<T> getList(Class<T> entityClass, ConditionGroup conditionGroup) {
        return searchHelper.searchHelper(conditionGroup, entityClass);
    }

    @Override
    public List<T> getList(Class<T> entityClass, List<ConditionGroup> conditionGroupList) {
        return searchHelper.searchHelper(conditionGroupList, entityClass);
    }


    @Override
    public List<T> getList(Class<T> entityClass, ConditionGroup conditionGroup, Sort sort) {
        return searchHelper.searchHelper(conditionGroup, entityClass, sort);
    }

    @Override
    public List<T> getList(Class<T> entityClass, ConditionGroup conditionGroup, Sort sort, List<JoinCondition<?>> joinConditionList, LogicOperator joinConditionLogicOperator) {
        return searchHelper.searchHelperWithJoin(conditionGroup, entityClass, sort, joinConditionList, joinConditionLogicOperator);
    }

    @Override
    public List<T> getList(Class<T> entityClass, ConditionGroup conditionGroup, Sort sort, List<JoinCondition<?>> joinConditionList) {
        return searchHelper.searchHelperWithJoin(conditionGroup, entityClass, sort, joinConditionList, LogicOperator.AND);
    }

    @Override
    public Page<T> getList(Class<T> entityClass, PageRequest pageRequest) {
        return searchHelper.pageableSearchHelper(entityClass, pageRequest);
    }

    @Override
    public List<?> getList(Class<T> entityClass, Class<?> dtoClass, List<ConditionGroup> conditionGroupList) {
        var fields = dtoClass.getDeclaredFields();
        var fieldList = Arrays.stream(fields).map(Field::getName).toList();
        return searchHelper.searchHelper(conditionGroupList, entityClass, dtoClass, fieldList);
    }

    @Override
    public Page<?> getList(Class<T> entityClass, Class<?> dtoClass, PageRequest pageRequest, List<ConditionGroup> conditionGroupList) {
        var fields = dtoClass.getDeclaredFields();
        var fieldList = Arrays.stream(fields).map(Field::getName).toList();
        return searchHelper.pageableSearchHelper(conditionGroupList, entityClass, dtoClass, fieldList, pageRequest);
    }

}
