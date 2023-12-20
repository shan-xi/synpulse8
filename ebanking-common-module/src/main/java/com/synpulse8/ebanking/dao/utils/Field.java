package com.synpulse8.ebanking.dao.utils;

import java.util.List;

public interface Field<T> {

    String getName();

    T getValue();

    List<T> getValueList();

}
