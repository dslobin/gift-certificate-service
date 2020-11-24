package com.epam.esm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "carts")
@Data
@Slf4j
public class Cart implements Serializable {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "foreign", parameters = @Parameter(name = "property", value = "user"))
    private long id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne
    @PrimaryKeyJoinColumn
    private User user;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Transient
    private BigDecimal itemsCost;

    public Cart() {
        this(null);
    }

    public Cart(User user) {
        this.user = user;
        this.itemsCost = calculateItemsCost();
    }

    public void clear() {
        this.itemsCost = BigDecimal.ZERO;
        this.items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getItemsCount() {
        return items.size();
    }

    public BigDecimal getItemsCost() {
        this.itemsCost = calculateItemsCost();
        return itemsCost;
    }

    public void setItems(Set<CartItem> cartItems) {
        this.items = cartItems;
        itemsCost = calculateItemsCost();
    }

    public void update(GiftCertificate certificate, int newQuantity) {
        if (certificate == null) {
            return;
        }

        if (newQuantity > 0) {
            CartItem existedItem = findItemById(certificate.getId());
            if (existedItem == null) {
                log.debug("The certificate(id = {}) has been added to the cart", certificate.getId());
                items.add(new CartItem(this, certificate, newQuantity));
            } else {
                log.debug("The number of certificates(id = {}) has been renewed. New quantity: {}", certificate.getId(), newQuantity);
                existedItem.setQuantity(newQuantity);
            }
        } else {
            log.debug("The certificate(id = {}) has been removed from the cart", certificate.getId());
            removeItem(certificate.getId());
        }

        itemsCost = calculateItemsCost();
        log.debug("Cart items cost = {}", itemsCost);
        log.debug("Cart items quantity = {}", items.size());
    }

    private BigDecimal calculateItemsCost() {
        return items.stream()
                .map(CartItem::calculateCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private CartItem findItemById(long certificateId) {
        return items.stream()
                .filter(cartItem -> cartItem.getGiftCertificate().getId() == certificateId)
                .findFirst()
                .orElse(null);
    }

    private void removeItem(long productId) {
        this.items.removeIf(item -> item.getGiftCertificate().getId() == productId);
    }
}
