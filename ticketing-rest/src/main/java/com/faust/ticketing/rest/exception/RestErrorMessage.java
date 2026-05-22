package com.faust.ticketing.rest.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class RestErrorMessage {
    private Instant timestamp;
    private Integer status;
    private String code;
    private String message;
    private String devMessage;
}
