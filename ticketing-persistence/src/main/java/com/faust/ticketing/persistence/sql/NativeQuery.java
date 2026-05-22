package com.faust.ticketing.persistence.sql;

import lombok.Getter;
import lombok.NonNull;

import java.io.File;
import java.util.Objects;

public enum NativeQuery {
    test("test");

    private final static String SQL_FOLDER_NAME = "native";
    private final static String SQL_EXTENSION = ".sql";

    @Getter
    private final String fileName;
    @Getter
    private final String sql;

    NativeQuery(@NonNull final String fileName) {
        this.fileName = fileName;
        this.sql = Objects.requireNonNull(Thread.currentThread().getContextClassLoader()
            .getResourceAsStream(SQL_FOLDER_NAME + File.separator + fileName + SQL_EXTENSION)).toString();
    }
}
