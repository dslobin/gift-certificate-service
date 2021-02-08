package com.epam.esm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cart_items")
@NoArgsConstructor
@Data
public class CartItem implements Serializable {
    @EmbeddedId
    private CartItemId id;

    @MapsId("cartId")
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Cart cart;

    @MapsId("giftCertificateId")
    @JoinColumn(name = "certificate_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private GiftCertificate giftCertificate;

    @Column(name = "quantity")
    private int quantity;

    public CartItem(Cart cart, GiftCertificate giftCertificate, int quantity) {
        this.id = new CartItemId(cart.getId(), giftCertificate.getId());
        this.cart = cart;
        this.giftCertificate = giftCertificate;
        this.quantity = quantity;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
        this.id.setCartId(cart.getId());
    }

    public void setProduct(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
        this.id.setGiftCertificateId(giftCertificate.getId());
    }
}
