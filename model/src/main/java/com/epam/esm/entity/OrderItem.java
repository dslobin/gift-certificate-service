package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItem implements Serializable {
    @EmbeddedId
    private OrderItemId id = new OrderItemId();

    @MapsId("giftCertificateId")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate_id", referencedColumnName = "id")
    private GiftCertificate giftCertificate;

    @ToString.Exclude
    @MapsId("orderId")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @Column(name = "quantity")
    private int quantity;

    public void setOrder(Order order) {
        this.order = order;
        this.id.setOrderId(order.getId());
    }

    public void setGiftCertificate(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
        this.id.setGiftCertificateId(giftCertificate.getId());
    }
}
