package com.esteroids.esteroids.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.esteroids.esteroids.model.CartItem;
import com.esteroids.esteroids.model.Product;
import com.esteroids.esteroids.model.ProductService;
import com.esteroids.esteroids.model.Role;
import com.esteroids.esteroids.model.User;
import com.esteroids.esteroids.model.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    private ProductService ps;
    
    @Autowired
    private UserService us;

    @GetMapping("/")
    public String index(Model model){
        List<Product> produtos = ps.obterTodosProdutos().stream().filter(Product::isActive).limit(3).toList();
        model.addAttribute("produtosPopulares", produtos);

        return "pages/landing-page";
    }

    // ======================
    // CONTROLLERS DO PRODUTO      
    // ======================

    @GetMapping("/produto")
    public String formProduto(Model model){
        model.addAttribute("product", new Product());
        return "pages/product-form";
    }

    @PostMapping("/produto")
    public String cadastrarProduto(@ModelAttribute Product product, @RequestParam("imagemArquivo") MultipartFile file) {          
        if (!file.isEmpty()) {
            try {
                String pastaUpload = System.getProperty("user.dir") + "/uploads/";
                File dir = new File(pastaUpload);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String nomeOriginal = file.getOriginalFilename();
                String nomeFinal = System.currentTimeMillis() + "_" + nomeOriginal;

                Path caminhoCompleto = Paths.get(pastaUpload + nomeFinal);
                Files.write(caminhoCompleto, file.getBytes());

                product.setImageUrl("/uploads/" + nomeFinal);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ps.inserirProduto(product); 
        return "redirect:/listar/produtos";
    }
    
    @GetMapping("/produto/{id}/atualizar")
    public String formAtualizarProduto(Model model, @PathVariable int id){

        Product produtoAntigo = ps.obterProduto(id);

        if(produtoAntigo == null){
            return "redirect:/listar/produtos";
        }

        model.addAttribute("id", id);
        model.addAttribute("product", produtoAntigo);

        return "pages/product-update";
    }

    @PostMapping("/produto/{id}/atualizar")
    public String atualizarProduto(@PathVariable int id, @ModelAttribute Product product, @RequestParam(value = "imagemArquivo", required = false) MultipartFile file){

        Product produtoBanco = ps.obterProduto(id);

        if(produtoBanco == null){
            return "redirect:/listar/produtos";
        }
        
        if (file != null && !file.isEmpty()) {
            try {
                String pastaUpload = System.getProperty("user.dir") + "/uploads/";
                File dir = new File(pastaUpload);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String nomeOriginal = file.getOriginalFilename();
                String nomeFinal = System.currentTimeMillis() + "_" + nomeOriginal;

                Path caminhoCompleto = Paths.get(pastaUpload + nomeFinal);
                Files.write(caminhoCompleto, file.getBytes());

                product.setImageUrl("/uploads/" + nomeFinal);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            product.setImageUrl(produtoBanco.getImageUrl());
        }
        
        ps.atualizarProduto(id, product);
        return "redirect:/listar/produtos";
    }

    @PostMapping("/produto/{id}/deletar")
    public String deletarProduto(@PathVariable int id){
        ps.deletarProduto(id);
        return "redirect:/listar/produtos";
    }

    @GetMapping("/listar/produtos")
    public String listarProdutos(HttpSession session, Model model){
        User usuario = (User) session.getAttribute("usuarioLogado");

        if(usuario == null){
            return "redirect:/auth";
        }

        if(usuario.getRole() != Role.ADMIN){
            return "redirect:/";
        }
        
        List<Product> lista = ps.obterTodosProdutos();
        model.addAttribute("products", lista);
        return "pages/products";
    }

    // Adicione isto no MainController para abrir o Dashboard
    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session){
        User usuario = (User) session.getAttribute("usuarioLogado");

        if(usuario == null){
            return "redirect:/auth";
        }

        if(usuario.getRole() != Role.ADMIN){
            return "redirect:/";
        }

        return "pages/dashboard";
    }

    // Adicione isto no MainController para abrir os Pedidos
    @GetMapping("/listar/pedidos")
    public String listarPedidos(HttpSession session){
        User usuario = (User) session.getAttribute("usuarioLogado");

        if(usuario == null){
            return "redirect:/auth";
        }

        if(usuario.getRole() != Role.ADMIN){
            return "redirect:/";
        }

        return "pages/pedidos"; // Certifique-se de que o HTML de Pedidos se chama "pedidos.html"
    }

    // ======================
    // CONTROLLERS DO USUARIO (LIMPOS SEM GETBEAN)
    // ======================

    @GetMapping("/usuario")
    public String formUsuario(Model model){
        model.addAttribute("user", new User());
        return "pages/user-form";
    }

    @GetMapping("/usuario/{id}/atualizar")
    public String formupdUsuario(Model model, @PathVariable int id){

        User usuarioAntigo = us.obterUsuario(id);

        if(usuarioAntigo == null){
            return "redirect:/listar/usuarios";
        }

        model.addAttribute("id", id);
        model.addAttribute("user", usuarioAntigo);

        return "pages/user-update";
    }

    @PostMapping("/usuario/{id}/atualizar")
    public String atualizarUsuario(@PathVariable int id, @ModelAttribute User user){

        User usuarioBanco = us.obterUsuario(id);

        if(usuarioBanco == null){
            return "redirect:/listar/usuarios";
        }

        us.atualizarUsuario(id, user);

        return "redirect:/listar/usuarios";
    }

    @PostMapping("/usuario")
    public String cadastrarUsuario(@ModelAttribute User user){
        us.inserirUsuario(user);
        return "redirect:/listar/usuarios";
    }

    @GetMapping("/listar/usuarios")
    public String listarUsuarios(HttpSession session, Model model){
        User usuario = (User) session.getAttribute("usuarioLogado");

        if(usuario == null){
            return "redirect:/auth";
        }

        if(usuario.getRole() != Role.ADMIN){
            return "redirect:/";
        }

        List<User> lista = us.obterTodosUsuarios();
        model.addAttribute("users", lista);
        return "pages/users";
    }

    @PostMapping("/usuario/{id}/deletar")
    public String deletarUsuario(@PathVariable int id){
        us.deletarUsuario(id);
        return "redirect:/listar/usuarios";
    }

    // =================== 
    // CONTROLLERS DO SHOP
    // ===================
    
    @GetMapping("/shop")
    public String shop(@RequestParam(required = false) String search,
                       @RequestParam(required = false) String category,
                       @RequestParam(required = false) Boolean inStock,
                       Model model){
        List<Product> produtos = ps.filtrarProdutos(search, category, inStock);
        model.addAttribute("products", produtos);
        model.addAttribute("categorias", ps.obterCategorias());
        return "pages/shop";
    }

    @GetMapping("/produto/{id}")
    public String productDetails(@PathVariable int id, Model model){
        Product produto = ps.obterProduto(id);

        if(produto == null){
            return "redirect:/shop";
        }
        
        List<Product> relacionados =
            ps.obterProdutosPorCategoria(
                produto.getCategory(),
                produto.getId()
            );

        model.addAttribute("produto", produto);
        model.addAttribute("relacionados", relacionados);

        return "pages/product-details";
    }

    // =======================
    // CONTROLLERS DO CARRINHO
    // =======================

    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable int id, @RequestParam(defaultValue = "1") int quantity, HttpSession session){
        User usuario = (User) session.getAttribute("usuarioLogado");

        if(usuario == null){
            return "redirect:/auth";
        }
        
        if(quantity <= 0){
            return "redirect:/shop";
        }

        Product produto = ps.obterProduto(id);

        if(produto == null){
            return "redirect:/shop";
        }

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if(cart == null){
            cart = new ArrayList<>();
        }
    
        boolean encontrado = false;
        for(CartItem item : cart){
            if(item.getProduct().getId() == id){
                item.setQuantity(item.getQuantity() + quantity);
                encontrado = true;
                break;
            }
        }
    
        if(!encontrado){
            cart.add(new CartItem(produto, quantity));
        }
    
        session.setAttribute("cart", cart);
        return "redirect:/shop?adicionado=true";
    }

    @GetMapping("/cart")
    public String cart(HttpSession session, Model model){
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        User usuario = (User) session.getAttribute("usuarioLogado");

        if(usuario == null){
            return "redirect:/auth";
        }

        if(cart == null){
            cart = new ArrayList<>();
        }

        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        for(CartItem item : cart){
            total = total.add(item.getSubtotal());
        }

        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        return "pages/cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable int id, HttpSession session){

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if(cart != null){
            cart.removeIf(item -> item.getProduct().getId() == id);
            session.setAttribute("cart", cart);
        }

        return "redirect:/cart";
    }

    // =======================
    // CONTROLLERS DO CHECKOUT
    // =======================

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model){
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        User usuario =
        (User) session.getAttribute("usuarioLogado");

        if(usuario == null){
            return "redirect:/auth";
        }

        if(cart == null){
            cart = new ArrayList<>();
        }

        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        for(CartItem item : cart){
            total = total.add(item.getSubtotal());
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

    // ===========================
    // CONTROLLERS DA AUTENTICAÇÃO
    // ===========================

    @GetMapping("/auth")
    public String auth(){
        return "pages/login";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username, @RequestParam String email, @RequestParam String password){

        User user = new User();

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        us.inserirUsuario(user);

        return "redirect:/auth";
    }

    @PostMapping("/auth")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session){
        User user = us.buscarPorEmail(email);

        if(user == null || !user.getPassword().equals(password)){
            return "redirect:/auth?erro=login";
        }

        session.setAttribute("usuarioLogado", user);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}