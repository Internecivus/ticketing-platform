package com.faust.ticketing.rest;

import com.faust.ticketing.core.language.LanguageSelector;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.annotation.WebFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class LanguageFilter implements ContainerRequestFilter {
    @Inject
    private LanguageSelector language;

    @Override
    public void filter(final ContainerRequestContext containerRequestContext) {
        language.select(containerRequestContext.getLanguage());
    }
}
