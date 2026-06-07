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
        String sql = "INSERT INTO produto(nome,descricao,preco,estoque,imagem_url,categoria,ativo) VALUES(?,?,?,?,?,?,?)";
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
        String sql = "SELECT * FROM produto WHERE id = ?";
    
        try{
            return Product.converterRegistro(
                (Map<String,Object>) jdbc.queryForMap(sql, id)
            );
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Product> buscarProdutos(String search){
        String sql = "SELECT * FROM produto " + "WHERE LOWER(nome) LIKE LOWER(?)";
        List<Map<String,Object>> registros = jdbc.queryForList(sql, "%" + search + "%");
        List<Product> lista = new ArrayList<>();

        for(Map<String,Object> registro : registros){
            lista.add(Product.converterRegistro(registro));
        }

        return lista;
    }

    public List<String> obterCategorias(){
        String sql = "SELECT DISTINCT categoria " + "FROM produto " + "WHERE categoria IS NOT NULL";
        return jdbc.queryForList(sql, String.class);
    }

    public List<Product> obterProdutosPorCategoria(Category category, int produtoAtualId){
        String sql = "SELECT * FROM produto WHERE categoria = ? AND id != ? AND ativo = true LIMIT 4";
        List<Map<String,Object>> registros = jdbc.queryForList(sql, category.toString(), produtoAtualId);
        List<Product> lista = new ArrayList<>();
        for(Map<String,Object> registro : registros){
            lista.add(Product.converterRegistro(registro));
        }
        return lista;
    }

    public List<Product> filtrarProdutos(String search,String category,Boolean inStock){
        StringBuilder sql = new StringBuilder("SELECT * FROM produto WHERE ativo = true");
        List<Object> params = new ArrayList<>();

        if(search != null && !search.isBlank()){
            sql.append(" AND LOWER(nome) LIKE LOWER(?)");
            params.add("%" + search + "%");
        }

        if(category != null && !category.isBlank()){
            sql.append(" AND categoria = ?");
            params.add(category);
        }

        if(inStock != null && inStock){
            sql.append(" AND estoque > 0");
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
        String sql = "UPDATE produto SET nome = ?, descricao = ?, preco = ?, estoque = ?, imagem_url = ?, categoria = ?, ativo = ? WHERE id = ?";

        Object[] obj = new Object[8];

        obj[0] = novo.getName();
        obj[1] = novo.getDescription();
        obj[2] = novo.getPrice();
        obj[3] = novo.getStock();
        obj[4] = novo.getImageUrl();
        obj[5] = novo.getCategory().name();
        obj[6] = novo.isActive();
        obj[7] = id;

        jdbc.update(sql, obj);
    }
    // DELETE
    public void deletarProduto(int id){
        String sql = "DELETE FROM produto WHERE id = ?";
        jdbc.update(sql, id);
    }

    // SELECT ALL
    public List<Product> obterTodosProdutos() {        
        String sql = "SELECT * FROM produto";
        List<Map<String,Object>> registros = jdbc.queryForList(sql);
        ArrayList<Product> lista = new ArrayList<>();
        for(Map<String,Object> registro : registros){
            lista.add(Product.converterRegistro(registro));
        }
        return lista;
    }

    public void baixarEstoque(int produtoId, int quantidade){
        String sql = "UPDATE produto SET estoque = estoque - ? WHERE id = ?";
        jdbc.update(sql, quantidade, produtoId);
    }

    public int obterEstoque(int produtoId){
        String sql = "SELECT estoque FROM produto WHERE id = ?";
        return jdbc.queryForObject(sql,Integer.class,produtoId);
    }
}