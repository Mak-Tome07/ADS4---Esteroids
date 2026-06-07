package com.esteroids.esteroids.model;

import java.util.Map;

public class User {

    private int id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private boolean active;

    public User() {
        this.role = Role.CLIENTE;
        this.active = true;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = Role.CLIENTE;
        this.active = true;
    }

    public User(int id, String username, String email, String password, Role role, boolean active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static User converterRegistros(Map<String,Object> registros){
        int id = (int) registros.get("id");
        String username = (String) registros.get("nome_usuario");
        String email = (String) registros.get("email");
        String password = (String) registros.get("senha");
        Role role = Role.valueOf((String) registros.get("role"));
        Boolean active = (Boolean) registros.get("ativo");
        return new User(id,username,email,password,role,active != null ? active : false);
    }
}