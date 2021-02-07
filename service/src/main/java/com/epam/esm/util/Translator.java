package com.epam.esm.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@AllArgsConstructor
@Slf4j
public class Translator {
    private ResourceBundleMessageSource messageSource;

    public String toLocale(String msgCode) {
        Locale locale = LocaleContextHolder.getLocale();
        log.debug("Locale language: {}", locale.getLanguage());
        return messageSource.getMessage(msgCode, null, locale);
    }

    public String toLocale(String msgCode, Object ...msgArgs) {
        Locale locale = LocaleContextHolder.getLocale();
        log.debug("Locale language: {}", locale.getLanguage());
        log.debug("Arguments passed to the message of the translator service: {}", msgArgs);
        return messageSource.getMessage(msgCode, msgArgs, locale);
    }
}
