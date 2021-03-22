package org.dipesh.spring.boot.console.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Component
public class MySampleBeanBase {
    private NameFactory nameFactory;
    private static Logger LOG = LoggerFactory
        .getLogger(MySampleBeanBase.class);

    public MySampleBeanBase() {
        LOG.info("CTOR -> MySampleBeanBase");
        nameFactory = new NameFactory();
    }

    @PostConstruct
    void init(){
        LOG.info("POST CONSTRUCT -> MySampleBeanBase");
        nameFactory.setName("Dipesh");
    }

    public NameFactory getNameFactory() {
        return nameFactory;
    }
}
