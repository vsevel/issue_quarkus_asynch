package org.acme.getting.started;

import io.quarkus.security.identity.CurrentIdentityAssociation;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.Set;

@Interceptor
@RunAs(username = "", roles = {})
class RunAsInterceptor {

    @Inject
    CurrentIdentityAssociation association;

    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception {
        var existing = association.getIdentity();
        try {
            RunAs runAs = context.getMethod().getAnnotation(RunAs.class);
            var identity = QuarkusSecurityIdentity.builder().setPrincipal(new QuarkusPrincipal(runAs.username()))
                    .addRoles(Set.of(runAs.roles()))
                    .build();
            association.setIdentity(identity);
            return context.proceed();
        } finally {
            // association.setIdentity(existing);
        }
    }
}
