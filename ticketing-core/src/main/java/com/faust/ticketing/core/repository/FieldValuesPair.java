package com.faust.ticketing.core.repository;

import lombok.Data;

import java.util.Set;

@Data
public class FieldValuesPair {
    private final String fieldName;
    private final Set<?> fieldValues;
}
