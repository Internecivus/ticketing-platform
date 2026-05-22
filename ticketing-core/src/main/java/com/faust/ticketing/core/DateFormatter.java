package com.faust.ticketing.core;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    public static String shortFormat(final Instant instant) {
        return instant != null
                ? DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withZone(ZoneId.from(ZoneOffset.UTC)).format(instant)
                : null;
    }
}
