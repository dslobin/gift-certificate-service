package com.epam.esm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * {@link AcceptHeaderLocaleResolver} extension that simply uses the primary locale
 * specified in the "accept-language" header of the HTTP request
 */
@Configuration
@Slf4j
public class AcceptLanguageHeaderResolver extends AcceptHeaderLocaleResolver {
    private final List<Locale> LOCALES = Arrays.asList(
            new Locale("en"),
            new Locale("ru")
    );

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String headerLang = request.getHeader("Accept-Language");
        return StringUtils.isEmpty(headerLang)
                ? Locale.getDefault()
                : Locale.lookup(Locale.LanguageRange.parse(headerLang), LOCALES);
    }
}
