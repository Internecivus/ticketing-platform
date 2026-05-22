package com.faust.ticketing.core.repository;

import lombok.Data;

@Data
public class FieldValuePair {
    private final String fieldName;
    private final Object fieldValue;
}
