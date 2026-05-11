// ════════════════════════════════════════════════════════════
//  app.js — JavaScript general de Playres
//  Secciones:
//    1. NAVBAR scroll
//    2. CATÁLOGO — filtros, galería, paginación
//    3. PRODUCT DETAIL — galería, días, tabs
//    4. ADMIN — tabla, modales, CRUD
// ════════════════════════════════════════════════════════════

// ── 1. NAVBAR SCROLL ─────────────────────────────────────────
const navbar = document.querySelector(".navbar-playres");
if (navbar) {
  window.addEventListener("scroll", () => {
    navbar.classList.toggle("scrolled", window.scrollY > 30);
  });
}

const bgPorPlataformaUI = {
  PS5: "game-thumb-purple",
  Xbox: "game-thumb-red",
  Switch: "game-thumb-green",
  PC: "game-thumb-blue",
};

function formatoPrecio(valor) {
  return Number(valor || 0).toLocaleString("es-CO");
}

function estrellas(rating) {
  return "★".repeat(Math.max(1, Math.round(Number(rating || 0))));
}

function normalizarTexto(valor) {
  return String(valor || "")
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "")
    .trim()
    .toLowerCase();
}

function claveGenero(valor) {
  const texto = normalizarTexto(valor);
  const alias = {
    accion: "accion",
    action: "accion",
    aventura: "aventura",
    adventure: "aventura",
    deportes: "deportes",
    sports: "deportes",
    carreras: "carreras",
    racing: "carreras",
    shooter: "shooter",
    shooters: "shooter",
    rpg: "rpg",
    roleplaying: "rpg",
    "role-playing": "rpg",
    "role-playing-games": "rpg",
  };
  return alias[texto] || texto;
}

function obtenerImagenProducto(producto) {
  return (
    producto?.imagenUrl ||
    producto?.background_image ||
    producto?.backgroundImage ||
    producto?.imagen ||
    ""
  );
}

function productoCard(p, extraTag = "") {
  const agotado = Number(p.stock || 0) === 0;
  const estado = agotado ? "agotado" : "disponible";
  return `
    <div class="game-card" data-product-id="${p.id}" data-stock="${p.stock || 0}" data-estado="${estado}">
      <a href="product-detail.html?id=${p.id}" class="catalog-card-link">
        <div class="game-thumbnail ${p.bg || bgPorPlataformaUI[p.plataforma] || "game-thumb-blue"}">
          <div class="game-tags">
            <span class="game-tag">${p.genero || "Accion"}</span>
            ${extraTag}
            ${agotado ? `<span class="game-tag-top">AGOTADO</span>` : ""}
          </div>
          <div class="status-badge ${estado} catalog-status-badge">${agotado ? "Agotado" : "Disponible"}</div>
          ${
            obtenerImagenProducto(p)
              ? `<img class="game-cover" src="${obtenerImagenProducto(p)}" alt="${p.titulo}" />`
              : `<span style="font-size:3rem">${p.emoji || "🎮"}</span>`
          }
        </div>
      </a>
      <div class="game-card-info">
        <div class="game-platform">${p.plataforma || "PC"}</div>
        <h3 class="game-title-catalog">${p.titulo}</h3>
        <div class="game-bottom">
          <div class="game-price">$${formatoPrecio(p.precio)}<span>/día</span></div>
          <div><span class="game-stars">${estrellas(p.rating)}</span><span class="game-rating">${p.rating || 0}</span></div>
        </div>
        <a href="product-detail.html?id=${p.id}"
           class="catalog-btn-ver ${agotado ? "disabled" : ""}"
           ${agotado ? 'style="pointer-events:none;opacity:0.5"' : ""}>
          ${agotado ? "Agotado" : "Ver detalles →"}
        </a>
      </div>
    </div>
  `;
}

