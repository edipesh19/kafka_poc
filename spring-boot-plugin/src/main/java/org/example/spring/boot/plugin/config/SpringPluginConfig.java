package org.example.spring.boot.plugin.config;

import org.example.spring.boot.plugin.registry.AbstractLogicRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.plugin.core.config.EnablePluginRegistries;

@Configuration
@EnablePluginRegistries(AbstractLogicRegistry.class)
public class SpringPluginConfig {
}
