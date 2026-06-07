package com.esteroids.esteroids.model;

import java.math.BigDecimal;
import java.util.Map;

public class OrderItem {

    private int id;
    private int orderId;
    private int productId;

    private int quantity;
    private BigDecimal unitPrice;

    public OrderItem() {
        
    }

    public OrderItem(int id, int orderId, int productId, int quantity, BigDecimal unitPrice) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public static OrderItem converterRegistro(Map<String,Object> registro){
        int id = (int) registro.get("id");
        int orderId = (int) registro.get("pedido_id");
        int productId = (int) registro.get("produto_id");
        int quantity = (int) registro.get("quantidade");
        BigDecimal unitPrice = (BigDecimal) registro.get("preco_unitario");
        return new OrderItem(id,orderId,productId,quantity,unitPrice);
    }
}
