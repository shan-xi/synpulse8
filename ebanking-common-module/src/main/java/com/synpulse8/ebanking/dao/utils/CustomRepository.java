package com.synpulse8.ebanking.dao.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CustomRepository<T> {
    Page<T> getList(Class<T> entityClass, PageRequest pageRequest, ConditionGroup conditionGroup);

    Page<T> getList(Class<T> entityClass, PageRequest pageRequest, List<ConditionGroup> conditionGroupList);

    Page<T> getList(Class<T> entityClass, PageRequest pageRequest, ConditionGroup conditionGroup, List<JoinCondition<?>> joinConditionList);

    Page<T> getList(Class<T> mainEntityClass, PageRequest pageRequest, ConditionGroup conditionGroup, List<JoinCondition<?>> joinConditionList, LogicOperator joinConditionLogicOperator);

    List<T> getList(Class<T> entityClass, ConditionGroup conditionGroup);

    List<T> getList(Class<T> entityClass, List<ConditionGroup> conditionGroupList);

    List<T> getList(Class<T> entityClass, ConditionGroup conditionGroup, Sort sort);

    List<T> getList(Class<T> entityClass, ConditionGroup conditionGroup, Sort sort, List<JoinCondition<?>> joinConditionList, LogicOperator joinConditionLogicOperator);

    List<T> getList(Class<T> entityClass, ConditionGroup conditionGroup, Sort sort, List<JoinCondition<?>> joinConditionList);

    Page<T> getList(Class<T> entityClass, PageRequest pageRequest);

    List<?> getList(Class<T> entityClass, Class<?> dtoClass, List<ConditionGroup> conditionGroupList);

    Page<?> getList(Class<T> entityClass, Class<?> dtoClass, PageRequest pageRequest, List<ConditionGroup> conditionGroupList);

}
