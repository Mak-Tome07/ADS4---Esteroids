package com.esteroids.esteroids.controller;

import java.util.List;
import java.util.ArrayList;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.esteroids.esteroids.model.Product;
import com.esteroids.esteroids.model.ProductService;
import com.esteroids.esteroids.model.User;
import com.esteroids.esteroids.model.UserService;
import com.esteroids.esteroids.model.CartItem;

@Controller
public class MainController {

    @Autowired
    ApplicationContext context;

    @GetMapping("/")
    public String index(Model model){
        ProductService ps = context.getBean(ProductService.class);
        List<Product> produtos = ps.obterTodosProdutos().stream().limit(3).toList();
        model.addAttribute("produtosPopulares", produtos);

        return "pages/landing-page";
    }

    // ======================
    // CONTROLLERS DO PRODUTO      
    // ======================

    @GetMapping("/produto")
    public String formProduto(Model model){
        model.addAttribute("product",new Product());
        return "product-form";
    }

    @PostMapping("/produto")
    public String cadastrarProduto(@ModelAttribute Product product) {
        ProductService ps = context.getBean(ProductService.class);
        ps.inserirProduto(product);
        return "redirect:/listar/produtos";
    }
    
    @GetMapping("/produto/{id}/atualizar")
    public String formAtualizarProduto(Model model, @PathVariable int id){
        ProductService ps = context.getBean(ProductService.class);
        Product produtoAntigo = ps.obterProduto(id);
        model.addAttribute("id", id);
        model.addAttribute("product", produtoAntigo);
        return "product-update";
    }

    @PostMapping("/produto/{id}/atualizar")
    public String atualizarProduto(@PathVariable int id, @ModelAttribute Product product){
        ProductService ps = context.getBean(ProductService.class);
        ps.atualizarProduto(id, product);
        return "redirect:/listar/produtos";
    }

    @GetMapping("/produto/{id}/deletar")
    public String deletarProduto(@PathVariable int id){
        ProductService ps = context.getBean(ProductService.class);
        ps.deletarProduto(id);
        return "redirect:/listar/produtos";
    }

    @GetMapping("/listar/produtos")
    public String listarProdutos(Model model){
        ProductService ps = context.getBean(ProductService.class);
        List<Product> lista = ps.obterTodosProdutos();
        model.addAttribute("products",lista);
        return "products";
    }

    // ======================
    // CONTROLLERS DO USUARIO
    // ======================

    @GetMapping("/usuario")
    public String formUsuario(Model model){
        model.addAttribute("user",new User());
        return "user-form";
    }

    @GetMapping("/usuario/{id}/atualizar")
    public String formupdUsuario(Model model, @PathVariable int id){
        model.addAttribute("id",id);
        UserService us = context.getBean(UserService.class);
        User usuarioAntigo = us.obterUsuario(id);
        model.addAttribute("user",usuarioAntigo);
        return "user-update";
    }

    @PostMapping("/usuario/{id}/atualizar")
    public String atualizarUsuario(Model model, 
                                  @PathVariable int id,
                                  @ModelAttribute User user){
        UserService us = context.getBean(UserService.class);
        us.atualizarUsuario(id, user);
        return "redirect:/listar/usuarios";
    }

    @PostMapping("/usuario")
    public String formUsuario(@ModelAttribute User user, Model model){
        UserService us = context.getBean(UserService.class);
		us.inserirUsuario(user);
		return "sucesso";
    }

    @GetMapping("/listar/usuarios")
    public String listarUsuarios(Model model){
        UserService us = context.getBean(UserService.class);
        List<User> lista = us.obterTodosUsuarios();
        model.addAttribute("users", lista);
        return "users";
    }

    @GetMapping("/usuario/{id}/deletar")
    public String deletarUsuario(@PathVariable int id){
        UserService us = context.getBean(UserService.class);
        us.deletarUsuario(id);
        return "redirect:/listar/usuarios";
    }

    // =================== 
    // CONTROLLERS DO SHOP
    // ===================
    
    @GetMapping("/shop")
    public String shop(@RequestParam(required = false) String search,@RequestParam(required = false) String category,@RequestParam(required = false) Boolean inStock,Model model){
        ProductService ps = context.getBean(ProductService.class);
        List<Product> produtos = ps.filtrarProdutos(search, category, inStock);
        model.addAttribute("products", produtos);
        model.addAttribute("categorias",ps.obterCategorias());
        return "pages/shop";
    }

    @GetMapping("/produto/{id}")
    public String productDetails(@PathVariable int id, Model model){
        ProductService ps = context.getBean(ProductService.class);
        Product produto = ps.obterProduto(id);
        List<Product> relacionados = ps.obterProdutosPorCategoria(produto.getCategory(),produto.getId());
        model.addAttribute("produto", produto);
        model.addAttribute("relacionados", relacionados);
        return "pages/product-details";
    }

    // =======================
    // CONTROLLERS DO CARRINHO
    // =======================

    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable int id, @RequestParam int quantity, HttpSession session){
        ProductService ps = context.getBean(ProductService.class);
        Product produto = ps.obterProduto(id);
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if(cart == null){
            cart = new ArrayList<>();
        }
    
        boolean encontrado = false;
    
        for(CartItem item : cart){
            if(item.getProduct().getId() == id){
                item.increaseQuantity();
                encontrado = true;
                break;
            }
        }
    
        if(!encontrado){
            cart.add(new CartItem(produto, quantity));
        }
    
        session.setAttribute("cart", cart);

        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String cart(HttpSession session, Model model){
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if(cart == null){
            cart = new ArrayList<>();
        }

        double total = 0;

        for(CartItem item : cart){
            total += item.getSubtotal();
        }

        model.addAttribute("cart", cart);
        model.addAttribute("total", total);

        return "pages/cart";
    }

    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable int id, HttpSession session){
    
        List<CartItem> cart =
            (List<CartItem>) session.getAttribute("cart");
    
        if(cart != null){
            cart.removeIf(item ->
                item.getProduct().getId() == id
            );
        }
    
        session.setAttribute("cart", cart);
    
        return "redirect:/cart";
    }

    // =======================
    // CONTROLLERS DO CHECKOUT
    // =======================

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model){
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if(cart == null){
            cart = new ArrayList<>();
        }

        double total = 0;

        for(CartItem item : cart){
            total += item.getSubtotal();
        }

        model.addAttribute("cart", cart);
        model.addAttribute("total", total);

        return "pages/checkout";
    }

    @PostMapping("/checkout")
    public String finalizarCompra(HttpSession session){
        session.removeAttribute("cart");
        return "pages/success";
    }
}
