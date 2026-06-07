package com.esteroids.esteroids.model;

import java.util.List;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderDAO odao;

    @Autowired
    private OrderItemDAO oidao;

    @Autowired
    private ProductDAO pdao;

    @Transactional
    public int criarPedido(Order pedido, List<OrderItem> itens){
        for(OrderItem item : itens){
            int estoque = pdao.obterEstoque(item.getProductId());
            if(estoque < item.getQuantity()){
                throw new RuntimeException("Estoque insuficiente");
            }
        }
        int orderId = odao.inserirPedido(pedido);
        for(OrderItem item : itens){
            item.setOrderId(orderId);
            oidao.inserirItem(item);
            pdao.baixarEstoque(item.getProductId(),item.getQuantity());
        }

        return orderId;
    }

    public List<Order> obterPedidosPorUsuario(int usuarioId){
        return odao.obterPedidosPorUsuario(usuarioId);
    }
    
    public List<Order> obterTodosPedidos(){
        return odao.obterTodosPedidos();
    }

    public Order obterPedido(int id){
        return odao.obterPedido(id);
    }

    public List<OrderItem> obterItensPorPedido(int pedidoId){
        return oidao.obterItensPorPedido(pedidoId);
    }

    public List<OrderView> obterPedidosAdmin(){
        return odao.obterPedidosAdmin();
    }

    public void atualizarStatus(int pedidoId, OrderStatus status){
        odao.atualizarStatus(pedidoId,status);
    }

    public BigDecimal calcularReceitaTotal(){
        BigDecimal total = BigDecimal.ZERO;
        for(Order pedido : odao.obterTodosPedidos()){
            total = total.add(
                pedido.getTotal()
            );
        }
        return total;
    }
}