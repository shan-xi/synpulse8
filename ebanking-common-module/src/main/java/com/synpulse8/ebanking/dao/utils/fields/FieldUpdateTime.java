package com.synpulse8.ebanking.dao.utils.fields;

import com.synpulse8.ebanking.dao.utils.AbstractField;

import java.util.Date;
import java.util.List;

public class FieldUpdateTime extends AbstractField<Date> {

    private static final String name = "updateTime";

    public FieldUpdateTime() {
        super(name);
    }

    public FieldUpdateTime(List<Date> valueList) {
        super(name, valueList);
    }

    public FieldUpdateTime(Date value) {
        super(name, value);
    }
}