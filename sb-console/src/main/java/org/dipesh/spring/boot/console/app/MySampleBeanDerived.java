package org.dipesh.spring.boot.console.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;

@Component
public class MySampleBeanDerived {

    private static Logger LOG = LoggerFactory
        .getLogger(MySampleBeanBase.class);

    private MySampleBeanBase mySampleBeanBase;

    @Autowired
    public MySampleBeanDerived(MySampleBeanBase mySampleBeanBase) {
        this.mySampleBeanBase = mySampleBeanBase;
        LOG.info("CTOR -> MySampleBeanDerived");
    }

    @PostConstruct
    void init() {
        LOG.info("POST CONSTRUCT -> MySampleBeanDerived");
    }

    public void test() {
        System.out.println(" Hello " + mySampleBeanBase.getNameFactory().getName());
    }
}
