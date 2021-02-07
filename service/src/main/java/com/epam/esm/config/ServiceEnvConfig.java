package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ServiceEnvConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource resourceBundle = new ResourceBundleMessageSource();
        resourceBundle.setBasename("messages");
        resourceBundle.setDefaultEncoding("UTF-8");
        resourceBundle.setUseCodeAsDefaultMessage(true);
        return resourceBundle;
    }
}
