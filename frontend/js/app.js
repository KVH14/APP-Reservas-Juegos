// ════════════════════════════════════════════════════════════
//  app.js — JavaScript general de Playres
// ════════════════════════════════════════════════════════════

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

// ════════════════════════════════════════════════════════════
//  3. PRODUCT DETAIL
// (todo tu código de product detail igual, sin cambios)
// ════════════════════════════════════════════════════════════

// ════════════════════════════════════════════════════════════
//  4. ADMIN PANEL
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

  async function cargarProductos() {
    try {
      products = await ProductoAPI.listar();
      filteredProds = [...products];
      renderTable();
    } catch (e) {
      showToast("danger", "❌", "No se pudo conectar al backend: " + e.message);
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
          <td><span class="platform-tbl">${p.plataforma || p.platform}</span></td>
          <td>${p.genero || p.genre}</td>
          <td class="tbl-price">$${(p.precio || p.price).toLocaleString("es-CO")}</td>
          <td><span class="tbl-rating-num">${p.rating}</span></td>
          <td><span class="status-badge ${p.estado || p.status}">${(p.estado || p.status)}</span></td>
          <td>${p.stock}</td>
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
        (!q || (p.titulo || p.title).toLowerCase().includes(q)) &&
        (!plt || (p.plataforma || p.platform) === plt) &&
        (!sts || (p.estado || p.status) === sts),
    );
    currentPage = 1;
    renderTable();
  };

  window.openAddModal = function () {
    document.getElementById("modalTitle").textContent = "➕ Agregar producto";
    document.getElementById("editIndex").value = -1;
    ["fTitle", "fPrice", "fStock", "fRating"].forEach(id => document.getElementById(id).value = "");
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
    document.getElementById("fTitle").value = p.titulo || p.title;
    document.getElementById("fPrice").value = p.precio || p.price;
    document.getElementById("fStock").value = p.stock;
    document.getElementById("fRating").value = p.rating;
    document.getElementById("fPlatform").value = p.plataforma || p.platform;
    document.getElementById("fGenre").value = p.genero || p.genre;
    document.getElementById("fStatus").value = p.estado || p.status;
    document.getElementById("fEmoji").value = p.emoji;
    new bootstrap.Modal(document.getElementById("productModal")).show();
  };

  window.saveProduct = async function () {
    const idx = parseInt(document.getElementById("editIndex").value);
    const producto = {
      titulo: document.getElementById("fTitle").value.trim(),
      precio: parseFloat(document.getElementById("fPrice").value),
      stock: parseInt(document.getElementById("fStock").value),
      rating: parseFloat(document.getElementById("fRating").value),
      plataforma: document.getElementById("fPlatform").value,
      genero: document.getElementById("fGenre").value,
      estado: document.getElementById("fStatus").value,
      emoji: document.getElementById("fEmoji").value
    };

    if (idx >= 0) {
      await ProductoAPI.actualizar(products[idx].id, producto);
      showToast("success", "✅", "Producto actualizado.");
    } else {
      await ProductoAPI.crear(producto);
      showToast("success", "✅", "Producto agregado.");
    }
    await cargarProductos();
    bootstrap.Modal.getInstance(document.getElementById("productModal")).hide();
  };

  window.openDeleteModal = function (idx) {
    deleteIndex = idx;
    document.getElementById("deleteGameName").textContent = '"' + (products[idx].titulo || products[idx].title) + '"';
    new bootstrap.Modal(document.getElementById("deleteModal")).show();
  };

  window.confirmDelete = async function () {
    if (deleteIndex < 0) return;
    const id = products[deleteIndex].id;
    const name = products[deleteIndex].titulo || products[deleteIndex].title;
    await ProductoAPI.eliminar(id);
    deleteIndex = -1;
    await cargarProductos();
    bootstrap.Modal.getInstance(document.getElementById("deleteModal")).hide();
    showToast("danger", "🗑️", `"${name}" eliminado.`);
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

  // Iniciar admin cargando productos reales del backend
  cargarProductos();
}
