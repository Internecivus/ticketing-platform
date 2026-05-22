package com.faust.ticketing.rest;

import com.faust.ticketing.core.configuration.ServerConfiguration;
import com.faust.ticketing.core.configuration.ServerMode;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {
    private final static String HEADER_ORIGIN = "origin";

    private final static String HEADER_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private final static String HEADER_ALLOW_ORIGIN_VALUE = ServerConfiguration.DOMAIN_NAME;

    private final static String HEADER_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    private final static String HEADER_ALLOW_HEADERS_VALUE = String.join(",",
            HEADER_ORIGIN,
            HttpHeaders.AUTHORIZATION,
            HttpHeaders.ACCEPT,
            HttpHeaders.CONTENT_TYPE);

    private final static String HEADER_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    private final static String HEADER_ALLOW_CREDENTIALS_VALUE = Boolean.TRUE.toString();

    private final static String HEADER_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private final static String HEADER_ALLOW_METHODS_VALUE = String.join(",",
            HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.OPTIONS, HttpMethod.HEAD);

    private final static String HEADER_MAX_AGE = "Access-Control-Max-Age";
    private final static String HEADER_MAX_AGE_VALUE_S = String.valueOf(60 * 60 * 24 * 30);

    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) {
        responseContext.getHeaders().add(HEADER_ALLOW_ORIGIN, HEADER_ALLOW_ORIGIN_VALUE);
        responseContext.getHeaders().add(HEADER_ALLOW_HEADERS, HEADER_ALLOW_HEADERS_VALUE);
        responseContext.getHeaders().add(HEADER_ALLOW_CREDENTIALS, HEADER_ALLOW_CREDENTIALS_VALUE);
        responseContext.getHeaders().add(HEADER_ALLOW_METHODS, HEADER_ALLOW_METHODS_VALUE);
        responseContext.getHeaders().add(HEADER_MAX_AGE, HEADER_MAX_AGE_VALUE_S);
    }
}
