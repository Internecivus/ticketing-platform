package com.faust.ticketing.core.language;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Locale;

@AllArgsConstructor
public enum Language {
    en(new Locale("en")),
    hr(new Locale("hr"));

    @Getter
    final private Locale locale;

    public static Language valueOf(final @NonNull Locale locale) {
        return Arrays.stream(values()).filter(language ->
                language.locale.getLanguage().equals(locale.getLanguage())).findFirst().orElse(null);
    }
}
