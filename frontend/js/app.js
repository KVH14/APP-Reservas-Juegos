// ════════════════════════════════════════════════════════════
//  app.js — JavaScript general de Playres (versión integrada con API)
//  Secciones:
//    1. NAVBAR scroll
//    2. CATÁLOGO — filtros, galería, paginación
//    3. PRODUCT DETAIL — galería, días, tabs
//    4. ADMIN — tabla, modales, CRUD (con ProductoAPI)
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
//  2. CATÁLOGO (solo se ejecuta si existe #gamesGrid)
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
                  <div class="game-price">$${Number(g.price).toLocaleString("es-CO")}<span>/día</span></div>
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
//  3. PRODUCT DETAIL (solo si existe #galleryMain)
// ════════════════════════════════════════════════════════════
if (document.getElementById("galleryMain")) {
  // Cambiar imagen galería
  window.changeGallery = function (el, emoji, c1, c2) {
    document
      .querySelectorAll(".gallery-thumb")
      .forEach((t) => t.classList.remove("active"));
    el.classList.add("active");
    document.getElementById("galleryEmoji").textContent = emoji;
    document.getElementById("galleryMain").style.background =
      `linear-gradient(135deg, ${c1}, ${c2})`;
  };

  // Selector de días y cálculo de total
  const PRICE = 9900;
  window.selectDays = function (el, days) {
    document
      .querySelectorAll(".day-btn")
      .forEach((b) => b.classList.remove("active"));
    el.classList.add("active");
    document.getElementById("totalDisplay").textContent =
      "$" + (PRICE * days).toLocaleString("es-CO");
  };

  // Cambiar tabs
  window.switchTab = function (el, tabId) {
    document
      .querySelectorAll(".detail-tab")
      .forEach((t) => t.classList.remove("active"));
    document
      .querySelectorAll(".tab-content-playres")
      .forEach((t) => t.classList.remove("active"));
    el.classList.add("active");
    document.getElementById(tabId).classList.add("active");
  };
}