// ════════════════════════════════════════════════════════════
//  2. CATÁLOGO (solo se ejecuta si existe #gamesGrid)
// ════════════════════════════════════════════════════════════
if (document.getElementById("featuredGamesGrid")) {
  const normalizarListaHome = (respuesta) =>
    Array.isArray(respuesta) ? respuesta : respuesta?.contenido || [];

  const esReservaActivaHome = (reserva) =>
    !["CANCELADA", "CANCELADO"].includes((reserva.estado || "").toUpperCase());

  const formatoNumeroCortoHome = (valor) => {
    const numero = Number(valor || 0);
    if (numero >= 1000) {
      const miles = numero / 1000;
      return `${Number.isInteger(miles) ? miles : miles.toFixed(1)}K`;
    }
    return String(numero);
  };

  const escapeHtmlHome = (valor) =>
    String(valor ?? "")
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;")
      .replace(/'/g, "&#39;");

  function reservasPorProductoHome(reservas) {
    return reservas.reduce((conteo, reserva) => {
      const id = reserva.producto?.id;
      if (!id || !esReservaActivaHome(reserva)) return conteo;
      conteo[id] = (conteo[id] || 0) + 1;
      return conteo;
    }, {});
  }

  function actualizarTextoHome(selector, texto) {
    const el = document.querySelector(selector);
    if (el) el.textContent = texto;
  }

  function renderHeroHome(productos, reservas) {
    const totalStock = productos.reduce((total, p) => total + Number(p.stock || 0), 0);
    const disponibles = productos.filter((p) => Number(p.stock || 0) > 0);
    const reservasActivas = reservas.filter(esReservaActivaHome);
    const conteo = reservasPorProductoHome(reservas);
    const destacado = [...(disponibles.length ? disponibles : productos)].sort(
      (a, b) =>
        (conteo[b.id] || 0) - (conteo[a.id] || 0) ||
        Number(b.rating || 0) - Number(a.rating || 0),
    )[0];

    actualizarTextoHome(".badge-top", `${formatoNumeroCortoHome(totalStock)} juegos disponibles hoy`);
    actualizarTextoHome(".stats-row .stat-item:nth-child(1) .stat-num", formatoNumeroCortoHome(disponibles.length));
    actualizarTextoHome(".side-stats .side-stat-card:nth-child(2) .side-stat-value", formatoNumeroCortoHome(reservasActivas.length));
    actualizarTextoHome(".side-stats .side-stat-card:nth-child(3) .side-stat-value", formatoNumeroCortoHome(totalStock));

    if (!destacado) {
      actualizarTextoHome(".game-card-main .game-title", "No hay productos disponibles");
      actualizarTextoHome(".game-card-main .game-price-row", "Carga productos desde el panel admin");
      actualizarTextoHome(".game-card-main .game-meta-item:nth-child(2) .game-meta-value", "0");
      actualizarTextoHome(".game-card-main .game-meta-item:nth-child(3) .game-meta-value", "0");
      return;
    }

    const imagen = document.querySelector(".game-card-main .gc-img");
    if (imagen) {
      imagen.innerHTML = obtenerImagenProducto(destacado)
        ? `<img class="hero-featured-cover" src="${escapeHtmlHome(obtenerImagenProducto(destacado))}" alt="${escapeHtmlHome(destacado.titulo)}" />`
        : `<span class="hero-featured-emoji">${escapeHtmlHome(destacado.emoji || "🎮")}</span>`;
    }

    actualizarTextoHome(".game-card-main .game-title", destacado.titulo || "Producto");
    actualizarTextoHome(".game-card-main .game-price-row strong", `$${formatoPrecio(destacado.precio)}`);
    actualizarTextoHome(".game-card-main .rating-value", destacado.rating || 0);
    actualizarTextoHome(".game-card-main .game-meta-item:nth-child(2) .game-meta-value", formatoNumeroCortoHome(conteo[destacado.id] || 0));
    actualizarTextoHome(".game-card-main .game-meta-item:nth-child(3) .game-meta-value", formatoNumeroCortoHome(destacado.stock));

    const boton = document.querySelector(".game-card-main .btn-res-card");
    if (boton) boton.href = `product-detail.html?id=${destacado.id}`;
  }

  function renderCategoriasHome(productos, categorias) {
    const grid = document.querySelector(".cat-grid");
    if (!grid) return;

    const conteos = productos.reduce((acc, producto) => {
      const genero = (producto.genero || "Sin categoria").trim();
      acc[genero] = (acc[genero] || 0) + 1;
      return acc;
    }, {});
    const porNombre = new Map();

    categorias.forEach((categoria) => {
      const nombre = (categoria.nombre || "").trim();
      if (nombre) {
        porNombre.set(nombre, {
          nombre,
          emoji: categoria.emoji || "🎮",
          cantidad: conteos[nombre] || 0,
        });
      }
    });

    Object.entries(conteos).forEach(([nombre, cantidad]) => {
      if (!porNombre.has(nombre)) {
        porNombre.set(nombre, { nombre, emoji: "🎮", cantidad });
      }
    });

    const items = [
      {
        nombre: "Tendencia",
        emoji: "🔥",
        cantidad: productos.length,
        href: "catalogo.html",
        active: true,
      },
      ...Array.from(porNombre.values())
        .sort((a, b) => b.cantidad - a.cantidad || a.nombre.localeCompare(b.nombre))
        .slice(0, 5)
        .map((categoria) => ({
          ...categoria,
          href: `catalogo.html?genre=${encodeURIComponent(categoria.nombre)}`,
        })),
    ];

    grid.innerHTML =
      items.length === 0
        ? `<p style="color: gray;">No hay categorias disponibles.</p>`
        : items
            .map(
              (categoria) => `
                <a href="${categoria.href}" class="cat-card ${categoria.active ? "active" : ""}">
                  <span class="category-emoji">${escapeHtmlHome(categoria.emoji)}</span>
                  <span class="category-name">${escapeHtmlHome(categoria.nombre)}</span>
                  <span class="category-count">${categoria.cantidad} juego${categoria.cantidad === 1 ? "" : "s"}</span>
                </a>
              `,
            )
            .join("");
  }

  async function cargarDatosRealesHome() {
    await cargarJuegosAPI();
    const productos = [...juegos];
    const [reservasRespuesta, categoriasRespuesta] = await Promise.all([
      ReservaAPI.listar().catch(() => []),
      CategoriaAPI.listar().catch(() => []),
    ]);
    const reservas = normalizarListaHome(reservasRespuesta);
    const categorias = normalizarListaHome(categoriasRespuesta);
    const conteo = reservasPorProductoHome(reservas);

    renderHeroHome(productos, reservas);
    renderCategoriasHome(productos, categorias);

    const grid = document.getElementById("featuredGamesGrid");
    const destacados = [...productos]
      .sort(
        (a, b) =>
          (conteo[b.id] || 0) - (conteo[a.id] || 0) ||
          Number(b.rating || 0) - Number(a.rating || 0),
      )
      .slice(0, 4);
    grid.innerHTML =
      destacados.length === 0
        ? `<div class="col-12 catalog-empty"><p>No hay productos disponibles.</p></div>`
        : destacados
            .map((p, i) =>
              productoCard(
                p,
                i === 0
                  ? `<span class="game-tag-top">TOP 1</span>`
                  : i === 1
                    ? `<span class="game-tag-new">+ NUEVO</span>`
                    : "",
              ),
            )
            .join("");
  }

  window.cargarDatosRealesHome = cargarDatosRealesHome;

  cargarDatosRealesHome().catch((error) => {
    console.error("Error cargando datos reales del home:", error);
  });
}

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
        if (claveGenero(tag.textContent).includes(claveGenero(genreParam))) {
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
          (g.titulo || "").toLowerCase().includes(q) ||
          normalizarTexto(g.genero).includes(normalizarTexto(q)) ||
          claveGenero(g.genero).includes(claveGenero(q)) ||
          (g.plataforma || "").toLowerCase().includes(q)) &&
        (!activeGenre || claveGenero(g.genero) === claveGenero(activeGenre)) &&
        (!activePlatform || g.plataforma === activePlatform),
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
              <div class="game-card" data-product-id="${g.id}" data-stock="${g.stock || 0}" data-estado="${g.stock === 0 ? "agotado" : "disponible"}">
                <a href="product-detail.html?id=${g.id}" class="catalog-card-link">
                  <div class="game-thumbnail ${g.bg}">
                    <div class="game-tags">
                      <span class="game-tag">${g.genero || "Accion"}</span>
                      ${g.stock === 0 ? `<span class="game-tag-top">AGOTADO</span>` : ""}
                    </div>
                    <div class="status-badge ${g.stock === 0 ? "agotado" : "disponible"} catalog-status-badge">${g.stock === 0 ? "Agotado" : "Disponible"}</div>
                    ${
                      obtenerImagenProducto(g)
                        ? `<img class="game-cover" src="${obtenerImagenProducto(g)}" alt="${g.titulo}" />`
                        : `<span style="font-size:3rem">${g.emoji}</span>`
                    }
                  </div>
                </a>
                <div class="game-card-info">
                  <div class="game-platform">${g.plataforma || "PC"}</div>
                  <h3 class="game-title-catalog">${g.titulo}</h3>
                  <div class="game-bottom">
                    <div class="game-price">$${Number(g.precio || 0).toLocaleString("es-CO")}<span>/día</span></div>
                    <div><span class="game-stars">★★★★★</span><span class="game-rating">${g.rating}</span></div>
                  </div>
                  <a href="product-detail.html?id=${g.id}"
                     class="catalog-btn-ver ${g.stock === 0 ? "disabled" : ""}"
                     ${g.stock === 0 ? 'style="pointer-events:none;opacity:0.5"' : ""}>
                    ${g.stock === 0 ? "Agotado" : "Ver detalles →"}
                  </a>
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
  let currentDetailProduct = null;
  let selectedDetailDays = 1;

  // Cambiar imagen galería
  // Selector de días y cálculo de total
  let detailPrice = 9900;
  window.selectDays = function (el, days) {
    selectedDetailDays = days;
    document
      .querySelectorAll(".day-btn")
      .forEach((b) => b.classList.remove("active"));
    el.classList.add("active");
    document.getElementById("totalDisplay").textContent =
      "$" + formatoPrecio(detailPrice * days);
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

  function setText(selector, value) {
    const el = document.querySelector(selector);
    if (el) el.textContent = value;
  }

  function renderDetail(producto) {
    producto.imagenUrl = obtenerImagenProducto(producto);
    currentDetailProduct = producto;
    detailPrice = Number(producto.precio || 0);
    const agotado = Number(producto.stock || 0) === 0;

    setText(".breadcrumb-playres .current", producto.titulo || "Producto");
    setText(".platform-badge-detail", `🎮 ${producto.plataforma || "PC"}`);
    setText(".detail-title", producto.titulo || "Producto");
    setText(".detail-rating-num", producto.rating || 0);
    setText(".detail-rating-count", `Stock: ${producto.stock || 0}`);
    setText(".detail-price-amount", `$${formatoPrecio(producto.precio)} /día`);
    setText("#totalDisplay", `$${formatoPrecio(producto.precio)}`);

    const genreRow = document.querySelector(".detail-genre-row");
    if (genreRow) {
      genreRow.innerHTML = `<span class="genre-tag">${producto.genero || "Accion"}</span>`;
    }

    const stockMeta = document.querySelector(
      ".detail-meta-row .detail-meta-item:nth-child(3) .detail-meta-value",
    );
    if (stockMeta) stockMeta.textContent = producto.stock || 0;

    const reservarBtn = document.querySelector(".btn-reservar-detail");
    if (reservarBtn) {
      reservarBtn.textContent = agotado ? "Agotado" : "Reservar ahora →";
      reservarBtn.classList.toggle("disabled", agotado);
      reservarBtn.style.pointerEvents = agotado ? "none" : "";
      reservarBtn.style.opacity = agotado ? "0.6" : "";
    }

    const desc = document.querySelector("#tab-desc .detail-description");
    if (desc) {
      desc.innerHTML = `<p>${producto.politicas || "Producto disponible para reserva desde el catalogo de Playres."}</p>`;
    }

    const specs = document.querySelector("#tab-specs .specs-table");
    if (specs) {
      specs.innerHTML = `
        <tr><td>Titulo</td><td>${producto.titulo || ""}</td></tr>
        <tr><td>Plataforma</td><td>${producto.plataforma || ""}</td></tr>
        <tr><td>Genero</td><td>${producto.genero || ""}</td></tr>
        <tr><td>Precio por dia</td><td>$${formatoPrecio(producto.precio)}</td></tr>
        <tr><td>Estado</td><td>${producto.estado || "disponible"}</td></tr>
        <tr><td>Stock</td><td>${producto.stock || 0}</td></tr>
      `;
    }

    const gallery = document.getElementById("galleryMain");
    if (gallery) {
      gallery.className = `gallery-main ${bgPorPlataformaUI[producto.plataforma] || "game-thumb-blue"}`;
      document.getElementById("galleryEmoji").innerHTML = producto.imagenUrl
        ? `<img class="detail-cover" src="${producto.imagenUrl}" alt="${producto.titulo}" />`
        : producto.emoji || "🎮";
    }

    document.title = `Playres - ${producto.titulo || "Detalle del Juego"}`;
  }

  function fechaISO(offsetDias = 0) {
    const fecha = new Date();
    fecha.setDate(fecha.getDate() + offsetDias);
    return fecha.toISOString().slice(0, 10);
  }

  function usuarioActivo() {
    return JSON.parse(sessionStorage.getItem("usuarioActivo") || "null");
  }

  async function cargarDatosExtraDetalle(producto) {
    const reservasProducto = await ReservaAPI.porProducto(producto.id).catch(() => []);
    const reservasActivas = (Array.isArray(reservasProducto) ? reservasProducto : []).filter(
      (r) => !["CANCELADA", "CANCELADO"].includes((r.estado || "").toUpperCase()),
    );
    const reservasMeta = document.querySelector(
      ".detail-meta-row .detail-meta-item:nth-child(2) .detail-meta-value",
    );
    if (reservasMeta) reservasMeta.textContent = reservasActivas.length;

    const disponibilidad = await DisponibilidadAPI.verificar(producto.id).catch(() => null);
    if (disponibilidad) {
      const agotado = disponibilidad !== "Disponible";
      const reservarBtn = document.querySelector(".btn-reservar-detail");
      if (reservarBtn) {
        reservarBtn.textContent = agotado ? "No disponible" : "Reservar ahora →";
        reservarBtn.classList.toggle("disabled", agotado);
        reservarBtn.style.pointerEvents = agotado ? "none" : "";
        reservarBtn.style.opacity = agotado ? "0.6" : "";
      }
    }

    const politicas = await ProductoAPI.politicas(producto.id).catch(() => null);
    const desc = document.querySelector("#tab-desc .detail-description");
    if (desc && politicas?.politicas) {
      desc.innerHTML = `<p>${politicas.politicas}</p>`;
    }

    const caracteristicas = await ProductoAPI.caracteristicas(producto.id).catch(() => []);
    const specs = document.querySelector("#tab-specs .specs-table");
    if (specs && Array.isArray(caracteristicas) && caracteristicas.length > 0) {
      specs.innerHTML += caracteristicas
        .map((c) => `<tr><td>${c.clave || "Caracteristica"}</td><td>${c.valor || ""}</td></tr>`)
        .join("");
    }

    renderPuntuacionDetalle(producto);
  }

  function renderPuntuacionDetalle(producto) {
    const reviews = document.getElementById("tab-reviews");
    if (!reviews || document.getElementById("ratingBackendBox")) return;

    reviews.insertAdjacentHTML(
      "afterbegin",
      `
        <div id="ratingBackendBox" class="detail-description" style="margin-bottom:18px;">
          <p>Califica este producto:</p>
          <div class="d-flex flex-wrap gap-2">
            ${[1, 2, 3, 4, 5]
              .map((n) => `<button class="btn btn-sm btn-outline-light" onclick="puntuarProducto(${n})">${n} ★</button>`)
              .join("")}
          </div>
        </div>
      `,
    );
  }

  window.puntuarProducto = async function (puntuacion) {
    if (!currentDetailProduct) return;
    try {
      const actualizado = await ProductoAPI.puntuar(currentDetailProduct.id, puntuacion);
      currentDetailProduct = actualizado;
      setText(".detail-rating-num", actualizado.rating || puntuacion);
      alert("Puntuacion registrada.");
    } catch (error) {
      alert("No se pudo registrar la puntuacion: " + error.message);
    }
  };

  async function crearReservaDetalle(e) {
    e.preventDefault();
    if (!currentDetailProduct || Number(currentDetailProduct.stock || 0) === 0) return;

    let usuario = usuarioActivo();
    let nombreCliente = usuario?.nombre;
    let emailCliente = usuario?.email;

    if (!nombreCliente) nombreCliente = prompt("Nombre para la reserva:");
    if (!emailCliente) emailCliente = prompt("Email para la reserva:");
    if (!nombreCliente || !emailCliente) return;

    try {
      await ReservaAPI.crear({
        nombreCliente,
        emailCliente,
        productoId: currentDetailProduct.id,
        fechaReserva: fechaISO(0),
        fechaDevolucion: fechaISO(selectedDetailDays),
        tipo: "NORMAL",
      });
      alert("Reserva creada correctamente.");
      currentDetailProduct = await ProductoAPI.obtener(currentDetailProduct.id);
      renderDetail(currentDetailProduct);
      await cargarDatosExtraDetalle(currentDetailProduct);
    } catch (error) {
      alert("No se pudo crear la reserva: " + error.message);
    }
  }

  async function crearFavoritoDetalle(e) {
    e.preventDefault();
    if (!currentDetailProduct) return;
    const usuario = usuarioActivo();
    if (!usuario?.id) {
      alert("Inicia sesion para agregar favoritos.");
      return;
    }

    try {
      await FavoritoAPI.crear({
        usuarioId: usuario.id,
        productoId: currentDetailProduct.id,
      });
      alert("Producto agregado a tu lista.");
      await cargarFavoritosUsuario();
    } catch (error) {
      alert("No se pudo agregar a favoritos: " + error.message);
    }
  }

  async function cargarDetalleProducto() {
    setText(".breadcrumb-playres .current", "Cargando producto...");
    setText(".detail-title", "Cargando producto...");
    setText(".detail-price-amount", "$0 /día");
    setText("#totalDisplay", "$0");
    const relatedGrid = document.querySelector(".related-grid");
    if (relatedGrid) {
      relatedGrid.innerHTML = `<p style="color: gray">Cargando productos relacionados...</p>`;
    }

    try {
      const params = new URLSearchParams(window.location.search);
      const id = params.get("id");
      const primeraLista = await ProductoAPI.listar();
      const productos = Array.isArray(primeraLista)
        ? primeraLista
        : primeraLista?.contenido || [];
      const producto = id ? await ProductoAPI.obtener(id) : productos[0];

      if (!producto) return;
      renderDetail(producto);
      await cargarDatosExtraDetalle(producto);

      const segundaLista = await ProductoAPI.listar();
      const relacionados = (Array.isArray(segundaLista)
        ? segundaLista
        : segundaLista?.contenido || [])
        .filter((p) => String(p.id) !== String(producto.id))
        .slice(0, 4);
      if (relatedGrid) {
        relatedGrid.innerHTML = relacionados.map((p) => productoCard(p)).join("");
      }
    } catch (error) {
      console.error("Error cargando detalle del producto:", error);
    }
  }

  cargarDetalleProducto();

  document
    .querySelector(".btn-reservar-detail")
    ?.addEventListener("click", crearReservaDetalle);
  document
    .querySelector(".btn-wishlist")
    ?.addEventListener("click", crearFavoritoDetalle);
}

// ════════════════════════════════════════════════════════════
//  4. ADMIN PANEL (solo si existe #tableBody)
// ════════════════════════════════════════════════════════════
if (document.getElementById("tableBody")) {
  let products = []; // se llena desde el backend
  let filteredProds = [];
  let currentPage = 1;
  const perPage = 8;
  let deleteIndex = -1;
  const bgPorPlataformaAdmin = {
    PS5: "game-thumb-purple",
    Xbox: "game-thumb-red",
    Switch: "game-thumb-green",
    PC: "game-thumb-blue",
  };

  function obtenerListaProductos(respuesta) {
    if (Array.isArray(respuesta)) return respuesta;
    return respuesta?.contenido || [];
  }

  function estadoNormalizado(estado) {
    return (estado || "disponible").toString().toLowerCase();
  }

  function estadoCapitalizado(estado) {
    const valor = estadoNormalizado(estado);
    return valor.charAt(0).toUpperCase() + valor.slice(1);
  }

  window.cargarProductos = async function () {
    try {
      products = obtenerListaProductos(await ProductoAPI.listar());
      filteredProds = [...products];
      renderTable();
    } catch (e) {
      showToast("danger", "❌", "No se pudo conectar al backend: " + e.message);
    }
  };

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
              <div class="tbl-thumb ${bgPorPlataformaAdmin[p.plataforma] || "game-thumb-purple"}">
                ${p.imagenUrl ? `<img class="tbl-cover" src="${p.imagenUrl}" alt="${p.titulo}" />` : p.emoji || "🎮"}
              </div>
              <div>
                <div class="tbl-game-name">${p.titulo}</div>
                <div class="tbl-game-id">${p.id}</div>
              </div>
            </div>
          </td>
          <td><span class="platform-tbl">${p.plataforma || ""}</span></td>
          <td>${p.genero || ""}</td>
          <td class="tbl-price">$${Number(p.precio || 0).toLocaleString("es-CO")}</td>
          <td><span class="tbl-stars">${"★".repeat(Math.floor(p.rating || 0))}</span> <span class="tbl-rating-num">${p.rating || 0}</span></td>
          <td><span class="status-badge ${estadoNormalizado(p.estado)}">${estadoCapitalizado(p.estado)}</span></td>
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
      (p) => estadoNormalizado(p.estado) === "disponible",
    ).length;
    ReservaAPI.listar()
      .then((reservas) => {
        const activas = (Array.isArray(reservas) ? reservas : []).filter(
          (r) => !["CANCELADA", "CANCELADO"].includes((r.estado || "").toUpperCase()),
        );
        document.getElementById("reservedCount").textContent = activas.length;
      })
      .catch(() => {
        document.getElementById("reservedCount").textContent = products.filter(
          (p) => estadoNormalizado(p.estado) === "reservado",
        ).length;
      });
    document.getElementById("outCount").textContent = products.filter(
      (p) => estadoNormalizado(p.estado) === "agotado",
    ).length;
  }

  window.filterTable = function () {
    const q = document.getElementById("searchInput").value.toLowerCase();
    const plt = document.getElementById("platformFilter").value;
    const sts = document.getElementById("statusFilter").value;
    filteredProds = products.filter(
      (p) =>
        (!q ||
          (p.titulo || "").toLowerCase().includes(q) ||
          String(p.id || "").toLowerCase().includes(q)) &&
        (!plt || p.plataforma === plt) &&
        (!sts || estadoNormalizado(p.estado) === sts),
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

  window.openEditModal = function (idx) {
    const p = products[idx];
    document.getElementById("modalTitle").textContent = "✏️ Editar producto";
    document.getElementById("editIndex").value = idx;
    document.getElementById("fTitle").value = p.titulo || "";
    document.getElementById("fPrice").value = p.precio || "";
    document.getElementById("fStock").value = p.stock;
    document.getElementById("fRating").value = p.rating;
    document.getElementById("fPlatform").value = p.plataforma || "PC";
    document.getElementById("fGenre").value = p.genero || "RPG";
    document.getElementById("fStatus").value = estadoNormalizado(p.estado);
    document.getElementById("fEmoji").value = p.emoji;
    new bootstrap.Modal(document.getElementById("productModal")).show();
  };

  window.saveProduct = async function () {
    const idx = parseInt(document.getElementById("editIndex").value);
    const titulo = document.getElementById("fTitle").value.trim();
    const precio = parseInt(document.getElementById("fPrice").value);
    if (!titulo || !precio) {
      showToast("danger", "⚠️", "Completa título y precio.");
      return;
    }

    const producto = {
      titulo,
      plataforma: document.getElementById("fPlatform").value,
      genero: document.getElementById("fGenre").value,
      precio,
      rating: parseFloat(document.getElementById("fRating").value) || 4.5,
      stock:
        document.getElementById("fStock").value !== ""
          ? parseInt(document.getElementById("fStock").value)
          : idx >= 0
            ? products[idx].stock
            : 0,
      estado: document.getElementById("fStatus").value,
      emoji: document.getElementById("fEmoji").value,
      imagenUrl: idx >= 0 ? products[idx].imagenUrl || "" : "",
      politicas: idx >= 0 ? products[idx].politicas || "" : "",
    };

    try {
      if (idx >= 0) {
        await ProductoAPI.actualizar(products[idx].id, producto);
        showToast("success", "✅", "Producto actualizado.");
      } else {
        await ProductoAPI.crear(producto);
        showToast("success", "✅", "Producto agregado.");
      }

      await cargarProductos();
      bootstrap.Modal.getInstance(document.getElementById("productModal")).hide();
    } catch (error) {
      showToast("danger", "Error", "Error al guardar: " + error.message);
    }
  };

  window.openDeleteModal = function (idx) {
    deleteIndex = idx;
    document.getElementById("deleteGameName").textContent =
      '"' + products[idx].titulo + '"';
    new bootstrap.Modal(document.getElementById("deleteModal")).show();
  };

  window.confirmDelete = async function () {
    if (deleteIndex < 0) return;
    const producto = products[deleteIndex];
    const name = producto.titulo;
    try {
      await ProductoAPI.eliminar(producto.id);
      deleteIndex = -1;
      await cargarProductos();
      bootstrap.Modal.getInstance(document.getElementById("deleteModal")).hide();
      showToast("danger", "🗑️", `"${name}" eliminado.`);
    } catch (error) {
      showToast("danger", "Error", "Error al eliminar: " + error.message);
    }
  };

  let toastTimer;
  function showToast(type, icon, msg) {
    const el = document.getElementById("toastEl");
    document.getElementById("toastIcon").textContent = icon;
    document.getElementById("toastMsg").textContent = msg;
    el.className = `playres-toast ${type} show`;
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => el.classList.remove("show"), 3000);
  }

  // ── IMPORTAR DESDE RAWG ──────────────────────────────────
  window.openImportModal = function () {
    document.getElementById("rawgSearchInput").value = "";
    document.getElementById("rawgSearchResults").style.display = "none";
    document.getElementById("rawgImportForm").style.display = "none";
    document.getElementById("importConfirmBtn").style.display = "none";
    document.getElementById("rawgResultsList").innerHTML = "";
    document.getElementById("selectedRawgId").value = "";
    new bootstrap.Modal(document.getElementById("importRawgModal")).show();
  };

  window.searchRawgGames = async function () {
    const query = document.getElementById("rawgSearchInput").value.trim();
    if (!query) {
      showToast("warning", "⚠️", "Ingresa el nombre de un juego.");
      return;
    }

    try {
      showToast("info", "🔍", "Buscando en RAWG...");
      const results = await fetch(
        `http://localhost:8080/api/rawg/buscar?q=${encodeURIComponent(query)}`,
      ).then((r) => r.json());

      if (!results || results.length === 0) {
        showToast("warning", "⚠️", "No se encontraron juegos con ese nombre.");
        document.getElementById("rawgSearchResults").style.display = "none";
        return;
      }

      // Mostrar resultados
      const resultsList = document.getElementById("rawgResultsList");
      resultsList.innerHTML = results
        .map((game) => {
          const gameName = game.nombre || game.name || "Juego sin nombre";
          const gameId = game.id;
          return `
        <div style="padding: 12px; border-bottom: 1px solid var(--border); cursor: pointer; transition: all 0.2s; background: rgba(255,255,255,0.02);" 
             onclick="selectRawgGame(${gameId}, '${gameName.replace(/'/g, "\\'")}')">
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <div>
              <strong style="color: #fff;">${gameName}</strong>
              <div style="font-size: 0.85rem; color: var(--muted); margin-top: 4px;">
                Géneros: ${game.genres && game.genres.length > 0 ? game.genres.map((g) => g.name || g).join(", ") : "N/A"}
              </div>
            </div>
            <div style="color: var(--red); font-weight: bold;">Seleccionar →</div>
          </div>
        </div>
      `;
        })
        .join("");

      document.getElementById("rawgSearchResults").style.display = "block";
      showToast("success", "✅", `Se encontraron ${results.length} juego(s).`);
    } catch (error) {
      showToast("danger", "❌", "Error al buscar: " + error.message);
    }
  };

  window.selectRawgGame = function (rawgId, gameName) {
    document.getElementById("selectedRawgId").value = rawgId;
    document.getElementById("selectedGameTitle").value = gameName;
    document.getElementById("rawgImportForm").style.display = "block";
    document.getElementById("importConfirmBtn").style.display = "inline-block";
    document.getElementById("rawgSearchResults").style.display = "none";
  };

  window.confirmImportGame = async function () {
    const rawgId = parseInt(document.getElementById("selectedRawgId").value);
    const plataforma = document.getElementById("importPlatform").value;
    const precio = parseInt(document.getElementById("importPrice").value);
    const stock = parseInt(document.getElementById("importStock").value);
    const estado = document.getElementById("importStatus").value;

    if (!rawgId || !precio || !stock) {
      showToast("warning", "⚠️", "Completa todos los campos.");
      return;
    }

    try {
      showToast("info", "⏳", "Importando juego...");
      const result = await ProductoAPI.importar({
        rawgId,
        precio,
        stock,
        plataforma,
        estado,
      });

      await cargarProductos();

      bootstrap.Modal.getInstance(
        document.getElementById("importRawgModal"),
      ).hide();
      showToast("success", "✅", `"${result.titulo}" importado correctamente.`);
    } catch (error) {
      showToast("danger", "❌", "Error al importar: " + error.message);
    }
  };

  function prepararVistaAdmin(titulo, columnas) {
    const topbarTitle = document.querySelector(".topbar-title");
    if (topbarTitle) topbarTitle.innerHTML = `Panel de <span>${titulo}</span>`;
    document.querySelector(".table-toolbar")?.classList.add("d-none");
    document.querySelector(".admin-pagination")?.classList.add("d-none");
    document.querySelector(".admin-table thead tr").innerHTML = columnas
      .map((col) => `<th>${col}</th>`)
      .join("");
  }

  function activarLinkAdmin(linkActivo) {
    document
      .querySelectorAll(".sidebar-nav a")
      .forEach((link) => link.classList.remove("active"));
    if (linkActivo) linkActivo.classList.add("active");
  }

  async function renderReservasAdmin(link) {
    activarLinkAdmin(link);
    prepararVistaAdmin("Reservas", [
      "ID",
      "Cliente",
      "Email",
      "Producto",
      "Fecha reserva",
      "Devolucion",
      "Estado",
      "Acciones",
    ]);

    const reservas = await ReservaAPI.listar();
    document.getElementById("tableBody").innerHTML = (Array.isArray(reservas) ? reservas : [])
      .map(
        (r) => `
          <tr>
            <td>${r.id}</td>
            <td>${r.nombreCliente || ""}</td>
            <td>${r.emailCliente || ""}</td>
            <td>${r.producto?.titulo || ""}</td>
            <td>${r.fechaReserva || ""}</td>
            <td>${r.fechaDevolucion || ""}</td>
            <td><span class="status-badge ${String(r.estado || "").toLowerCase()}">${r.estado || ""}</span></td>
            <td>
              <div class="action-btns">
                ${r.estado === "PENDIENTE" ? `<button class="btn-edit" onclick="confirmarReservaAdmin(${r.id})">Confirmar</button>` : ""}
                ${r.estado !== "CANCELADA" ? `<button class="btn-delete" onclick="cancelarReservaAdmin(${r.id})">Cancelar</button>` : ""}
              </div>
            </td>
          </tr>
        `,
      )
      .join("");
  }

  async function renderUsuariosAdmin(link) {
    activarLinkAdmin(link);
    prepararVistaAdmin("Usuarios", ["ID", "Nombre", "Email", "Rol", "Acciones"]);

    const usuarios = await UsuarioAPI.listar();
    document.getElementById("tableBody").innerHTML = (Array.isArray(usuarios) ? usuarios : [])
      .map(
        (u) => `
          <tr>
            <td>${u.id}</td>
            <td>${u.nombre || ""}</td>
            <td>${u.email || ""}</td>
            <td><span class="status-badge ${String(u.rol || "").toLowerCase()}">${u.rol || ""}</span></td>
            <td>
              <div class="action-btns">
                ${u.rol === "USUARIO"
                  ? `<button class="btn-edit" onclick="cambiarRolAdmin(${u.id}, 'ADMIN')">Hacer Admin</button>`
                  : `<button class="btn-delete" onclick="cambiarRolAdmin(${u.id}, 'USUARIO')">Quitar Admin</button>`
                }
              </div>
            </td>
          </tr>
        `,
      )
      .join("");
  }

  window.cambiarRolAdmin = async function (id, rol) {
    await UsuarioAPI.cambiarRol(id, rol);
    await renderUsuariosAdmin(document.querySelector(".sidebar-nav a.active"));
  };

  window.confirmarReservaAdmin = async function (id) {
    await ReservaAPI.confirmar(id);
    await renderReservasAdmin(document.querySelector(".sidebar-nav a.active"));
  };

  window.cancelarReservaAdmin = async function (id) {
    await ReservaAPI.cancelar(id);
    await renderReservasAdmin(document.querySelector(".sidebar-nav a.active"));
  };

  document.querySelectorAll(".sidebar-nav a").forEach((link) => {
    const texto = link.textContent;
    if (texto.includes("Reservas")) {
      link.addEventListener("click", (e) => {
        e.preventDefault();
        renderReservasAdmin(link).catch((error) =>
          showToast("danger", "Error", "No se pudieron cargar reservas: " + error.message),
        );
      });
    }
    if (texto.includes("Usuarios")) {
      link.addEventListener("click", (e) => {
        e.preventDefault();
        renderUsuariosAdmin(link).catch((error) =>
          showToast("danger", "Error", "No se pudieron cargar usuarios: " + error.message),
        );
      });
    }
  });

  // Iniciar admin
  cargarProductos();
}

