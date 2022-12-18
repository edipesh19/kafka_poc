package org.example.spring.boot.plugin.logic;

import org.example.spring.boot.plugin.model.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Logic3 implements BaseLogic {

    private static Logger logger = LoggerFactory.getLogger(Logic3.class);
    @Override
    public void execute(BaseModel model) {
        logger.info("Executing {} for model {}", this.getClass().getName(), model.getName());
    }
}
