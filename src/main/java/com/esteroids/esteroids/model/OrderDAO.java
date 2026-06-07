package com.esteroids.esteroids.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class OrderDAO {

    @Autowired
    DataSource dataSource;

    JdbcTemplate jdbc;

    @PostConstruct
    private void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }

    public int inserirPedido(Order pedido){
        String sql = "INSERT INTO pedido(usuario_id,data_pedido,valor_total,status) VALUES(?,?,?,?) RETURNING id";
        Object[] obj = new Object[4];
        obj[0] = pedido.getUserId();
        obj[1] = pedido.getCreatedAt();
        obj[2] = pedido.getTotal();
        obj[3] = pedido.getStatus().name();

        return jdbc.queryForObject(sql, Integer.class, obj);
    }

    public List<Order> obterPedidosPorUsuario(int usuarioId){
        String sql = "SELECT * FROM pedido WHERE usuario_id = ? ORDER BY data_pedido DESC";
        List<Map<String,Object>> registros = jdbc.queryForList(sql, usuarioId);
        List<Order> pedidos = new ArrayList<>();
        for(Map<String,Object> registro : registros){
            pedidos.add(Order.converterRegistro(registro));
        }
        return pedidos;
    }
    
    public List<Order> obterTodosPedidos(){
        String sql = "SELECT * FROM pedido ORDER BY data_pedido DESC";
        List<Map<String,Object>> registros = jdbc.queryForList(sql);
        List<Order> pedidos = new ArrayList<>();
        for(Map<String,Object> registro : registros){
            pedidos.add(Order.converterRegistro(registro));
        }
        return pedidos;
    }

    public Order obterPedido(int id){
        String sql = "SELECT * FROM pedido WHERE id = ?";
        try{
            return Order.converterRegistro(
                jdbc.queryForMap(sql, id)
            );
        }
        catch(Exception e){
            return null;
        }
    }

    public List<OrderView> obterPedidosAdmin(){
        String sql = "SELECT p.id, p.data_pedido, p.valor_total, p.status, u.nome_usuario FROM pedido p INNER JOIN usuario u ON p.usuario_id = u.id ORDER BY p.data_pedido DESC";
        List<Map<String,Object>> registros = jdbc.queryForList(sql);
        List<OrderView> pedidos = new ArrayList<>();
        for(Map<String,Object> r : registros){ 
            pedidos.add(new OrderView((int) r.get("id"),
                    (String) r.get("nome_usuario"),
                    ((java.sql.Timestamp)r.get("data_pedido")).toLocalDateTime(),
                    (BigDecimal) r.get("valor_total"),
                    OrderStatus.valueOf((String) r.get("status"))
                )
            );
        }
        return pedidos;
    }

    public void atualizarStatus(int pedidoId, OrderStatus status){
        String sql = "UPDATE pedido SET status = ? WHERE id = ?";
        jdbc.update(sql,status.name(),pedidoId);
    }
}