// ==========================================
// 1. REGISTRO DE USUARIO — llama al backend
// ==========================================
const registerForm = document.getElementById("registerForm");
if (registerForm) {
  registerForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    const nombre = document.getElementById("regNombre").value.trim();
    const apellido = document.getElementById("regApellido").value.trim();
    const email = document.getElementById("regEmail").value.trim();
    const password = document.getElementById("regPassword").value.trim();

    const errorMsg = document.getElementById("registerError");
    const successMsg = document.getElementById("successMessage");

    try {
      await AuthAPI.registro({
        nombre: nombre + " " + apellido,
        email,
        password,
      });

      // Registro exitoso: mostrar mensaje y cerrar modal
      if (successMsg) successMsg.classList.remove("d-none");
      registerForm.reset();

      setTimeout(() => {
        const modal = bootstrap.Modal.getInstance(
          document.getElementById("registerModal"),
        );
        modal.hide();
        if (successMsg) successMsg.classList.add("d-none");
      }, 2000);
    } catch (err) {
      // El backend lanza "Email ya existe" cuando el correo está duplicado
      if (errorMsg) {
        errorMsg.textContent = err.message.includes("ya existe")
          ? "Este correo ya está registrado."
          : "Error al registrar. Intenta de nuevo.";
        errorMsg.classList.remove("d-none");
      }
    }
  });
}

