package com.faust.ticketing.rest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

public class SecurityFilter implements ContainerResponseFilter {
    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) {
        responseContext.getHeaders().add("Content-Security-Policy", "frame-ancestors 'none'");
        responseContext.getHeaders().add("X-Frame-Options", "DENY");
    }
}
