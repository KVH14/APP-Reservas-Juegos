// ════════════════════════════════════════════════════════════
//  app.js — JavaScript general de Playres
//  Secciones:
//    1. NAVBAR scroll
//    2. CATÁLOGO — filtros, galería, paginación
//    3. PRODUCT DETAIL — galería, días, tabs
//    4. ADMIN — tabla, modales, CRUD
// ════════════════════════════════════════════════════════════

// ── 0. Nota importante ───────────────────────────────────────
// Asegúrate de cargar en tu HTML los scripts en este orden:
// <script src="js/juegos.js"></script>
// <script src="js/api.js"></script>   // define ProductoAPI
// <script src="js/app.js"></script>
// Si no respetas el orden, app.js fallará porque ProductoAPI no existirá.

// ── 1. NAVBAR SCROLL ─────────────────────────────────────────
const navbar = document.querySelector(".navbar-playres");
if (navbar) {
  window.addEventListener("scroll", () => {
    navbar.classList.toggle("scrolled", window.scrollY > 30);
  });
}

// ════════════════════════════════════════════════════════════
//  2. CATÁLOGO
// (todo tu código de catálogo igual, sin cambios)
// ════════════════════════════════════════════════════════════
if (document.getElementById("gamesGrid")) {
  let filteredGames = [];
  let currentPage = 1;
  const perPage = 8;
  let activeGenre = "";
  let activePlatform = "";

  // Leer parámetros de URL para aplicar filtros
  const urlParams = new URLSearchParams(window.location.search);
  const genreParam = urlParams.get("genre");

  cargarJuegosAPI().then(() => {
    filteredGames = [...juegos];

    // Si hay un género en la URL, aplicarlo
    if (genreParam) {
      activeGenre = genreParam;
      // Actualizar UI para mostrar el filtro activo
      document.querySelectorAll(".genre-filter-tag").forEach((tag) => {
        if (tag.textContent.includes(genreParam)) {
          tag.classList.add("active");
        } else {
          tag.classList.remove("active");
        }
      });
    }

    filterGames();
  });

  // Filtrar juegos
  window.setFilter = function (type, value, el) {
    if (type === "genre") {
      activeGenre = value;
      document
        .querySelectorAll(".genre-filter-tag")
        .forEach((t) => t.classList.remove("active"));
    } else {
      activePlatform = value;
      document
        .querySelectorAll(".platform-filter-tag")
        .forEach((t) => t.classList.remove("active"));
    }
    if (el) el.classList.add("active");
    filterGames();
  };

  window.filterGames = function () {
    const q = document.getElementById("searchInput")
      ? document.getElementById("searchInput").value.toLowerCase()
      : "";
    filteredGames = juegos.filter(
      (g) =>
        (!q ||
          g.title.toLowerCase().includes(q) ||
          g.genre.toLowerCase().includes(q) ||
          g.platform.toLowerCase().includes(q)) &&
        (!activeGenre || g.genre === activeGenre) &&
        (!activePlatform || g.platform === activePlatform),
    );
    currentPage = 1;
    renderGames();
  };

  function renderGames() {
    const start = (currentPage - 1) * perPage;
    const slice = filteredGames.slice(start, start + perPage);
    const totalPages = Math.ceil(filteredGames.length / perPage);
    const grid = document.getElementById("gamesGrid");

    document.getElementById("resultCount").textContent =
      `Mostrando ${filteredGames.length} juego${filteredGames.length !== 1 ? "s" : ""}`;
    document.getElementById("pageIndicator").textContent =
      `Página ${currentPage} de ${totalPages || 1}`;

    grid.innerHTML =
      slice.length === 0
        ? `<div class="col-12 catalog-empty"><p>No se encontraron juegos con ese filtro.</p></div>`
        : slice
            .map(
              (g) => `
          <div class="col-lg-3 col-md-4 col-sm-6">
            <div class="game-card">
              <a href="product-detail.html" class="catalog-card-link">
                <div class="game-thumbnail ${g.bg}">
                  <div class="game-tags">
                    <span class="game-tag">${g.genre}</span>
                    ${g.tag ? `<span class="${g.tagClass}">+ ${g.tag}</span>` : ""}
                  </div>
                  ${g.emoji}
                </div>
              </a>
              <div class="game-card-info">
                <div class="game-platform">${g.platform}</div>
                <h3 class="game-title-catalog">${g.title}</h3>
                <div class="game-bottom">
                  <div class="game-price">$${g.price.toLocaleString("es-CO")}<span>/día</span></div>
                  <div><span class="game-stars">★★★★★</span><span class="game-rating">${g.rating}</span></div>
                </div>
                <a href="product-detail.html" class="catalog-btn-ver">Ver detalles →</a>
              </div>
            </div>
          </div>
        `,
            )
            .join("");

    // Paginación Bootstrap
    const pag = document.getElementById("pagination");
    pag.innerHTML = `
      <li class="page-item ${currentPage === 1 ? "disabled" : ""}">
        <a class="page-link" href="#catalogo" onclick="goPage(${currentPage - 1})">←</a>
      </li>`;

    for (let i = 1; i <= totalPages; i++) {
      pag.innerHTML += `
        <li class="page-item ${i === currentPage ? "active" : ""}">
          <a class="page-link" href="#catalogo" onclick="goPage(${i})">${i}</a>
        </li>`;
    }

    pag.innerHTML += `
      <li class="page-item ${currentPage >= totalPages ? "disabled" : ""}">
        <a class="page-link" href="#catalogo" onclick="goPage(${currentPage + 1})">→</a>
      </li>`;
  }

  window.goPage = function (n) {
    const totalPages = Math.ceil(filteredGames.length / perPage);
    if (n < 1 || n > totalPages) return;
    currentPage = n;
    renderGames();
  };

  // Iniciar catálogo
  renderGames();
}

