package com.esteroids.esteroids.model;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class OrderItemDAO {
    @Autowired
    DataSource dataSource;

    JdbcTemplate jdbc;

    @PostConstruct
    private void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }

    public void inserirItem(OrderItem item){
        String sql = "INSERT INTO pedido_item(pedido_id,produto_id,quantidade,preco_unitario) VALUES(?,?,?,?)";
        Object[] obj = new Object[4];
        obj[0] = item.getOrderId();
        obj[1] = item.getProductId();
        obj[2] = item.getQuantity();
        obj[3] = item.getUnitPrice();
        jdbc.update(sql,obj);
    }

    public List<OrderItem> obterItensPorPedido(int pedidoId){
        String sql = "SELECT * FROM pedido_item WHERE pedido_id = ?";
        List<Map<String,Object>> registros = jdbc.queryForList(sql, pedidoId);
        List<OrderItem> itens = new ArrayList<>();
        for(Map<String,Object> registro : registros){
            itens.add(OrderItem.converterRegistro(registro));
        }
        return itens;
    }
}