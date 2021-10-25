package org.acme.getting.started;

import javax.enterprise.context.RequestScoped;
import java.util.concurrent.atomic.AtomicInteger;

@RequestScoped
public class MyBean {

    static AtomicInteger idgen = new AtomicInteger();

    int id = idgen.incrementAndGet();

    public int getId() {
        return id;
    }
}