// ════════════════════════════════════════════════════════════
//  3. PRODUCT DETAIL
// (todo tu código de product detail igual, sin cambios)
// ════════════════════════════════════════════════════════════

// ════════════════════════════════════════════════════════════
//  4. ADMIN PANEL (solo si existe #tableBody)
// ════════════════════════════════════════════════════════════
if (document.getElementById("tableBody")) {
  let products = [];
  let filteredProds = [];
  let currentPage = 1;
  const perPage = 8;
  let deleteIndex = -1;

  // 🔹 API para conectar con backend
  const ProductoAPI = {
    async listar() {
      const res = await fetch("http://localhost:8080/api/productos");
      return await res.json();
    },
    async crear(producto) {
      const res = await fetch("http://localhost:8080/api/productos", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(producto)
      });
      return await res.json();
    },
    async actualizar(id, producto) {
      const res = await fetch(`http://localhost:8080/api/productos/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(producto)
      });
      return await res.json();
    },
    async eliminar(id) {
      await fetch(`http://localhost:8080/api/productos/${id}`, { method: "DELETE" });
    }
  };

  // Cargar productos desde backend y normalizar campos para la UI
  async function cargarProductos() {
    try {
      const raw = await ProductoAPI.listar();
      products = (raw || []).map(p => ({
        id: p.id,
        title: p.titulo || p.title || `Producto ${p.id}`,
        platform: p.plataforma || p.platform || "PS5",
        genre: p.genero || p.genre || "Acción",
        price: p.precio || p.price || 0,
        rating: p.rating || 0,
        stock: p.stock || 0,
        status: (p.estado || p.status || 'disponible').toLowerCase(),
        emoji: p.emoji || '🎮',
        bg: p.bg || 'game-thumb-purple',
        categorias: p.categorias || [],
        // conserva el objeto original por si lo necesitas
        _raw: p
      }));
      filteredProds = [...products];
      renderTable();
    } catch (e) {
      showToast('danger', '❌', 'No se pudo conectar al backend: ' + e.message);
    }
  }

  function renderTable() {
    const start = (currentPage - 1) * perPage;
    const slice = filteredProds.slice(start, start + perPage);
    const totalPages = Math.ceil(filteredProds.length / perPage);

    document.getElementById("tableBody").innerHTML = slice
      .map((p, idx) => `
        <tr>
          <td>
            <div class="tbl-game-info">
              <div class="tbl-thumb">${p.emoji || ""}</div>
              <div>
                <div class="tbl-game-name">${p.titulo || p.title}</div>
                <div class="tbl-game-id">${p.id}</div>
              </div>
            </div>
          </td>
          <td><span class="platform-tbl">${p.platform}</span></td>
          <td>${p.genre}</td>
          <td class="tbl-price">$${p.price.toLocaleString("es-CO")}</td>
          <td><span class="tbl-stars">${"★".repeat(Math.floor(p.rating))}</span> <span class="tbl-rating-num">${p.rating}</span></td>
          <td><span class="status-badge ${p.status}">${p.status.charAt(0).toUpperCase() + p.status.slice(1)}</span></td>
          <td class="${p.stock === 0 ? "tbl-stock-out" : "tbl-stock"}">${p.stock}</td>
          <td>
            <div class="action-btns">
              <button class="btn-edit" onclick="openEditModal(${idx})">✏️ Editar</button>
              <button class="btn-delete" onclick="openDeleteModal(${idx})">🗑️</button>
            </div>
          </td>
        </tr>`).join("");

    // Stats
    document.getElementById("totalCount").textContent = products.length;
    document.getElementById("availCount").textContent = products.filter(p => (p.estado || p.status) === "disponible").length;
    document.getElementById("reservedCount").textContent = products.filter(p => (p.estado || p.status) === "reservado").length;
    document.getElementById("outCount").textContent = products.filter(p => (p.estado || p.status) === "agotado").length;
  }

  window.filterTable = function () {
    const q = document.getElementById("searchInput").value.toLowerCase();
    const plt = document.getElementById("platformFilter").value;
    const sts = document.getElementById("statusFilter").value;
    filteredProds = products.filter(
      (p) =>
        (!q ||
          p.title.toLowerCase().includes(q) ||
          p.id.toLowerCase().includes(q)) &&
        (!plt || p.platform === plt) &&
        (!sts || p.status === sts),
    );
    currentPage = 1;
    renderTable();
  };

  // Abrir modal para agregar (usa el modal existente en tu HTML)
  window.openAddModal = function () {
    document.getElementById("modalTitle").textContent = "➕ Agregar producto";
    document.getElementById("editIndex").value = -1;
    ["fTitle", "fPrice", "fStock", "fRating"].forEach(
      (id) => (document.getElementById(id).value = ""),
    );
    document.getElementById("fPlatform").value = "PS5";
    document.getElementById("fGenre").value = "RPG";
    document.getElementById("fStatus").value = "disponible";
    document.getElementById("fEmoji").value = "🌌";
    new bootstrap.Modal(document.getElementById("productModal")).show();
  };

  // Abrir modal para editar (carga datos frescos desde backend)
  window.openEditModal = async function (idx) {
    const p = products[idx];
    document.getElementById("modalTitle").textContent = "✏️ Editar producto";
    document.getElementById("editIndex").value = idx;
    document.getElementById("fTitle").value = p.title;
    document.getElementById("fPrice").value = p.price;
    document.getElementById("fStock").value = p.stock;
    document.getElementById("fRating").value = p.rating;
    document.getElementById("fPlatform").value = p.platform;
    document.getElementById("fGenre").value = p.genre;
    document.getElementById("fStatus").value = p.status;
    document.getElementById("fEmoji").value = p.emoji;
    new bootstrap.Modal(document.getElementById("productModal")).show();
  };

  window.saveProduct = function () {
    const idx = parseInt(document.getElementById("editIndex").value);
    const title = document.getElementById("fTitle").value.trim();
    const price = parseInt(document.getElementById("fPrice").value);
    if (!title || !price) {
      showToast("danger", "⚠️", "Completa título y precio.");
      return;
    }

    const bgMap = {
      PS5: "game-thumb-purple",
      Xbox: "game-thumb-red",
      Switch: "game-thumb-green",
      PC: "game-thumb-blue",
    };
    const platform = document.getElementById("fPlatform").value;

    const prod = {
      id:
        idx >= 0
          ? products[idx].id
          : "PLY-" + String(products.length + 1).padStart(3, "0"),
      title,
      platform,
      genre: document.getElementById("fGenre").value,
      price,
      rating: parseFloat(document.getElementById("fRating").value) || 4.5,
      stock:
        document.getElementById("fStock").value !== ""
          ? parseInt(document.getElementById("fStock").value)
          : idx >= 0
            ? products[idx].stock
            : 0,
      status: document.getElementById("fStatus").value,
      emoji: document.getElementById("fEmoji").value,
      bg: bgMap[platform] || "game-thumb-purple",
      tag: "",
      tagClass: "",
      description: "",
    };

    if (idx >= 0) {
      products[idx] = prod;
      showToast("success", "✅", "Producto actualizado.");
    } else {
      products.push(prod);
      showToast("success", "✅", "Producto agregado.");
    }

    filteredProds = [...products];
    filterTable();
    bootstrap.Modal.getInstance(document.getElementById("productModal")).hide();
  };

  // Abrir modal de confirmación de borrado
  window.openDeleteModal = function (idx) {
    deleteIndex = idx;
    document.getElementById("deleteGameName").textContent = '"' + (products[idx].titulo || products[idx].title) + '"';
    new bootstrap.Modal(document.getElementById("deleteModal")).show();
  };

  window.confirmDelete = function () {
    if (deleteIndex < 0) return;
    const name = products[deleteIndex].title;
    products.splice(deleteIndex, 1);
    deleteIndex = -1;
    filteredProds = [...products];
    filterTable();
    bootstrap.Modal.getInstance(document.getElementById("deleteModal")).hide();
    showToast("danger", "🗑️", `"${name}" eliminado.`);
  };

  let toastTimer;
  function showToast(type, icon, msg) {
    const el = document.getElementById("toastEl");
    if (!el) {
      console.warn("Toast element not found");
      alert(msg);
      return;
    }
    document.getElementById("toastIcon").textContent = icon;
    document.getElementById("toastMsg").textContent = msg;
    el.className = `playres-toast ${type} show`;
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => el.classList.remove("show"), 3000);
  }

  // Iniciar admin
  renderTable();
}

