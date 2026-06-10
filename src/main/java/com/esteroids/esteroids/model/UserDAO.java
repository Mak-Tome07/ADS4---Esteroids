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
public class UserDAO {

     @Autowired
	DataSource dataSource;
	
	JdbcTemplate jdbc;
	
	@PostConstruct
	private void initialize() {
		jdbc = new JdbcTemplate(dataSource);
	}

    // CREATE
    public void inserirUsuario(User usuario){
        String sql = "INSERT INTO usuario(nome_usuario,email,senha,role,ativo) VALUES(?,?,?,?,?)";
    
        Object[] obj = new Object[5];

        obj[0] = usuario.getUsername();
        obj[1] = usuario.getEmail();
        obj[2] = usuario.getPassword();
        obj[3] = usuario.getRole().name();
        obj[4] = usuario.isActive();
        jdbc.update(sql,obj);
    }

    // RESEARCH
    public User obterUsuario(int id){
        String sql = "SELECT * FROM usuario WHERE id = ?";
    
        try{
            return User.converterRegistros(
                (Map<String,Object>) jdbc.queryForMap(sql,id)
            );
        }catch(Exception e){
            return null;
        }
    }

    // UPDATE
    public void atualizarUsuario(int id, User novo){
        String sql = "UPDATE usuario SET nome_usuario = ?, email = ?, senha = ? where id = ?";
        Object[] obj = new Object[4];
        obj[0] = novo.getUsername();
        obj[1] = novo.getEmail();
        obj[2] = novo.getPassword();
        obj[3] = id;
        jdbc.update(sql,obj);
    }

    // DELETE
    public void deletarUsuario(int id){
        String sql = "DELETE FROM usuario WHERE id = ?";
        jdbc.update(sql,id);
    }

    public List<User> obterTodosUsuarios(){
        String sql = "SELECT * FROM usuario";
        List<Map<String,Object>> listaRegistros = jdbc.queryForList(sql);
        ArrayList<User> aux = new ArrayList<>();
        for(Map<String,Object> registro : listaRegistros){
            aux.add(User.converterRegistros(registro));
        }
        return aux;
    }  

    public User buscarPorEmail(String email){
        String sql = "SELECT * FROM usuario WHERE email = ?";

        try{
            return User.converterRegistros(
                (Map<String,Object>) jdbc.queryForMap(sql, email)
            );
        }catch(Exception e){
            return null;
        }
    }

    public List<User> buscarUsuarios(String busca){
        String sql = "SELECT * FROM usuario WHERE LOWER(nome_usuario) LIKE LOWER(?) OR LOWER(email) LIKE LOWER(?)";
        List<Map<String,Object>> registros = jdbc.queryForList(sql,"%" + busca + "%","%" + busca + "%");
        List<User> lista = new ArrayList<>();
        for(Map<String,Object> registro : registros){
            lista.add(User.converterRegistros(registro));
        }
        return lista;
    }
}
