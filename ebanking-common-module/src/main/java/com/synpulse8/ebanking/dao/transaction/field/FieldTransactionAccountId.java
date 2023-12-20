package com.synpulse8.ebanking.dao.transaction.field;

import com.synpulse8.ebanking.dao.utils.AbstractField;

import java.util.List;

public class FieldTransactionAccountId extends AbstractField<Long> {

    private static final String name = "accountId";

    public FieldTransactionAccountId(List<Long> valueList) {
        super(name, valueList);
    }

    public FieldTransactionAccountId(Long value) {
        super(name, value);
    }
}
