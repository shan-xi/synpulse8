package com.synpulse8.ebanking.dao.utils;

import lombok.Data;

import java.util.List;

@Data
public abstract class AbstractField<T> implements Field<T> {

    private final String name;
    private T value;
    private List<T> valueList;

    public AbstractField(String name) {
        this.name = name;
    }

    public AbstractField(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public AbstractField(String name, List<T> valueList) {
        this.name = name;
        if (valueList.size() == 1) {
            this.value = valueList.get(0);
        }
        this.valueList = valueList;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public List<T> getValueList() {
        return valueList;
    }
}
