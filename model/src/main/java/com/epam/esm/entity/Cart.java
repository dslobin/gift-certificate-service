package com.epam.esm.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@EqualsAndHashCode(exclude = "itemsCost")
@ToString(exclude = "itemsCost")
public class Cart implements Serializable {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "foreign", parameters = @Parameter(name = "property", value = "user"))
    private long id;

    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<CartItem> items = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Transient
    private BigDecimal itemsCost;

    public Cart() {
        this(null);
    }

    public Cart(User user) {
        this.user = user;
        itemsCost = calculateItemsCost();
    }

    public void clear() {
        itemsCost = BigDecimal.ZERO;
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getItemsCount() {
        return items.size();
    }

    public BigDecimal getItemsCost() {
        return itemsCost;
    }

    public void setItems(List<CartItem> cartItems) {
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
                items.add(new CartItem(this, certificate, newQuantity));
            } else {
                existedItem.setQuantity(newQuantity);
            }
        } else {
            removeItem(certificate.getId());
        }

        itemsCost = calculateItemsCost();
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
        items.removeIf(item -> item.getGiftCertificate().getId() == productId);
    }
}
