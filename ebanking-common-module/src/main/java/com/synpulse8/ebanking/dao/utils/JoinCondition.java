package com.synpulse8.ebanking.dao.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinCondition<J> {
    private String joinName; // use parent table attribute as relationship
    private J joinEntityClass;
    private ConditionGroup conditionGroup;
}