// ==========================================
// 1. REGISTRO DE USUARIO
// ==========================================
const registerForm = document.getElementById("registerForm");
if (registerForm) {
  registerForm.addEventListener("submit", function (e) {
    e.preventDefault();

    // Creamos el objeto con los datos del formulario
    const nuevoUsuario = {
      nombre: document.getElementById("regNombre").value.trim(),
      apellido: document.getElementById("regApellido").value.trim(),
      email: document.getElementById("regEmail").value.trim(),
      password: document.getElementById("regPassword").value.trim(),
    };

    // Guardamos en el "disco duro" del navegador
    localStorage.setItem("usuarioRegistrado", JSON.stringify(nuevoUsuario));

    // Mostrar mensaje de éxito y limpiar
    document.getElementById("successMessage").classList.remove("d-none");
    registerForm.reset();

    setTimeout(() => {
      const modal = bootstrap.Modal.getInstance(
        document.getElementById("registerModal"),
      );
      modal.hide();
      document.getElementById("successMessage").classList.add("d-none");
    }, 2000);
  });
}

// ==========================================
// 2. LOGIN DE USUARIO
// ==========================================
const loginForm = document.getElementById("loginForm");
if (loginForm) {
  loginForm.addEventListener("submit", function (e) {
    e.preventDefault();

    const emailInput = document.getElementById("loginEmail").value.trim();
    const passInput = document.getElementById("loginPassword").value.trim();
    const errorMsg = document.getElementById("loginError");

    // Traemos al usuario que se registró antes
    const usuarioGuardado = JSON.parse(
      localStorage.getItem("usuarioRegistrado"),
    );

    if (
      usuarioGuardado &&
      emailInput === usuarioGuardado.email &&
      passInput === usuarioGuardado.password
    ) {
      localStorage.setItem("usuarioActivo", JSON.stringify(usuarioGuardado));

      // Cerramos el modal
      const modal = bootstrap.Modal.getInstance(
        document.getElementById("loginModal"),
      );
      modal.hide();
      loginForm.reset();
      errorMsg.classList.add("d-none");

      // ¡IMPORTANTE! se llama a la funcion para cambiar la interfaz
      actualizarInterfaz();
    } else {
      // ERROR
      errorMsg.textContent = "Datos incorrectos o usuario no registrado.";
      errorMsg.classList.remove("d-none");
    }
  });
}