// ==========================================
// 2. LOGIN DE USUARIO — llama al backend
// ==========================================
const loginForm = document.getElementById("loginForm");
if (loginForm) {
  loginForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    const email = document.getElementById("loginEmail").value.trim();
    const password = document.getElementById("loginPassword").value.trim();
    const errorMsg = document.getElementById("loginError");

    try {
      const usuario = await AuthAPI.login({ email, password });

      // Guardamos solo datos no sensibles en sessionStorage
      sessionStorage.setItem(
        "usuarioActivo",
        JSON.stringify({
          id: usuario.id,
          nombre: usuario.nombre,
          email: usuario.email,
          rol: usuario.rol,
        }),
      );

      const modal = bootstrap.Modal.getInstance(
        document.getElementById("loginModal"),
      );
      modal.hide();
      loginForm.reset();
      if (errorMsg) errorMsg.classList.add("d-none");

      actualizarInterfaz();
      await cargarFavoritosUsuario();

      // Si es admin, redirigir al panel
      if (usuario.rol === "ADMIN") {
        window.location.href = "admin.html";
      }
    } catch (err) {
      if (errorMsg) {
        errorMsg.textContent = "Correo o contraseña incorrectos.";
        errorMsg.classList.remove("d-none");
      }
    }
  });
}

