package com.esteroids.esteroids.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.esteroids.esteroids.model.CartItem;
import com.esteroids.esteroids.model.User;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("cartQuantity")
    public int cartQuantity(HttpSession session){

        List<CartItem> cart =
            (List<CartItem>) session.getAttribute("cart");

        if(cart == null){
            return 0;
        }

        int total = 0;

        for(CartItem item : cart){
            total += item.getQuantity();
        }

        return total;
    }

    @ModelAttribute
    public void adicionarUsuario(Model model, HttpSession session){
        User usuario = (User) session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", usuario);
    }
}