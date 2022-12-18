package org.example.spring.boot.plugin;

import org.example.spring.boot.plugin.model.Model1;
import org.example.spring.boot.plugin.model.Model2;
import org.example.spring.boot.plugin.model.Model3;
import org.example.spring.boot.plugin.service.ExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
//@EnablePluginRegistries(AbstractLogicRegistry.class)
public class SBPluginDemoApp implements CommandLineRunner {
    private static Logger logger = LoggerFactory.getLogger(SBPluginDemoApp.class);
    @Autowired
    private ExecutionService executionService;

    public static void main(String[] args) {
        logger.info("STARTING THE APPLICATION");
        SpringApplication.run(SBPluginDemoApp.class, args);
        logger.info("APPLICATION EXITED");
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Spring boot plugin demo started");
        Model1 model1 = new Model1("Model1", "1");
        executionService.serve(model1);


        Model2 model2 = new Model2("Model2", "2");
        executionService.serve(model2);


        Model3 model3 = new Model3("Model3", "3");
        executionService.serve(model3);

        logger.info("Spring boot plugin demo ended");
    }
}
