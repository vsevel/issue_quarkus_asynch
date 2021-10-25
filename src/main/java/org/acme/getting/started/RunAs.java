package org.acme.getting.started;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@InterceptorBinding
public @interface RunAs {
    @Nonbinding String username();

    @Nonbinding String[] roles();

}