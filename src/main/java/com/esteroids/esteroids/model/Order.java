package com.esteroids.esteroids.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

public class Order {

    private int id;
    private int userId;
    private LocalDateTime createdAt;
    private BigDecimal total;
    private OrderStatus status;

    public Order(){
        
    }

    public Order(int id, int userId, LocalDateTime createdAt, BigDecimal total, OrderStatus status) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.total = total;
        this.status = status;
        
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public static Order converterRegistro(Map<String,Object> registro){
        int id = (int) registro.get("id");
        int userId = (int) registro.get("usuario_id");
        LocalDateTime createdAt = ((Timestamp) registro.get("data_pedido")).toLocalDateTime();
        BigDecimal total = (BigDecimal) registro.get("valor_total");
        OrderStatus status = OrderStatus.valueOf(((String) registro.get("status")).toUpperCase());
        return new Order(id, userId, createdAt, total, status);
    }
}