// ════════════════════════════════════════════════════════════
//  4. ADMIN PANEL (solo si existe #tableBody)
//     Ahora integrado con ProductoAPI (definido en api.js)
// ════════════════════════════════════════════════════════════
if (document.getElementById("tableBody")) {
  let products = [];           // se llena desde el backend
  let filteredProds = [];
  let currentPage = 1;
  const perPage = 8;
  let deleteIndex = -1;

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
      .map((p) => {
        const realIdx = products.indexOf(p);
        return `
        <tr>
          <td>
            <div class="tbl-game-info">
              <div class="tbl-thumb ${p.bg}">${p.emoji}</div>
              <div>
                <div class="tbl-game-name">${p.title}</div>
                <div class="tbl-game-id">${p.id}</div>
              </div>
            </div>
          </td>
          <td><span class="platform-tbl">${p.platform}</span></td>
          <td>${p.genre}</td>
          <td class="tbl-price">$${Number(p.price).toLocaleString("es-CO")}</td>
          <td><span class="tbl-stars">${"★".repeat(Math.floor(p.rating))}</span> <span class="tbl-rating-num">${p.rating}</span></td>
          <td><span class="status-badge ${p.status}">${p.status.charAt(0).toUpperCase() + p.status.slice(1)}</span></td>
          <td class="${p.stock === 0 ? "tbl-stock-out" : "tbl-stock"}">${p.stock}</td>
          <td>
            <div class="action-btns">
              <button class="btn-edit" onclick="openEditModal(${realIdx})">✏️ Editar</button>
              <button class="btn-delete" onclick="openDeleteModal(${realIdx})">🗑️</button>
            </div>
          </td>
        </tr>`;
      })
      .join("");

    // Paginación
    document.getElementById("prevPage").disabled = currentPage === 1;
    document.getElementById("nextPage").disabled = currentPage >= totalPages;
    document
      .getElementById("page1")
      .classList.toggle("active", currentPage === 1);
    document
      .getElementById("page2")
      .classList.toggle("active", currentPage === 2);
    document.getElementById("page2").style.display =
      totalPages < 2 ? "none" : "flex";

    const from = start + 1;
    const to = Math.min(start + perPage, filteredProds.length);
    document.getElementById("pagInfo").textContent =
      `Mostrando ${from}–${to} de ${filteredProds.length} productos`;
    document.getElementById("resultCount").textContent =
      `Mostrando ${filteredProds.length} producto${filteredProds.length !== 1 ? "s" : ""}`;

    // Stats
    document.getElementById("totalCount").textContent = products.length;
    document.getElementById("availCount").textContent = products.filter(
      (p) => p.status === "disponible",
    ).length;
    document.getElementById("reservedCount").textContent = products.filter(
      (p) => p.status === "reservado",
    ).length;
    document.getElementById("outCount").textContent = products.filter(
      (p) => p.status === "agotado",
    ).length;
  }

  window.filterTable = function () {
    const q = document.getElementById("searchInput").value.toLowerCase();
    const plt = document.getElementById("platformFilter").value;
    const sts = document.getElementById("statusFilter").value;
    filteredProds = products.filter(
      (p) =>
        (!q ||
          p.title.toLowerCase().includes(q) ||
          String(p.id).toLowerCase().includes(q)) &&
        (!plt || p.platform === plt) &&
        (!sts || p.status === sts),
    );
    currentPage = 1;
    renderTable();
  };

  window.changePage = function (dir) {
    currentPage += dir;
    renderTable();
  };
  window.goPage = function (n) {
    currentPage = n;
    renderTable();
  };

  // Abrir modal para agregar (usa el modal existente en tu HTML)
  window.openAddModal = function () {
    document.getElementById("modalTitle").textContent = "➕ Agregar producto";
    document.getElementById("editIndex").value = -1;
    // Limpiar campos del modal (asegúrate que existan en tu HTML)
    ["fTitle", "fPrice", "fStock", "fRating", "fImageUrl", "fPoliticas", "fCategoriaId"].forEach(
      (id) => { const el = document.getElementById(id); if (el) el.value = ""; }
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
    try {
      const fresh = await ProductoAPI.obtener(p.id);
      document.getElementById("modalTitle").textContent = "✏️ Editar producto";
      document.getElementById("editIndex").value = idx;
      document.getElementById("fTitle").value = fresh.titulo || fresh.title || p.title;
      document.getElementById("fPrice").value = fresh.precio || fresh.price || p.price;
      document.getElementById("fStock").value = fresh.stock || p.stock;
      document.getElementById("fRating").value = fresh.rating || p.rating;
      document.getElementById("fPlatform").value = fresh.plataforma || fresh.platform;
      document.getElementById("fGenre").value = fresh.genero || fresh.genre;
      document.getElementById("fStatus").value = fresh.estado || fresh.status;
      document.getElementById("fEmoji").value = fresh.emoji || p.emoji;
      if (document.getElementById("fImageUrl")) document.getElementById("fImageUrl").value = fresh.imagenUrl || "";
      if (document.getElementById("fPoliticas")) document.getElementById("fPoliticas").value = fresh.politicas || "";
      if (document.getElementById("fCategoriaId")) {
        document.getElementById("fCategoriaId").value = fresh.categorias && fresh.categorias[0] ? fresh.categorias[0].id : "";
      }
      new bootstrap.Modal(document.getElementById("productModal")).show();
    } catch (err) {
      showToast("danger", "❌", "No se pudo cargar el producto: " + err.message);
    }
  };

  // Guardar producto (crear o actualizar) usando ProductoAPI
  window.saveProduct = async function () {
    const idx = parseInt(document.getElementById("editIndex").value);
    const title = document.getElementById("fTitle").value.trim();
    const price = parseInt(document.getElementById("fPrice").value);
    if (!title || !price) {
      showToast("danger", "⚠️", "Completa título y precio.");
      return;
    }

    const dto = {
      titulo: title,
      plataforma: document.getElementById("fPlatform").value,
      genero: document.getElementById("fGenre").value,
      precio: price,
      rating: parseFloat(document.getElementById("fRating").value) || 4.5,
      stock: document.getElementById("fStock").value !== "" ? parseInt(document.getElementById("fStock").value) : 0,
      estado: document.getElementById("fStatus").value,
      emoji: document.getElementById("fEmoji").value,
      imagenUrl: document.getElementById("fImageUrl") ? document.getElementById("fImageUrl").value : undefined,
      politicas: document.getElementById("fPoliticas") ? document.getElementById("fPoliticas").value : undefined,
      categoriaId: document.getElementById("fCategoriaId") ? Number(document.getElementById("fCategoriaId").value) : null
    };

    try {
      if (idx >= 0) {
        const prodId = products[idx].id;
        await ProductoAPI.actualizar(prodId, dto);
        showToast("success", "✅", "Producto actualizado.");
      } else {
        await ProductoAPI.crear(dto);
        showToast("success", "✅", "Producto agregado.");
      }
      await cargarProductos();
      bootstrap.Modal.getInstance(document.getElementById("productModal")).hide();
    } catch (err) {
      showToast("danger", "❌", err.message || "Error al guardar");
    }
  };

  // Abrir modal de confirmación de borrado
  window.openDeleteModal = function (idx) {
    deleteIndex = idx;
    document.getElementById("deleteGameName").textContent =
      '"' + products[idx].title + '"';
    new bootstrap.Modal(document.getElementById("deleteModal")).show();
  };

  // Confirmar eliminación usando ProductoAPI
  window.confirmDelete = async function () {
    if (deleteIndex < 0) return;
    const prod = products[deleteIndex];
    try {
      await ProductoAPI.eliminar(prod.id);
      showToast("danger", "🗑️", `"${prod.title}" eliminado.`);
      deleteIndex = -1;
      await cargarProductos();
      bootstrap.Modal.getInstance(document.getElementById("deleteModal")).hide();
    } catch (err) {
      showToast("danger", "❌", "No se pudo eliminar: " + err.message);
    }
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

  // Iniciar admin: cargar productos desde backend
  cargarProductos();
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
