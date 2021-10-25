package org.acme.getting.started;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusPrincipal;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    static Logger log = Logger.getLogger(GreetingResource.class);

    @Inject
    MyBean myBean;

    @Inject
    Work work;

    @Inject
    SecurityIdentity securityIdentity;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @RunAs(username = "vince", roles = {"user"})
    public String hello() {
        log.info("Processing request " + getInfo());

        for (int i = 0; i < 10; i++) {
            work.doWork();
        }
        log.info("start sleeping 5 secs " + getInfo());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("returning from jaxrs endpoint " + getInfo());
        return "Hello RESTEasy";
    }

    String getSecurityIdentity() {
        return securityIdentity.isAnonymous()
                ? "anonymous"
                : securityIdentity.getPrincipal().getName();
    }

    String getInfo() {
        return " user=" + getSecurityIdentity() + " bean=" + myBean.getId();
    }

}