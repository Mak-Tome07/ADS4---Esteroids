package com.esteroids.esteroids.model;

import java.math.BigDecimal;
import java.util.Map;

public class Product {

    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String imageUrl;
    private Category category;
    private boolean active;

    public Product(){}

    // INSERT
    public Product(String name, String description, BigDecimal price, int stock, String imageUrl, Category category, boolean active) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.category = category;
        this.active = active;
    }

    // SELECT
    public Product(int id, String name, String description, BigDecimal price, int stock, String imageUrl, Category category, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.category = category;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isActive() {
        return active;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static Product converterRegistro(Map<String,Object> registro) {
        int id = (int) registro.get("id");
        String name = (String) registro.get("name");
        String description = (String) registro.get("description");
        BigDecimal price = (BigDecimal) registro.get("price");
        int stock = (int) registro.get("stock");
        String imageUrl = (String) registro.get("image_url");
        Category category = Category.valueOf(((String) registro.get("category")).toUpperCase());
        boolean active = (boolean) registro.get("active");

        return new Product(id,name,description,price,stock,imageUrl,category,active);
    }
}