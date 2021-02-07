package com.epam.esm.service.config;

import com.epam.esm.config.ServiceEnvConfig;
import com.epam.esm.repository.*;
import com.epam.esm.service.*;
import com.epam.esm.service.impl.*;
import com.epam.esm.specification.GiftCertificateSpecification;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Import({ServiceEnvConfig.class})
public class ServiceContextTest {
    @Autowired
    private PasswordEncoder encoder;

    /**
     * Specification
     */

    @Bean
    public GiftCertificateSpecification giftCertificateSpecification() {
        return new GiftCertificateSpecification();
    }

    /**
     * Repositories
     */

    @Bean
    public RoleRepository roleRepository() {
        return Mockito.mock(RoleRepository.class);
    }

    @Bean
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    public TagRepository tagRepository() {
        return Mockito.mock(TagRepository.class);
    }

    @Bean
    public CartRepository cartRepository() {
        return Mockito.mock(CartRepository.class);
    }

    @Bean
    public OrderRepository orderRepository() {
        return Mockito.mock(OrderRepository.class);
    }

    @Bean
    public GiftCertificateRepository giftCertificateRepository() {
        return Mockito.mock(GiftCertificateRepository.class);
    }

    /**
     * Services
     */

    @Bean
    public TagService tagService(TagRepository tagRepository) {
        return new TagServiceImpl(tagRepository);
    }

    @Bean
    public RoleService roleService(RoleRepository roleRepository) {
        return new RoleServiceImpl(roleRepository);
    }

    @Bean
    public UserService userService(
            UserRepository userRepository,
            RoleService roleService,
            OrderRepository orderRepository
    ) {
        return new UserServiceImpl(userRepository, roleService, orderRepository, encoder);
    }

    @Bean
    public GiftCertificateService giftCertificateService(
            GiftCertificateRepository certificateRepository,
            TagService tagService,
            GiftCertificateSpecification specification
    ) {
        return new GiftCertificateServiceImpl(certificateRepository, tagService, specification);
    }

    @Bean
    public CartService cartService(
            CartRepository cartRepository,
            UserService userService,
            GiftCertificateService certificateService
    ) {
        return new CartServiceImpl(cartRepository, userService, certificateService);
    }

    @Bean
    public OrderService orderService(
            OrderRepository orderRepository,
            UserService userService,
            CartService cartService
    ) {
        return new OrderServiceImpl(orderRepository, userService, cartService);
    }
}
