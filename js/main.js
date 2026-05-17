// =====================================================
// ESTEROIDS - MAIN JS
// Inicialização geral do projeto
// =====================================================

document.addEventListener("DOMContentLoaded", () => {
  // =========================
  // GLOBAL (todas as páginas)
  // =========================
  initNavbarScroll();
  initMobileMenu();
  initCurrentYear();

  // =========================
  // PÁGINAS ESPECÍFICAS
  // =========================
  initLandingPage();
  initLoginPage();
  initAdminSidebar();
});


// =====================================================
// GLOBAL UI BEHAVIOR
// (componentes reutilizados em várias páginas)
// =====================================================

/**
 * Adiciona efeito de scroll na navbar
 */
function initNavbarScroll() {
  const navbar = document.querySelector(".navbar");
  if (!navbar) return;

  window.addEventListener("scroll", () => {
    navbar.classList.toggle("navbar-scrolled", window.scrollY > 20);
  });
}


/**
 * Controle do menu mobile (hamburguer)
 */
function initMobileMenu() {
  const menuToggle = document.getElementById("menuToggle");
  const mobileMenu = document.getElementById("mobileMenu");

  if (!menuToggle || !mobileMenu) return;

  menuToggle.addEventListener("click", () => {
    mobileMenu.classList.toggle("active");
  });
}


/**
 * Atualiza ano automaticamente no footer
 */
function initCurrentYear() {
  const el = document.getElementById("currentYear");
  if (!el) return;

  el.textContent = new Date().getFullYear();
}


// =====================================================
// LANDING PAGE
// =====================================================

/**
 * Popup de login ao tentar acessar carrinho bloqueado
 */
function initLandingPage() {
  const cartBtn = document.getElementById("lockedCartBtn");
  const popup = document.getElementById("loginPopup");
  const closePopup = document.getElementById("closePopup");

  if (cartBtn && popup && closePopup) {
    cartBtn.addEventListener("click", () => {
      popup.classList.add("show-popup");
    });

    closePopup.addEventListener("click", () => {
      popup.classList.remove("show-popup");
    });
  }
}


// =====================================================
// LOGIN PAGE
// =====================================================

/**
 * Alterna entre Login e Signup na mesma tela
 */
function initLoginPage() {
  const loginForm = document.getElementById("login-form");
  const signupForm = document.getElementById("signup-form");
  const toggleBtn = document.getElementById("toggle-btn");
  const toggleText = document.getElementById("toggle-text");
  const formTitle = document.getElementById("form-title");

  if (!toggleBtn) return;

  let isLogin = true;

  toggleBtn.addEventListener("click", () => {
    isLogin = !isLogin;

    loginForm.classList.toggle("hidden");
    signupForm.classList.toggle("hidden");

    formTitle.innerText = isLogin ? "Login" : "Criar Conta";
    toggleText.innerText = isLogin
      ? "Não possui conta?"
      : "Já possui conta?";
    toggleBtn.innerText = isLogin ? "Criar conta" : "Entrar";
  });
}


// =====================================================
// ADMIN PANEL
// =====================================================

/**
 * Sidebar do painel admin (menu lateral + overlay)
 */
function initAdminSidebar() {
  const toggle = document.getElementById("adminMenuToggle");
  const sidebar = document.querySelector(".sidebar");
  const overlay = document.getElementById("sidebarOverlay");

  if (!toggle || !sidebar || !overlay) return;

  toggle.addEventListener("click", () => {
    sidebar.classList.toggle("active");
    overlay.classList.toggle("active");
  });

  overlay.addEventListener("click", () => {
    sidebar.classList.remove("active");
    overlay.classList.remove("active");
  });
}