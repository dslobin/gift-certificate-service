package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
public class AcceptLanguageLocaleResolver extends AcceptHeaderLocaleResolver {
    private List<Locale> LOCALES = Arrays.asList(
            new Locale("en"),
            new Locale("ru")
    );

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String headerLang = request.getHeader("Accept-Language");
        return headerLang == null || headerLang.isEmpty()
                ? Locale.getDefault()
                : Locale.lookup(Locale.LanguageRange.parse(headerLang), LOCALES);
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
