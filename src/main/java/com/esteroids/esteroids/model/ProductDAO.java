package com.esteroids.esteroids.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class ProductDAO {

    @Autowired
    DataSource dataSource;

    JdbcTemplate jdbc;

    @PostConstruct
    private void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }

    // INSERT
    public void inserirProduto(Product product) {
        String sql = "INSERT INTO products(name,description,price,stock,image_url,category,active) VALUES(?,?,?,?,?,?,?)";
        Object[] obj = new Object[7];
        obj[0] = product.getName();
        obj[1] = product.getDescription();
        obj[2] = product.getPrice();
        obj[3] = product.getStock();
        obj[4] = product.getImageUrl();
        obj[5] = product.getCategory().name();
        obj[6] = product.isActive();
        jdbc.update(sql,obj);
    }

    // RESEARCH
    public Product obterProduto(int id){
        String sql = "SELECT * FROM products WHERE id = ?";
        return Product.converterRegistro((Map<String,Object>) jdbc.queryForMap(sql, id));
    }

    public List<Product> buscarProdutos(String search){
        String sql = "SELECT * FROM products " + "WHERE LOWER(name) LIKE LOWER(?)";
        List<Map<String,Object>> registros = jdbc.queryForList(sql, "%" + search + "%");
        List<Product> lista = new ArrayList<>();

        for(Map<String,Object> registro : registros){
            lista.add(Product.converterRegistro(registro));
        }

        return lista;
    }

    public List<String> obterCategorias(){
        String sql = "SELECT DISTINCT category " + "FROM products " + "WHERE category IS NOT NULL";
        return jdbc.queryForList(sql, String.class);
    }

    public List<Product> obterProdutosPorCategoria(Category category, int produtoAtualId){
        String sql = "SELECT * FROM products WHERE category = ? AND id != ? LIMIT 4";
        List<Map<String,Object>> registros = jdbc.queryForList(sql, category.toString(), produtoAtualId);
        List<Product> lista = new ArrayList<>();
        for(Map<String,Object> registro : registros){
            lista.add(Product.converterRegistro(registro));
        }
        return lista;
    }

    public List<Product> filtrarProdutos(String search,String category,Boolean inStock){
        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE active = true");
        List<Object> params = new ArrayList<>();

        if(search != null && !search.isBlank()){
            sql.append(" AND LOWER(name) LIKE LOWER(?)");
            params.add("%" + search + "%");
        }

        if(category != null && !category.isBlank()){
            sql.append(" AND category = ?");
            params.add(category);
        }

        if(inStock != null && inStock){
            sql.append(" AND stock > 0");
        }

        List<Map<String,Object>> registros =
            jdbc.queryForList(
                sql.toString(),
                params.toArray()
            );

        List<Product> lista = new ArrayList<>();

        for(Map<String,Object> registro : registros){
            lista.add(
                Product.converterRegistro(registro)
            );
        }

        return lista;
    }

    // UPDATE
    public void atualizarProduto(int id, Product novo){
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock = ? WHERE id = ?";
        Object[] obj = new Object[5];
        obj[0] = novo.getName();
        obj[1] = novo.getDescription();
        obj[2] = novo.getPrice();
        obj[3] = novo.getStock();
        obj[4] = id;
        jdbc.update(sql, obj);
    }

    // DELETE
    public void deletarProduto(int id){
        String sql = "DELETE FROM products WHERE id = ?";
        jdbc.update(sql, id);
    }

    // SELECT ALL
    public List<Product> obterTodosProdutos() {        
        String sql = "SELECT * FROM products";
        List<Map<String,Object>> registros = jdbc.queryForList(sql);
        ArrayList<Product> lista = new ArrayList<>();
        for(Map<String,Object> registro : registros){
            lista.add(Product.converterRegistro(registro));
        }
        return lista;
    }
}