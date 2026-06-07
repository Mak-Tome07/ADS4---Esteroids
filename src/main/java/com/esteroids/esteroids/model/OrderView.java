package com.esteroids.esteroids.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderView {

    private int id;
    private String customerName;
    private LocalDateTime createdAt;
    private BigDecimal total;
    private OrderStatus status;

    public OrderView(
        int id,
        String customerName,
        LocalDateTime createdAt,
        BigDecimal total,
        OrderStatus status
    ){
        this.id = id;
        this.customerName = customerName;
        this.createdAt = createdAt;
        this.total = total;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public OrderStatus getStatus() {
        return status;
    }
}