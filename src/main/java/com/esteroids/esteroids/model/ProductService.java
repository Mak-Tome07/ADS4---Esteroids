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

    public List<Product> buscarProdutos(String search){
        return pdao.buscarProdutos(search);
    }

    public List<String> obterCategorias(){
        return pdao.obterCategorias();
    }

    public List<Product> filtrarProdutos(String search,String category,Boolean inStock){
        return pdao.filtrarProdutos(search,category,inStock);
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