package org.example.spring.boot.plugin.service;

import org.example.spring.boot.plugin.SBPluginDemoApp;
import org.example.spring.boot.plugin.logic.BaseLogic;
import org.example.spring.boot.plugin.model.BaseModel;
import org.example.spring.boot.plugin.registry.AbstractLogicRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@EnablePluginRegistries(AbstractLogicRegistry.class)
public class ExecutionService {

    private static Logger logger = LoggerFactory.getLogger(ExecutionService.class);

    //@Qualifier("abstractLogicRegistryRegistry")
    private PluginRegistry<AbstractLogicRegistry, BaseModel> plugins;

    public ExecutionService(PluginRegistry<AbstractLogicRegistry, BaseModel> plugins) {
        this.plugins = plugins;
    }

    public void serve(BaseModel model) {
        Optional<AbstractLogicRegistry> l1 = plugins.getPluginFor(model);
        if(l1.isPresent()) {
            BaseLogic logic1 = l1.get().returnLogic();
            logic1.execute(model);
        } else {
            logger.warn("No logic registry found");
        }
    }
}
