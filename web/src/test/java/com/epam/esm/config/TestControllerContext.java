package com.epam.esm.config;

import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestControllerContext {

    @Bean
    public TagService tagService() {
        return Mockito.mock(TagService.class);
    }

    @Bean
    public GiftCertificateService certificateService() {
        return Mockito.mock(GiftCertificateService.class);
    }
}
