package com.epam.esm.service.config;

import com.epam.esm.dao.*;
import com.epam.esm.mapper.*;
import com.epam.esm.mapper.impl.*;
import com.epam.esm.service.*;
import com.epam.esm.service.impl.*;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceContextTest {

    /**
     * DAO Beans
     */

    @Bean
    public TagDao tagDao() {
        return Mockito.mock(TagDao.class);
    }

    @Bean
    public GiftCertificateDao giftCertificateDao() {
        return Mockito.mock(GiftCertificateDao.class);
    }

    @Bean
    public UserDao userDao() {
        return Mockito.mock(UserDao.class);
    }

    @Bean
    public OrderDao orderDao() {
        return Mockito.mock(OrderDao.class);
    }

    @Bean
    public CartDao cartDao() {
        return Mockito.mock(CartDao.class);
    }

    /**
     * Mapper Beans
     */

    @Bean
    public TagMapper tagMapper() {
        return new TagMapperImpl();
    }

    @Bean
    public GiftCertificateMapper giftCertificateMapper() {
        return new GiftCertificateMapperImpl();
    }

    @Bean
    public OrderMapper orderMapper() {
        return new OrderMapperImpl();
    }

    @Bean
    public UserMapper userMapper() {
        return new UserMapperImpl();
    }

    @Bean
    public CartMapper cartMapper() {
        return new CartMapperImpl();
    }

    /**
     * Service Beans
     */

    @Bean
    public CartService cartService(
            CartDao cartDao,
            CartMapper cartMapper,
            UserDao userDao,
            GiftCertificateDao certificateDao
    ) {
        return new CartServiceImpl(cartDao, cartMapper, userDao, certificateDao);
    }

    @Bean
    public OrderService orderService(
            OrderDao orderDao,
            OrderMapper orderMapper,
            UserDao userDao,
            CartDao cartDao
    ) {
        return new OrderServiceImpl(orderDao, orderMapper, userDao, cartDao);
    }

    @Bean
    public UserService userService(
            UserDao userDao,
            UserMapper userMapper,
            OrderDao orderDao
    ) {
        return new UserServiceImpl(userDao, userMapper, orderDao);
    }

    @Bean
    public GiftCertificateService giftCertificateService(
            GiftCertificateDao giftCertificateDao,
            TagDao tagDao,
            GiftCertificateMapper giftCertificateMapper
    ) {
        return new GiftCertificateServiceImpl(giftCertificateDao, tagDao, giftCertificateMapper);
    }

    @Bean
    public TagService tagService(
            TagDao tagDao,
            TagMapper tagMapper
    ) {
        return new TagServiceImpl(tagDao, tagMapper);
    }
}
