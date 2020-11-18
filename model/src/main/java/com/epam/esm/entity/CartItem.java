package com.epam.esm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
public class CartItem implements Serializable {
    @EmbeddedId
    private CartItemId id;

    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Cart cart;

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

    public BigDecimal calculateCost() {
        BigDecimal itemsCount = BigDecimal.valueOf(quantity);
        return giftCertificate.getPrice()
                .multiply(itemsCount);
    }

    public void setCart(Cart cart) {
        this.cart = cart;
        id.setCartId(cart.getId());
    }

    public void setProduct(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
        id.setGiftCertificateId(giftCertificate.getId());
    }
}
