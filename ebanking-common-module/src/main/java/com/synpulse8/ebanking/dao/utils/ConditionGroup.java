package com.synpulse8.ebanking.dao.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConditionGroup {

    private List<Condition<Field<?>>> conditionList;
    private LogicOperator logicOperator;

}
