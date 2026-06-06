package com.esteroids.esteroids.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDAO udao;

    public void inserirUsuario(User usuario){
        udao.inserirUsuario(usuario);
    }

    public User obterUsuario(int id){
        return udao.obterUsuario(id);
    }

    public List<User> obterTodosUsuarios(){
        return udao.obterTodosUsuarios();
    }

    public void atualizarUsuario(int id, User novo){
        udao.atualizarUsuario(id, novo);
    }

    public void deletarUsuario(int id){
        udao.deletarUsuario(id);
    }

    public User buscarPorEmail(String email){
        return udao.buscarPorEmail(email);
    }
}