// ==========================================
// 3. CAMBIO DE LOS BOTONES POR EL PERFIL
// ==========================================
function actualizarInterfaz() {
  const sesion = JSON.parse(localStorage.getItem("usuarioActivo"));

  const botonesAuth = document.getElementById("navAuthButtons");
  const perfilUsuario = document.getElementById("navUserProfile");
  const nombreTxt = document.getElementById("userNameDisplay");
  const avatarCirculo = document.getElementById("userAvatarInitials");

  if (sesion) {
    // una vez logueado se esconden los botones
    if (botonesAuth) botonesAuth.classList.add("d-none"); // se esconde el Login/Registro
    if (perfilUsuario) perfilUsuario.classList.remove("d-none"); // Mostramos Perfil

    // Ponemos el nombre
    if (nombreTxt) nombreTxt.textContent = sesion.nombre;

    // Creamos las iniciales (Ej: Juan Perez -> JP)
    if (avatarCirculo) {
      const inicialN = sesion.nombre.charAt(0).toUpperCase();
      const inicialA = sesion.apellido
        ? sesion.apellido.charAt(0).toUpperCase()
        : "";
      avatarCirculo.textContent = inicialN + inicialA;
    }
  } else {
    // Si no hay sesión (Cerró sesión o nunca entró)
    if (botonesAuth) botonesAuth.classList.remove("d-none");
    if (perfilUsuario) perfilUsuario.classList.add("d-none");
  }
}

