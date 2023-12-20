package com.synpulse8.ebanking.dao.transaction.field;

import com.synpulse8.ebanking.dao.utils.AbstractField;

import java.time.LocalDate;
import java.util.List;

public class FieldTransactionValueDate extends AbstractField<LocalDate> {

    private static final String name = "valueDate";

    public FieldTransactionValueDate(List<LocalDate> valueList) {
        super(name, valueList);
    }

    public FieldTransactionValueDate(LocalDate value) {
        super(name, value);
    }
}
