package org.acme.getting.started;

import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class WorkA implements Work {

    static Logger log = Logger.getLogger(WorkA.class);

    static AtomicInteger idgen = new AtomicInteger();

    @Inject
    MyBean myBean;

    @Inject
    SecurityIdentity securityIdentity;

    @Override
    public void doWork() {
        log.info("Do work " + getInfo(null));

        Uni.createFrom().item(this::nextId)
                .emitOn(Infrastructure.getDefaultWorkerPool())
                .subscribe().with(this::worker, Throwable::printStackTrace);
    }

    int nextId() {
        return idgen.incrementAndGet();
    }

    private Uni<Void> worker(int uuid) {
        long sleep = (long) (Math.random() * 10000);
        log.info("Starting work: sleep=" + sleep + " " + getInfo(uuid));
        try {

            Thread.sleep(sleep);
        } catch (InterruptedException ex) {
            log.info("Could not finish work: " + getInfo(uuid));
            throw new RuntimeException(ex);
        }
        log.info("Finish work: " + getInfo(uuid));
        return Uni.createFrom().voidItem();
    }

    String getSecurityIdentity() {
        return securityIdentity.isAnonymous()
                ? "anonymous"
                : securityIdentity.getPrincipal().getName();
    }

    String getInfo(Integer id) {
        return
                (id == null ? "" : "id=" + id + " ")
                        + "user=" + getSecurityIdentity()
                        + " bean=" + getMyBeanId();
    }

    String getMyBeanId() {
        return myBean.getId() + "";
    }
}

