package com.synpulse8.ebanking.dao.utils.fields;

import com.synpulse8.ebanking.dao.utils.AbstractField;

import java.util.Date;
import java.util.List;

public class FieldCreateTime extends AbstractField<Date> {

    private static final String name = "createTime";

    public FieldCreateTime() {
        super(name);
    }

    public FieldCreateTime(List<Date> valueList) {
        super(name, valueList);
    }

    public FieldCreateTime(Date value) {
        super(name, value);
    }
}