// ==========================================
// 3. CAMBIO DE LOS BOTONES POR EL PERFIL
// ==========================================
function actualizarInterfaz() {
  const sesion = JSON.parse(sessionStorage.getItem("usuarioActivo"));

  const botonesAuth = document.getElementById("navAuthButtons");
  const perfilUsuario = document.getElementById("navUserProfile");
  const nombreTxt = document.getElementById("userNameDisplay");
  const avatarCirculo = document.getElementById("userAvatarInitials");

  const btnAdmin = document.getElementById("btnPanelAdmin");

  if (sesion) {
    if (botonesAuth) botonesAuth.classList.add("d-none");
    if (!botonesAuth) {
      document
        .querySelectorAll(".btn-login")
        .forEach((btn) => btn.classList.add("d-none"));
    }
    if (perfilUsuario) perfilUsuario.classList.remove("d-none");

    if (nombreTxt) nombreTxt.textContent = sesion.nombre;

    if (avatarCirculo) {
      const partes = sesion.nombre.split(" ");
      const iniciales =
        (partes[0]?.charAt(0) ?? "") + (partes[1]?.charAt(0) ?? "");
      avatarCirculo.textContent = iniciales.toUpperCase();
    }

    if (btnAdmin) {
      if (sesion.rol === "ADMIN") btnAdmin.classList.remove("d-none");
      else btnAdmin.classList.add("d-none");
    }
  } else {
    if (botonesAuth) botonesAuth.classList.remove("d-none");
    if (!botonesAuth) {
      document
        .querySelectorAll(".btn-login")
        .forEach((btn) => btn.classList.remove("d-none"));
    }
    if (perfilUsuario) perfilUsuario.classList.add("d-none");
    if (btnAdmin) btnAdmin.classList.add("d-none");
  }
}

