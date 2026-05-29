package com.esteroids.esteroids.model;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    ProductDAO pdao;

    public void inserirProduto(Product product){
        pdao.inserirProduto(product);
    }

    public List<Product> obterTodosProdutos(){        
        return pdao.obterTodosProdutos();
    }

    public Product obterProduto(int id){
        return pdao.obterProduto(id);
    }
    
    public void atualizarProduto(int id, Product novo){
        pdao.atualizarProduto(id, novo);
    }
    
    public void deletarProduto(int id){
        pdao.deletarProduto(id);
    }

    public List<Product> obterProdutosPorCategoria(Category category, int produtoAtualId){
        return pdao.obterProdutosPorCategoria(category, produtoAtualId);
    }
}