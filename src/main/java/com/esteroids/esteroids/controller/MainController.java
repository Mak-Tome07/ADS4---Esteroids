package com.esteroids.esteroids.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.esteroids.esteroids.model.CartItem;
import com.esteroids.esteroids.model.Order;
import com.esteroids.esteroids.model.OrderItem;
import com.esteroids.esteroids.model.OrderService;
import com.esteroids.esteroids.model.OrderStatus;
import com.esteroids.esteroids.model.OrderView;
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

    @Autowired
    private OrderService os;

    @GetMapping("/")
    public String index(Model model){
        List<Product> produtos = ps.obterTodosProdutos().stream().filter(Product::isActive).limit(3).toList();
        model.addAttribute("produtosPopulares", produtos);

        return "pages/landing-page";
    }

    // ======================
    // CONTROLLERS DO PRODUTO :D     
    // ======================

    @GetMapping("/produto")
    public String formProduto(Model model){
        model.addAttribute("product", new Product());
        return "pages/product-form";
    }

    @PostMapping("/produto")
    public String cadastrarProduto(@ModelAttribute Product product) {     
        // Se o usuário não preencher nenhuma URL, você pode definir uma imagem padrão (opcional)
        if (product.getImageUrl() == null || product.getImageUrl().isBlank()) {
            product.setImageUrl("/uploads/default-product.png"); // Imagem padrão que está na sua pasta static
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
    public String atualizarProduto(@PathVariable int id, @ModelAttribute Product product) {
        Product produtoBanco = ps.obterProduto(id);
        if (produtoBanco == null) {
            return "redirect:/listar/produtos";
        }
        
        // Se no formulário de edição o campo de URL vier vazio, 
        // mantemos a URL da imagem que já estava salva no banco
        if (product.getImageUrl() == null || product.getImageUrl().isBlank()) {
            product.setImageUrl(produtoBanco.getImageUrl());
        }
        
        ps.atualizarProduto(id, product);
        return "redirect:/listar/produtos";
    }

    @GetMapping("/deletar/produto/{id}")
    public String deletarProduto(@PathVariable int id, RedirectAttributes redirectAttributes){
        try{
            ps.deletarProduto(id);
            redirectAttributes.addFlashAttribute("successMessage","Produto removido com sucesso.");
        }catch(DataIntegrityViolationException e){
            redirectAttributes.addFlashAttribute("errorMessage","Não é possível excluir este produto porque ele já faz parte de um pedido.");
        }
        return "redirect:/listar/produtos";
    }

    @GetMapping("/listar/produtos")
    public String listarProdutos(@RequestParam(required = false) String busca, HttpSession session, Model model){
        User usuario = (User) session.getAttribute("usuarioLogado");
        if(usuario == null){
            return "redirect:/auth";
        }
        if(usuario.getRole() != Role.ADMIN){
            return "redirect:/";
        }
        List<Product> lista;
        if(busca != null && !busca.isBlank()){
            lista = ps.buscarProdutos(busca);
        }else{
            lista = ps.obterTodosProdutos();
        }
        model.addAttribute("products", lista);
        model.addAttribute("busca", busca);
        return "pages/products";
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session, Model model){
        User usuario = (User) session.getAttribute("usuarioLogado");
        if(usuario == null){
            return "redirect:/auth";
        }
        if(usuario.getRole() != Role.ADMIN){
            return "redirect:/";
        }
        model.addAttribute("totalProdutos",ps.obterTodosProdutos().size());
        model.addAttribute("totalPedidos",os.obterTodosPedidos().size());
        model.addAttribute("totalUsuarios",us.obterTodosUsuarios().size());
        model.addAttribute("semEstoque",ps.contarProdutosSemEstoque());
        model.addAttribute("receitaTotal",os.calcularReceitaTotal());
        return "pages/dashboard";
    }

    @GetMapping("/listar/pedidos")
    public String listarPedidos(@RequestParam(required = false) String busca, @RequestParam(required = false) OrderStatus status, HttpSession session, Model model){
        User usuario = (User) session.getAttribute("usuarioLogado");
        List<OrderView> pedidos = os.obterPedidosAdmin();
        if(usuario == null){
            return "redirect:/auth";
        }
        if(usuario.getRole() != Role.ADMIN){
            return "redirect:/";
        }
        if(busca != null && !busca.isBlank()){
            pedidos = pedidos.stream().filter(p -> String.valueOf(p.getId()).contains(busca)).toList();
        }
        if(status != null){
            pedidos = pedidos.stream().filter(p -> p.getStatus() == status).toList();
        }
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("busca", busca);
        model.addAttribute("statusSelecionado", status);
        return "pages/pedidos";
    }

    // ======================
    // CONTROLLERS DO USUARIO
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
    public String listarUsuarios(@RequestParam(required = false) String busca, HttpSession session, Model model){
        User usuario = (User) session.getAttribute("usuarioLogado");
        if(usuario == null){
            return "redirect:/auth";
        }
        if(usuario.getRole() != Role.ADMIN){
            return "redirect:/";
        }
        List<User> lista;
        if(busca != null && !busca.isBlank()){
            lista = us.buscarUsuarios(busca);
        }else{
            lista = us.obterTodosUsuarios();
        }
        model.addAttribute("users", lista);
        model.addAttribute("busca", busca);
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
    public String shop(@RequestParam(required = false) String search, @RequestParam(required = false) String category, @RequestParam(required = false) Boolean inStock, Model model){
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
        List<Product> relacionados = ps.obterProdutosPorCategoria(produto.getCategory(),produto.getId());
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

    @PostMapping("/cart/increase/{id}")
    public String aumentarQuantidade(@PathVariable int id, HttpSession session){
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if(cart != null){
            for(CartItem item : cart){
                if(item.getProduct().getId() == id){
                    if(item.getQuantity() < item.getProduct().getStock()){
                        item.setQuantity(item.getQuantity() + 1);
                    }
                    break;
                }
            }
        }
    
        return "redirect:/cart";
    }

    @PostMapping("/cart/decrease/{id}")
    public String diminuirQuantidade(@PathVariable int id, HttpSession session){
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if(cart != null){
            cart.removeIf(item -> {
                if(item.getProduct().getId() == id){
                    item.setQuantity(item.getQuantity() - 1);
                    return item.getQuantity() <= 0;
                }
                return false;
            });
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
        User usuario = (User) session.getAttribute("usuarioLogado");
        if(usuario == null){
            return "redirect:/auth";
        }
        List<CartItem> cart =
            (List<CartItem>) session.getAttribute("cart");

        if(cart == null || cart.isEmpty()){
            return "redirect:/cart";
        }
        for(CartItem item : cart){
            Product produto = ps.obterProduto(item.getProduct().getId());
            if(produto.getStock() < item.getQuantity()){
                return "redirect:/cart";
            }
        }
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> itensPedido = new ArrayList<>();
        for(CartItem cartItem : cart){
            total = total.add(cartItem.getSubtotal());
            OrderItem item = new OrderItem();
            item.setProductId(cartItem.getProduct().getId());
            item.setQuantity(cartItem.getQuantity());
            item.setUnitPrice(cartItem.getProduct().getPrice());
            itensPedido.add(item);
        }
        Order pedido = new Order();
        pedido.setUserId(usuario.getId());
        pedido.setCreatedAt(LocalDateTime.now());
        pedido.setTotal(total);
        pedido.setStatus(OrderStatus.PENDENTE);
        os.criarPedido(pedido,itensPedido);
        for(OrderItem item : itensPedido){
            ps.baixarEstoque(
                item.getProductId(),
                item.getQuantity()
            );
        }
        session.removeAttribute("cart");
        return "redirect:/sucesso";
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

        if(user.getRole() == Role.ADMIN){
            session.setAttribute("usuarioLogado", user);
            return "redirect:/admin/dashboard";
        }

        session.setAttribute("usuarioLogado", user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    // =======================
    // CONTROLLERS DOS PEDIDOS
    // =======================

    @GetMapping("/meus-pedidos")
    public String meusPedidos(HttpSession session, Model model){
        User usuario = (User) session.getAttribute("usuarioLogado");
        if(usuario == null){
            return "redirect:/auth";
        }
        model.addAttribute("pedidos",os.obterPedidosPorUsuario(usuario.getId()));
        return "pages/meus-pedidos";
    }

    @GetMapping("/pedido/{id}")
    public String detalhesPedido(@PathVariable int id, HttpSession session, Model model){
        User usuario = (User) session.getAttribute("usuarioLogado");
        
        if(usuario == null){
            return "redirect:/auth";
        }
        Order pedido = os.obterPedido(id);
        if(pedido == null){
            return "redirect:/meus-pedidos";
        }
        if(pedido.getUserId() != usuario.getId()){
            return "redirect:/meus-pedidos";
        }
        List<OrderItem> itens = os.obterItensPorPedido(id);
        List<Product> produtos = new ArrayList<>();
        for(OrderItem item : itens){
            produtos.add(
                ps.obterProduto(item.getProductId())
            );
        }
        model.addAttribute("pedido", pedido);
        model.addAttribute("itens", itens);
        model.addAttribute("produtos", produtos);
        return "pages/pedido-detalhes";
    }

    @GetMapping("/admin/pedido/{id}")
    public String adminDetalhesPedido(@PathVariable int id, HttpSession session, Model model){
        User usuario = (User) session.getAttribute("usuarioLogado");
        if(usuario == null){
            return "redirect:/auth";
        }
        if(usuario.getRole() != Role.ADMIN){
            return "redirect:/";
        }
        Order pedido = os.obterPedido(id);
        if(pedido == null){
            return "redirect:/listar/pedidos";
        }
        List<OrderItem> itens = os.obterItensPorPedido(id);
        List<Product> produtos = new ArrayList<>();
        for(OrderItem item : itens){
            produtos.add(
                ps.obterProduto(
                    item.getProductId()
                )
            );
        }
        User cliente = us.obterUsuario(pedido.getUserId());
        model.addAttribute("pedido", pedido);
        model.addAttribute("cliente", cliente);
        model.addAttribute("itens", itens);
        model.addAttribute("produtos", produtos);
        return "pages/admin-pedido-detalhes";
    }

    @PostMapping("/admin/pedido/{id}/status")
    public String atualizarStatusPedido(@PathVariable int id, @RequestParam OrderStatus status, HttpSession session){
        User usuario = (User) session.getAttribute("usuarioLogado");
        if(usuario == null){
            return "redirect:/auth";
        }
        if(usuario.getRole() != Role.ADMIN){
            return "redirect:/";
        }
        os.atualizarStatus(id, status);
        return "redirect:/admin/pedido/" + id;
    }

    @GetMapping("/sucesso")
    public String sucesso(){
        return "pages/sucesso";
    }
}