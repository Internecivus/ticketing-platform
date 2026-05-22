package com.faust.ticketing.core.exception;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

@Inherited
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.METHOD, ElementType.TYPE} )
public @interface Throws {
    ApplicationErrorCode errorCode() default ApplicationErrorCode.NONE;
}
