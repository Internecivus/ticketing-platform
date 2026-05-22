package com.faust.ticketing.rest.authentication;

import com.faust.ticketing.core.configuration.ServerMode;
import com.faust.ticketing.core.language.LanguageSelector;
import com.faust.ticketing.rest.CORSFilter;
import com.faust.ticketing.rest.LanguageFilter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.authentication.mechanism.http.RememberMe;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.servlet.FilterRegistration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;

@AutoApplySession
@ApplicationScoped
@RememberMe(
        cookieMaxAgeSeconds = 60 * 60 * 24 * 14, // 14 days
        cookieSecureOnly = false,
        cookieHttpOnly = false
)
public class Authentication implements HttpAuthenticationMechanism {
    @Inject
    private CredentialStore credentialStore;

    @Inject
    private LanguageSelector language;

    @Override
    public AuthenticationStatus validateRequest(final HttpServletRequest request,
            final HttpServletResponse response, final HttpMessageContext context) {
        language.select(request.getLocale());

//        if (ServerMode.isDevelopment()) {
//            return notifyContainerAboutAdminLogin(context);
//        }
        final Credential credential = context.getAuthParameters().getCredential();

        if (credential != null) {
            final CredentialValidationResult validationResult = credentialStore.validate(credential);
            if (validationResult != null && validationResult.getStatus().equals(CredentialValidationResult.Status.VALID)) {
                //context.setRegisterSession(validationResult.getCallerPrincipal().getName(), validationResult.getCallerGroups());   and isRegister in next else if
                return context.notifyContainerAboutLogin(validationResult);
            }
        }
        else if (!context.isProtected()) {
            return context.doNothing();
        }

        return context.responseUnauthorized();
    }

    private AuthenticationStatus notifyContainerAboutAdminLogin(final HttpMessageContext context) {
        final CredentialValidationResult validationResult = credentialStore.validate(
                new UsernamePasswordCredential("test@test.com", "6wFeKkcyPU2Sa3c/ROA="));
        context.setRegisterSession(validationResult.getCallerPrincipal().getName(), validationResult.getCallerGroups());
        return context.notifyContainerAboutLogin(validationResult);
    }
}