// ==========================================
// 4. CERRAR SESIÓN
// ==========================================
window.cerrarSesion = async function () {
  try {
    await AuthAPI.logout();
  } catch (_) {
    // El backend solo responde con un mensaje, no importa si falla
  }
  sessionStorage.removeItem("usuarioActivo");
  actualizarInterfaz();
  await cargarFavoritosUsuario();
};

// Al cargar la página, verificar si ya hay sesión
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

  categoryForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    const nombreCategoria = document
      .getElementById("categoryNameInput")
      .value.trim();

    if (!nombreCategoria) return;

    try {
      await CategoriaAPI.crear({
        nombre: nombreCategoria,
        emoji: "🎮",
        cantidadJuegos: 0,
      });

      categoryForm.reset();

      const modal = bootstrap.Modal.getInstance(
        document.getElementById("addCategoryModal"),
      );
      modal.hide();

      if (typeof window.cargarDatosRealesHome === "function") {
        await window.cargarDatosRealesHome();
      }
    } catch (error) {
      console.error("Error creando categoria:", error);
    }
  });
});

/* DISPONIBILIDAD DE JUEGOS */

function pintarDisponibilidadDesdeDatos(root = document) {
  root.querySelectorAll(".game-card[data-stock]").forEach((card) => {
    const stock = Number(card.dataset.stock || 0);
    const estado = stock > 0 ? "disponible" : "agotado";
    const thumb = card.querySelector(".game-thumbnail");
    if (!thumb || thumb.querySelector(".catalog-status-badge")) return;

    const badge = document.createElement("div");
    badge.className = `status-badge ${estado} catalog-status-badge`;
    badge.textContent = stock > 0 ? "Disponible" : "Agotado";
    thumb.style.position = "relative";
    thumb.appendChild(badge);
  });
}

