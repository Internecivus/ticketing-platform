package com.faust.ticketing.core.service.messaging.email;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class Email implements Serializable {
    private static final long serialVersionUID = -8145165153855419965L;

    private Set<String> to = new HashSet<>();
    private Set<String> cc = new HashSet<>();
    private Set<String> bcc = new HashSet<>();
    private String from;
    private String message;
    private String subject;
}
