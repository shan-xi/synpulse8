package com.synpulse8.ebanking.dao.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class SearchHelper {

    @Autowired
    private EntityManager em;

    public <T> List<T> searchHelper(ConditionGroup conditionGroup, Class<T> clazz) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> root = cq.from(clazz);
        Predicate predicate = this.buildPredicateArray(conditionGroup, cb, root);
        cq.where(predicate);
        TypedQuery<T> query = em.createQuery(cq);
        return query.getResultList();
    }

    public <T> List<T> searchHelper(ConditionGroup conditionGroup, Class<T> clazz, Sort sort) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> root = cq.from(clazz);
        Predicate predicate = this.buildPredicateArray(conditionGroup, cb, root);
        cq.where(predicate);

        var iter = sort.iterator();
        List<Order> orderList = new ArrayList<>();
        while (iter.hasNext()) {
            var v = iter.next();
            if (v.getDirection() == Sort.Direction.ASC) {
                orderList.add(cb.asc(root.get(v.getProperty())));
            } else if (v.getDirection() == Sort.Direction.DESC) {
                orderList.add(cb.desc(root.get(v.getProperty())));
            }
        }
        cq.orderBy(orderList);

        TypedQuery<T> query = em.createQuery(cq);
        return query.getResultList();
    }

    public <T> Page<T> pageableSearchHelper(ConditionGroup conditionGroup, Class<T> clazz, PageRequest pageRequest) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> root = cq.from(clazz);
        Predicate predicates = this.buildPredicateArray(conditionGroup, cb, root);
        cq.where(predicates);

        Sort sort = pageRequest.getSort();
        var iter = sort.iterator();
        List<Order> orderList = new ArrayList<>();
        while (iter.hasNext()) {
            var v = iter.next();
            if (v.getDirection() == Sort.Direction.ASC) {
                orderList.add(cb.asc(root.get(v.getProperty())));
            } else if (v.getDirection() == Sort.Direction.DESC) {
                orderList.add(cb.desc(root.get(v.getProperty())));
            }
        }
        cq.orderBy(orderList);

        TypedQuery<T> query = em.createQuery(cq)
                .setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize()).setMaxResults(pageRequest.getPageSize());

        CriteriaBuilder cb2 = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq2 = cb2.createQuery(Long.class);
        Root rootCount = cq2.from(clazz);
        Predicate predicates2 = this.buildPredicateArray(conditionGroup, cb2, rootCount);
        cq2.select(cb2.count(rootCount)).where(predicates2);
        TypedQuery<Long> query2 = em.createQuery(cq2);
        return new PageImpl<T>(query.getResultList(), pageRequest, query2.getResultList().get(0));
    }

    public <T> Page<T> pageableSearchHelper(List<ConditionGroup> conditionGroupList, Class<T> clazz, PageRequest pageRequest) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> root = cq.from(clazz);

        List<Predicate> predicateList = new ArrayList<>();
        for (var conditionGroup : conditionGroupList) {
            Predicate predicates = this.buildPredicateArray(conditionGroup, cb, root);
            predicateList.add(predicates);
        }
        var predicateArray = new Predicate[predicateList.size()];
        cq.where(cb.and(predicateList.toArray(predicateArray)));

        Sort sort = pageRequest.getSort();
        var iter = sort.iterator();
        List<Order> orderList = new ArrayList<>();
        while (iter.hasNext()) {
            var v = iter.next();
            if (v.getDirection() == Sort.Direction.ASC) {
                orderList.add(cb.asc(root.get(v.getProperty())));
            } else if (v.getDirection() == Sort.Direction.DESC) {
                orderList.add(cb.desc(root.get(v.getProperty())));
            }
        }
        cq.orderBy(orderList);

        TypedQuery<T> query = em.createQuery(cq)
                .setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize()).setMaxResults(pageRequest.getPageSize());
        var content = query.getResultList();

        CriteriaBuilder cb2 = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq2 = cb2.createQuery(Long.class);
        Root rootCount = cq2.from(clazz);

        List<Predicate> predicateList2 = new ArrayList<>();
        for (var conditionGroup : conditionGroupList) {
            Predicate predicates = this.buildPredicateArray(conditionGroup, cb2, rootCount);
            predicateList2.add(predicates);
        }
        cq2.select(cb2.count(rootCount)).where(predicateList2.toArray(new Predicate[0]));
        TypedQuery<Long> query2 = em.createQuery(cq2);
        return new PageImpl<T>(content, pageRequest, query2.getResultList().get(0));
    }

    private <T> Predicate buildPredicateArray(ConditionGroup conditionGroup, CriteriaBuilder cb, From<T, T> root) {

        Predicate finalCriteria;
        var predicateList = new ArrayList<Predicate>();
        for (Condition<Field<?>> c : conditionGroup.getConditionList()) {
            Predicate criteria = null;
            if (c.getSqlOperator().equals(SqlOperator.LIKE)) {
                criteria = cb.like(root.get(c.getField().getName()), "%" + c.getField().getValue() + "%");
            } else if (c.getSqlOperator().equals(SqlOperator.LIKE_IGNORE_CASE)) {
                criteria = cb.like(
                        cb.lower(root.get(c.getField().getName())),
                        "%" + c.getField().getValue().toString().toLowerCase() + "%");
            } else if (c.getSqlOperator().equals(SqlOperator.NOT)) {
                criteria = cb.notEqual(root.get(c.getField().getName()), c.getField().getValue());
            } else if (c.getSqlOperator().equals(SqlOperator.IS)) {
                criteria = cb.equal(root.get(c.getField().getName()), c.getField().getValue());
            } else if (c.getSqlOperator().equals(SqlOperator.NOT_IN)) {
                criteria = root.get(c.getField().getName()).in(c.getField().getValueList()).not().isNull();
            } else if (c.getSqlOperator().equals(SqlOperator.IN)) {
                criteria = root.get(c.getField().getName()).in(c.getField().getValueList());
            } else if (c.getSqlOperator().equals(SqlOperator.GREATER_THAN)) {
                criteria = cb.greaterThan(root.get(c.getField().getName()), (long) c.getField().getValue());
            } else if (c.getSqlOperator().equals(SqlOperator.LESS_THAN)) {
                criteria = cb.lessThan(root.get(c.getField().getName()), (long) c.getField().getValue());
            } else if (c.getSqlOperator().equals(SqlOperator.GREATER_THAN_TO_DATE)) {
                criteria = cb.greaterThan(root.get(c.getField().getName()), (Date) c.getField().getValue());
            } else if (c.getSqlOperator().equals(SqlOperator.LESS_THAN_TO_DATE)) {
                criteria = cb.lessThan(root.get(c.getField().getName()), (Date) c.getField().getValue());
            } else if (c.getSqlOperator().equals(SqlOperator.GREATER_THAN_OR_EQUAL_TO)) {
                criteria = cb.greaterThanOrEqualTo(root.get(c.getField().getName()), (long) c.getField().getValue());
            } else if (c.getSqlOperator().equals(SqlOperator.LESS_THAN_OR_EQUAL_TO)) {
                criteria = cb.lessThanOrEqualTo(root.get(c.getField().getName()), (long) c.getField().getValue());
            } else if (c.getSqlOperator().equals(SqlOperator.GREATER_THAN_OR_EQUAL_TO_DATE)) {
                criteria = cb.greaterThanOrEqualTo(root.get(c.getField().getName()), (Date) c.getField().getValue());
            } else if (c.getSqlOperator().equals(SqlOperator.LESS_THAN_OR_EQUAL_TO_DATE)) {
                criteria = cb.lessThanOrEqualTo(root.get(c.getField().getName()), (Date) c.getField().getValue());
            } else if (c.getSqlOperator().equals(SqlOperator.GREATER_THAN_OR_EQUAL_TO_LOCAL_DATE)) {
                criteria = cb.greaterThanOrEqualTo(root.get(c.getField().getName()), (LocalDate) c.getField().getValue());
            } else if (c.getSqlOperator().equals(SqlOperator.LESS_THAN_OR_EQUAL_TO_LOCAL_DATE)) {
                criteria = cb.lessThanOrEqualTo(root.get(c.getField().getName()), (LocalDate) c.getField().getValue());
            }
            predicateList.add(criteria);
        }

        var predicateArray = new Predicate[conditionGroup.getConditionList().size()];
        if (conditionGroup.getLogicOperator().equals(LogicOperator.OR)) {
            finalCriteria = cb.or(predicateList.toArray(predicateArray));
        } else {
            finalCriteria = cb.and(predicateList.toArray(predicateArray));
        }

        return finalCriteria;
    }

    public <T> Page<T> pageableSearchHelperWithJoin(
            ConditionGroup conditionGroup,
            Class<T> mainEntityClass,
            PageRequest pageRequest,
            List<JoinCondition<?>> joinConditionList,
            LogicOperator joinConditionLogicOperator) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(mainEntityClass);
        Root<T> root = cq.from(mainEntityClass);
        Predicate predicates = this.buildPredicateArray(conditionGroup, cb, root);

        List<Predicate> joinPredicateList = new ArrayList<>();
        for (var joinObj : joinConditionList) {
            var join = root.join(joinObj.getJoinName());
            Predicate joinPredicate = this.buildPredicateArray(joinObj.getConditionGroup(), cb, join);
            joinPredicateList.add(joinPredicate);
        }
        joinPredicateList.add(predicates);

        if (joinConditionLogicOperator != null && joinConditionLogicOperator.equals(LogicOperator.OR)) {
            Predicate finalJoinPredicate = cb.or(joinPredicateList.toArray(new Predicate[0]));
            cq.where(finalJoinPredicate);
        } else {
            cq.where(joinPredicateList.toArray(new Predicate[0]));
        }

        Sort sort = pageRequest.getSort();
        var iter = sort.iterator();
        List<Order> orderList = new ArrayList<>();
        while (iter.hasNext()) {
            var v = iter.next();
            if (v.getDirection() == Sort.Direction.ASC) {
                orderList.add(cb.asc(root.get(v.getProperty())));
            } else if (v.getDirection() == Sort.Direction.DESC) {
                orderList.add(cb.desc(root.get(v.getProperty())));
            }
        }
        cq.orderBy(orderList);

        TypedQuery<T> query = em.createQuery(cq)
                .setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize()).setMaxResults(pageRequest.getPageSize());

        CriteriaBuilder cb2 = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq2 = cb2.createQuery(Long.class);
        Root rootCount = cq2.from(mainEntityClass);
        Predicate predicates2 = this.buildPredicateArray(conditionGroup, cb2, rootCount);

        List<Predicate> joinPredicateList2 = new ArrayList<>();
        for (var joinObj : joinConditionList) {
            var join = rootCount.join(joinObj.getJoinName());
            Predicate joinPredicate = this.buildPredicateArray(joinObj.getConditionGroup(), cb2, join);
            joinPredicateList2.add(joinPredicate);
        }
        joinPredicateList2.add(predicates2);

        if (joinConditionLogicOperator != null && joinConditionLogicOperator.equals(LogicOperator.OR)) {
            Predicate finalJoinPredicate = cb2.or(joinPredicateList2.toArray(new Predicate[0]));
            cq2.select(cb2.count(rootCount)).where(finalJoinPredicate);
        } else {
            cq2.select(cb2.count(rootCount)).where(joinPredicateList2.toArray(new Predicate[0]));
        }
        TypedQuery<Long> query2 = em.createQuery(cq2);
        return new PageImpl<T>(query.getResultList(), pageRequest, query2.getResultList().get(0));
    }

    public <T> List<T> searchHelperWithJoin(
            ConditionGroup conditionGroup,
            Class<T> clazz,
            Sort sort,
            List<JoinCondition<?>> joinConditionList,
            LogicOperator joinConditionLogicOperator) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> root = cq.from(clazz);
        Predicate predicate = this.buildPredicateArray(conditionGroup, cb, root);

        List<Predicate> joinPredicateList = new ArrayList<>();
        for (var joinObj : joinConditionList) {
            var join = root.join(joinObj.getJoinName());
            Predicate joinPredicate = this.buildPredicateArray(joinObj.getConditionGroup(), cb, join);
            joinPredicateList.add(joinPredicate);
        }
        joinPredicateList.add(predicate);


        if (joinConditionLogicOperator != null && joinConditionLogicOperator.equals(LogicOperator.OR)) {
            Predicate finalJoinPredicate = cb.or(joinPredicateList.toArray(new Predicate[0]));
            cq.where(finalJoinPredicate);
        } else {
            cq.where(joinPredicateList.toArray(new Predicate[0]));
        }

        var iter = sort.iterator();
        List<Order> orderList = new ArrayList<>();
        while (iter.hasNext()) {
            var v = iter.next();
            if (v.getDirection() == Sort.Direction.ASC) {
                orderList.add(cb.asc(root.get(v.getProperty())));
            } else if (v.getDirection() == Sort.Direction.DESC) {
                orderList.add(cb.desc(root.get(v.getProperty())));
            }
        }
        cq.orderBy(orderList);

        TypedQuery<T> query = em.createQuery(cq);
        return query.getResultList();
    }

    public <T> List<T> searchHelper(List<ConditionGroup> conditionGroupList, Class<T> clazz) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> root = cq.from(clazz);
        List<Predicate> predicateList = new ArrayList<>();
        for (var conditionGroup : conditionGroupList) {
            Predicate predicates = this.buildPredicateArray(conditionGroup, cb, root);
            predicateList.add(predicates);
        }
        var predicateArray = new Predicate[predicateList.size()];
        cq.where(cb.and(predicateList.toArray(predicateArray)));
        TypedQuery<T> query = em.createQuery(cq);
        return query.getResultList();
    }

    public <T> Page<T> pageableSearchHelper(Class<T> clazz, PageRequest pageRequest) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> root = cq.from(clazz);
        Predicate predicates = this.buildPredicateArray(new ConditionGroup(List.of(), LogicOperator.AND), cb, root);
        cq.where(predicates);

        Sort sort = pageRequest.getSort();
        var iter = sort.iterator();
        List<Order> orderList = new ArrayList<>();
        while (iter.hasNext()) {
            var v = iter.next();
            if (v.getDirection() == Sort.Direction.ASC) {
                orderList.add(cb.asc(root.get(v.getProperty())));
            } else if (v.getDirection() == Sort.Direction.DESC) {
                orderList.add(cb.desc(root.get(v.getProperty())));
            }
        }
        cq.orderBy(orderList);

        TypedQuery<T> query = em.createQuery(cq)
                .setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize()).setMaxResults(pageRequest.getPageSize());

        CriteriaBuilder cb2 = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq2 = cb2.createQuery(Long.class);
        Root rootCount = cq2.from(clazz);
        Predicate predicates2 = this.buildPredicateArray(new ConditionGroup(List.of(), LogicOperator.AND), cb2, rootCount);
        cq2.select(cb2.count(rootCount)).where(predicates2);
        TypedQuery<Long> query2 = em.createQuery(cq2);
        return new PageImpl<T>(query.getResultList(), pageRequest, query2.getResultList().get(0));
    }


    public <T, X> List<X> searchHelper(List<ConditionGroup> conditionGroupList, Class<T> entityClass, Class<X> dtoClass, List<String> fieldList) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<X> cq = cb.createQuery(dtoClass);
        Root<T> root = cq.from(entityClass);

        List<Predicate> predicateList = new ArrayList<>();
        for (var conditionGroup : conditionGroupList) {
            Predicate predicates = this.buildPredicateArray(conditionGroup, cb, root);
            predicateList.add(predicates);
        }

        var predicateArray = new Predicate[predicateList.size()];
        cq.where(cb.and(predicateList.toArray(predicateArray)));

        var selectionList = fieldList.stream().map(root::get).toList();
        cq.select(cb.construct(dtoClass, selectionList.toArray(new Selection[0])));

        TypedQuery<X> query = em.createQuery(cq);
        return query.getResultList();
    }

    public <T, X> Page<X> pageableSearchHelper(List<ConditionGroup> conditionGroupList, Class<T> entityClass, Class<X> dtoClass, List<String> fieldList, PageRequest pageRequest) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<X> cq = cb.createQuery(dtoClass);
        Root<T> root = cq.from(entityClass);

        List<Predicate> predicateList = new ArrayList<>();
        for (var conditionGroup : conditionGroupList) {
            Predicate predicates = this.buildPredicateArray(conditionGroup, cb, root);
            predicateList.add(predicates);
        }
        var predicateArray = new Predicate[predicateList.size()];
        cq.where(cb.and(predicateList.toArray(predicateArray)));

        var selectionList = fieldList.stream().map(root::get).toList();
        cq.select(cb.construct(dtoClass, selectionList.toArray(new Selection[0])));

        Sort sort = pageRequest.getSort();
        var iter = sort.iterator();
        List<Order> orderList = new ArrayList<>();
        while (iter.hasNext()) {
            var v = iter.next();
            if (v.getDirection() == Sort.Direction.ASC) {
                orderList.add(cb.asc(root.get(v.getProperty())));
            } else if (v.getDirection() == Sort.Direction.DESC) {
                orderList.add(cb.desc(root.get(v.getProperty())));
            }
        }
        cq.orderBy(orderList);

        TypedQuery<X> query = em.createQuery(cq)
                .setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize()).setMaxResults(pageRequest.getPageSize());
        var content = query.getResultList();

        CriteriaBuilder cb2 = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq2 = cb2.createQuery(Long.class);
        Root rootCount = cq2.from(entityClass);

        List<Predicate> predicateList2 = new ArrayList<>();
        for (var conditionGroup : conditionGroupList) {
            Predicate predicates = this.buildPredicateArray(conditionGroup, cb2, rootCount);
            predicateList2.add(predicates);
        }
        cq2.select(cb2.count(rootCount)).where(predicateList2.toArray(new Predicate[0]));
        TypedQuery<Long> query2 = em.createQuery(cq2);
        return new PageImpl<X>(content, pageRequest, query2.getResultList().get(0));
    }
}