document.addEventListener("DOMContentLoaded", () => pintarDisponibilidadDesdeDatos());

/* =========================================
   MARCAR / ELIMINAR JUEGOS FAVORITOS
========================================= */

let favoritos = [];

if (false) document.querySelectorAll(".game-card").forEach((card) => {
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

async function cargarFavoritosUsuario() {
  const grid = document.getElementById("favoritesGrid");
  if (!grid) return;

  const sesion = JSON.parse(sessionStorage.getItem("usuarioActivo") || "null");
  if (!sesion?.id) {
    grid.innerHTML = "<p style='color:gray;'>Inicia sesion para ver tu lista.</p>";
    return;
  }

  try {
    const respuesta = await FavoritoAPI.listar();
    favoritos = (Array.isArray(respuesta) ? respuesta : []).filter(
      (favorito) => String(favorito.usuario?.id) === String(sesion.id),
    );
    renderFavoritosBackend();
  } catch (error) {
    grid.innerHTML = "<p style='color:gray;'>No se pudieron cargar tus favoritos.</p>";
    console.error("Error cargando favoritos:", error);
  }
}

function renderFavoritosBackend() {
  const grid = document.getElementById("favoritesGrid");
  if (!grid) return;

  if (favoritos.length === 0) {
    grid.innerHTML = "<p style='color:gray;'>Aun no tienes favoritos.</p>";
    return;
  }

  grid.innerHTML = favoritos
    .map((favorito) => productoCard(favorito.producto || {}))
    .join("");
}

document.addEventListener("DOMContentLoaded", cargarFavoritosUsuario);