// ==========================================
// 4. CERRAR SESIÓN
// ==========================================
window.cerrarSesion = function () {
  localStorage.removeItem("usuarioActivo");
  actualizarInterfaz();
};

// Al cargar la página, verificamos si ya había una sesión iniciada
document.addEventListener("DOMContentLoaded", actualizarInterfaz);

/* ACTIVAR SELECCIÓN DE CATEGORÍAS */

document.querySelectorAll(".filter-tag").forEach((tag) => {
  tag.addEventListener("click", function () {
    document
      .querySelectorAll(".filter-tag")
      .forEach((t) => t.classList.remove("active"));

    this.classList.add("active");
  });
});

/* =========================================
   AGREGAR NUEVA CATEGORÍA DESDE MODAL
========================================= */

document.addEventListener("DOMContentLoaded", () => {
  const categoryForm = document.getElementById("addCategoryForm");

  if (!categoryForm) return;

  categoryForm.addEventListener("submit", function (e) {
    e.preventDefault();

    const nombreCategoria = document
      .getElementById("categoryNameInput")
      .value.trim();

    if (!nombreCategoria) return;

    const catGrid = document.querySelector(".cat-grid");

    const nuevaCategoria = document.createElement("a");
    nuevaCategoria.href = "#";
    nuevaCategoria.classList.add("cat-card");

    nuevaCategoria.innerHTML = `
      <div style="font-size:40px; margin-bottom:10px;">🎮</div>
      <h3 style="color:white; font-size:18px; margin:5px 0;">
        ${nombreCategoria}
      </h3>
      <p style="color:#8b9bb4; font-size:14px;">
        Nuevo
    `;

    catGrid.appendChild(nuevaCategoria);

    categoryForm.reset();

    const modal = bootstrap.Modal.getInstance(
      document.getElementById("addCategoryModal"),
    );

    modal.hide();
  });
});

/* DISPONIBILIDAD DE JUEGOS */

document.querySelectorAll(".game-card").forEach((card) => {
  const disponible = Math.random() > 0.3;

  const badge = document.createElement("div");
  badge.className = disponible
    ? "status-badge disponible"
    : "status-badge agotado";

  badge.style.position = "absolute";
  badge.style.bottom = "10px";
  badge.style.left = "10px";

  badge.textContent = disponible ? "Disponible" : "Agotado";

  const thumb = card.querySelector(".game-thumbnail");
  thumb.style.position = "relative";
  thumb.appendChild(badge);
});

/* =========================================
   MARCAR / ELIMINAR JUEGOS FAVORITOS
========================================= */

let favoritos = [];

document.querySelectorAll(".game-card").forEach((card) => {
  const favBtn = document.createElement("button");
  favBtn.innerHTML = "⭐";
  favBtn.className = "fav-btn";

  favBtn.style.position = "absolute";
  favBtn.style.top = "260px";
  favBtn.style.right = "10px";

  const thumb = card.querySelector(".game-thumbnail");
  thumb.style.position = "relative";
  thumb.appendChild(favBtn);

  favBtn.addEventListener("click", (e) => {
    e.stopPropagation();

    const titulo = card.querySelector(".game-title-catalog").textContent;

    // SI YA ES FAVORITO → ELIMINAR
    if (card.classList.contains("favorito")) {
      card.classList.remove("favorito");

      favoritos = favoritos.filter(
        (game) =>
          game.querySelector(".game-title-catalog").textContent !== titulo,
      );
    }
    // SI NO ES FAVORITO → AGREGAR
    else {
      card.classList.add("favorito");

      favoritos.push(card.cloneNode(true));
    }

    renderFavoritos();
  });
});

/* MOSTRAR LISTA DE FAVORITOS */

function renderFavoritos() {
  const grid = document.getElementById("favoritesGrid");
  if (!grid) return;

  grid.innerHTML = "";

  if (favoritos.length === 0) {
    grid.innerHTML = "<p style='color:gray;'>Aún no tienes favoritos.</p>";
    return;
  }

  favoritos.forEach((game) => {
    grid.appendChild(game);
  });
}
