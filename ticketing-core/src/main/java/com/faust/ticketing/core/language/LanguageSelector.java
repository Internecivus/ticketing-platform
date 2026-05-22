package com.faust.ticketing.core.language;

import lombok.Getter;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import java.util.Locale;

@RequestScoped
public class LanguageSelector {
    @Getter
    private final Language defaultLanguage =  Language.en;
    private Language selectedLanguage;

    public void select(final Locale locale) {
        if (locale != null) {
            this.selectedLanguage = Language.valueOf(locale);
        }
    }

    public void select(final Language language) {
        if (language != null) {
            this.selectedLanguage = language;
        }
    }

    public Language getSelected() {
        return selectedLanguage != null ? selectedLanguage : defaultLanguage;
    }

    public Locale getSelectedLocale() {
        return selectedLanguage != null ? selectedLanguage.getLocale() : defaultLanguage.getLocale();
    }
}
