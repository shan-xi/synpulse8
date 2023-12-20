package com.synpulse8.ebanking.dao.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Condition<T extends Field<?>> {

    private T field;

    private SqlOperator sqlOperator;

}
