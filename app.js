
// ════════════════════════════════════════════════════════════
//  app.js — JavaScript general de Playres
//  Secciones:
//    1. NAVBAR scroll
//    2. CATÁLOGO — filtros, galería, paginación
//    3. PRODUCT DETAIL — galería, días, tabs
//    4. ADMIN — tabla, modales, CRUD
// ════════════════════════════════════════════════════════════


// ── 1. NAVBAR SCROLL ─────────────────────────────────────────
const navbar = document.querySelector('.navbar-playres');
if (navbar) {
  window.addEventListener('scroll', () => {
    navbar.classList.toggle('scrolled', window.scrollY > 30);
  });
}


// ════════════════════════════════════════════════════════════
//  2. CATÁLOGO (solo se ejecuta si existe #gamesGrid)
// ════════════════════════════════════════════════════════════
if (document.getElementById('gamesGrid')) {

  let filteredGames = [...juegos];
  let currentPage   = 1;
  const perPage     = 8;
  let activeGenre   = '';
  let activePlatform = '';

  // Filtrar juegos
  window.setFilter = function(type, value, el) {
    if (type === 'genre') {
      activeGenre = value;
      document.querySelectorAll('.genre-filter-tag').forEach(t => t.classList.remove('active'));
    } else {
      activePlatform = value;
      document.querySelectorAll('.platform-filter-tag').forEach(t => t.classList.remove('active'));
    }
    if (el) el.classList.add('active');
    filterGames();
  };

  window.filterGames = function() {
    const q = document.getElementById('searchInput')
      ? document.getElementById('searchInput').value.toLowerCase()
      : '';
    filteredGames = juegos.filter(g =>
      (!q              || g.title.toLowerCase().includes(q) || g.genre.toLowerCase().includes(q) || g.platform.toLowerCase().includes(q)) &&
      (!activeGenre    || g.genre    === activeGenre) &&
      (!activePlatform || g.platform === activePlatform)
    );
    currentPage = 1;
    renderGames();
  };

  function renderGames() {
    const start = (currentPage - 1) * perPage;
    const slice = filteredGames.slice(start, start + perPage);
    const totalPages = Math.ceil(filteredGames.length / perPage);
    const grid = document.getElementById('gamesGrid');

    document.getElementById('resultCount').textContent =
      `Mostrando ${filteredGames.length} juego${filteredGames.length !== 1 ? 's' : ''}`;
    document.getElementById('pageIndicator').textContent =
      `Página ${currentPage} de ${totalPages || 1}`;

    grid.innerHTML = slice.length === 0
      ? `<div class="col-12 catalog-empty"><p>No se encontraron juegos con ese filtro.</p></div>`
      : slice.map(g => `
          <div class="col-lg-3 col-md-4 col-sm-6">
            <div class="game-card">
              <a href="product-detail.html" class="catalog-card-link">
                <div class="game-thumbnail ${g.bg}">
                  <div class="game-tags">
                    <span class="game-tag">${g.genre}</span>
                    ${g.tag ? `<span class="${g.tagClass}">+ ${g.tag}</span>` : ''}
                  </div>
                  ${g.emoji}
                </div>
              </a>
              <div class="game-card-info">
                <div class="game-platform">${g.platform}</div>
                <h3 class="game-title-catalog">${g.title}</h3>
                <div class="game-bottom">
                  <div class="game-price">$${g.price.toLocaleString('es-CO')}<span>/día</span></div>
                  <div><span class="game-stars">★★★★★</span><span class="game-rating">${g.rating}</span></div>
                </div>
                <a href="product-detail.html" class="catalog-btn-ver">Ver detalles →</a>
              </div>
            </div>
          </div>
        `).join('');

    // Paginación Bootstrap
    const pag = document.getElementById('pagination');
    pag.innerHTML = `
      <li class="page-item ${currentPage === 1 ? 'disabled' : ''}">
        <a class="page-link" href="#catalogo" onclick="goPage(${currentPage - 1})">←</a>
      </li>`;

    for (let i = 1; i <= totalPages; i++) {
      pag.innerHTML += `
        <li class="page-item ${i === currentPage ? 'active' : ''}">
          <a class="page-link" href="#catalogo" onclick="goPage(${i})">${i}</a>
        </li>`;
    }

    pag.innerHTML += `
      <li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
        <a class="page-link" href="#catalogo" onclick="goPage(${currentPage + 1})">→</a>
      </li>`;
  }

  window.goPage = function(n) {
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
if (document.getElementById('galleryMain')) {

  // Cambiar imagen galería
  window.changeGallery = function(el, emoji, c1, c2) {
    document.querySelectorAll('.gallery-thumb').forEach(t => t.classList.remove('active'));
    el.classList.add('active');
    document.getElementById('galleryEmoji').textContent = emoji;
    document.getElementById('galleryMain').style.background =
      `linear-gradient(135deg, ${c1}, ${c2})`;
  };

  // Selector de días y cálculo de total
  const PRICE = 9900;
  window.selectDays = function(el, days) {
    document.querySelectorAll('.day-btn').forEach(b => b.classList.remove('active'));
    el.classList.add('active');
    document.getElementById('totalDisplay').textContent =
      '$' + (PRICE * days).toLocaleString('es-CO');
  };

  // Cambiar tabs
  window.switchTab = function(el, tabId) {
    document.querySelectorAll('.detail-tab').forEach(t => t.classList.remove('active'));
    document.querySelectorAll('.tab-content-playres').forEach(t => t.classList.remove('active'));
    el.classList.add('active');
    document.getElementById(tabId).classList.add('active');
  };
}


// ════════════════════════════════════════════════════════════
//  4. ADMIN PANEL (solo si existe #tableBody)
// ════════════════════════════════════════════════════════════
if (document.getElementById('tableBody')) {

  // Copia local para poder agregar/editar/eliminar sin afectar el array original
  let products     = [...juegos];
  let filteredProds = [...products];
  let currentPage  = 1;
  const perPage    = 8;
  let deleteIndex  = -1;

  function renderTable() {
    const start = (currentPage - 1) * perPage;
    const slice = filteredProds.slice(start, start + perPage);
    const totalPages = Math.ceil(filteredProds.length / perPage);

    document.getElementById('tableBody').innerHTML = slice.map(p => {
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
          <td class="tbl-price">$${p.price.toLocaleString('es-CO')}</td>
          <td><span class="tbl-stars">${'★'.repeat(Math.floor(p.rating))}</span> <span class="tbl-rating-num">${p.rating}</span></td>
          <td><span class="status-badge ${p.status}">${p.status.charAt(0).toUpperCase() + p.status.slice(1)}</span></td>
          <td class="${p.stock === 0 ? 'tbl-stock-out' : 'tbl-stock'}">${p.stock}</td>
          <td>
            <div class="action-btns">
              <button class="btn-edit" onclick="openEditModal(${realIdx})">✏️ Editar</button>
              <button class="btn-delete" onclick="openDeleteModal(${realIdx})">🗑️</button>
            </div>
          </td>
        </tr>`;
    }).join('');

    // Paginación
    document.getElementById('prevPage').disabled  = currentPage === 1;
    document.getElementById('nextPage').disabled  = currentPage >= totalPages;
    document.getElementById('page1').classList.toggle('active', currentPage === 1);
    document.getElementById('page2').classList.toggle('active', currentPage === 2);
    document.getElementById('page2').style.display = totalPages < 2 ? 'none' : 'flex';

    const from = start + 1;
    const to   = Math.min(start + perPage, filteredProds.length);
    document.getElementById('pagInfo').textContent =
      `Mostrando ${from}–${to} de ${filteredProds.length} productos`;
    document.getElementById('resultCount').textContent =
      `Mostrando ${filteredProds.length} producto${filteredProds.length !== 1 ? 's' : ''}`;

    // Stats
    document.getElementById('totalCount').textContent    = products.length;
    document.getElementById('availCount').textContent    = products.filter(p => p.status === 'disponible').length;
    document.getElementById('reservedCount').textContent = products.filter(p => p.status === 'reservado').length;
    document.getElementById('outCount').textContent      = products.filter(p => p.status === 'agotado').length;
  }

  window.filterTable = function() {
    const q   = document.getElementById('searchInput').value.toLowerCase();
    const plt = document.getElementById('platformFilter').value;
    const sts = document.getElementById('statusFilter').value;
    filteredProds = products.filter(p =>
      (!q   || p.title.toLowerCase().includes(q) || p.id.toLowerCase().includes(q)) &&
      (!plt || p.platform === plt) &&
      (!sts || p.status   === sts)
    );
    currentPage = 1;
    renderTable();
  };

  window.changePage = function(dir) { currentPage += dir; renderTable(); };
  window.goPage     = function(n)   { currentPage = n;   renderTable(); };

  window.openAddModal = function() {
    document.getElementById('modalTitle').textContent = '➕ Agregar producto';
    document.getElementById('editIndex').value = -1;
    ['fTitle','fPrice','fStock','fRating'].forEach(id => document.getElementById(id).value = '');
    document.getElementById('fPlatform').value = 'PS5';
    document.getElementById('fGenre').value    = 'RPG';
    document.getElementById('fStatus').value   = 'disponible';
    document.getElementById('fEmoji').value    = '🌌';
    new bootstrap.Modal(document.getElementById('productModal')).show();
  };

  window.openEditModal = function(idx) {
    const p = products[idx];
    document.getElementById('modalTitle').textContent = '✏️ Editar producto';
    document.getElementById('editIndex').value  = idx;
    document.getElementById('fTitle').value     = p.title;
    document.getElementById('fPrice').value     = p.price;
    document.getElementById('fStock').value     = p.stock;
    document.getElementById('fRating').value    = p.rating;
    document.getElementById('fPlatform').value  = p.platform;
    document.getElementById('fGenre').value     = p.genre;
    document.getElementById('fStatus').value    = p.status;
    document.getElementById('fEmoji').value     = p.emoji;
    new bootstrap.Modal(document.getElementById('productModal')).show();
  };

  window.saveProduct = function() {
    const idx   = parseInt(document.getElementById('editIndex').value);
    const title = document.getElementById('fTitle').value.trim();
    const price = parseInt(document.getElementById('fPrice').value);
    if (!title || !price) { showToast('danger', '⚠️', 'Completa título y precio.'); return; }

    const bgMap = { PS5:'game-thumb-purple', Xbox:'game-thumb-red', Switch:'game-thumb-green', PC:'game-thumb-blue' };
    const platform = document.getElementById('fPlatform').value;

    const prod = {
      id:       idx >= 0 ? products[idx].id : 'PLY-' + String(products.length + 1).padStart(3, '0'),
      title, platform,
      genre:    document.getElementById('fGenre').value,
      price,
      rating:   parseFloat(document.getElementById('fRating').value) || 4.5,
      stock: document.getElementById('fStock').value !== '' ? parseInt(document.getElementById('fStock').value) : (idx >= 0 ? products[idx].stock : 0),
      status:   document.getElementById('fStatus').value,
      emoji:    document.getElementById('fEmoji').value,
      bg:       bgMap[platform] || 'game-thumb-purple',
      tag: '', tagClass: '', description: '',
    };

    if (idx >= 0) { products[idx] = prod; showToast('success', '✅', 'Producto actualizado.'); }
    else          { products.push(prod);  showToast('success', '✅', 'Producto agregado.'); }

    filteredProds = [...products];
    filterTable();
    bootstrap.Modal.getInstance(document.getElementById('productModal')).hide();
  };

  window.openDeleteModal = function(idx) {
    deleteIndex = idx;
    document.getElementById('deleteGameName').textContent = '"' + products[idx].title + '"';
    new bootstrap.Modal(document.getElementById('deleteModal')).show();
  };

  window.confirmDelete = function() {
    if (deleteIndex < 0) return;
    const name = products[deleteIndex].title;
    products.splice(deleteIndex, 1);
    deleteIndex  = -1;
    filteredProds = [...products];
    filterTable();
    bootstrap.Modal.getInstance(document.getElementById('deleteModal')).hide();
    showToast('danger', '🗑️', `"${name}" eliminado.`);
  };

  let toastTimer;
  function showToast(type, icon, msg) {
    const el = document.getElementById('toastEl');
    document.getElementById('toastIcon').textContent = icon;
    document.getElementById('toastMsg').textContent  = msg;
    el.className = `playres-toast ${type} show`;
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => el.classList.remove('show'), 3000);
  }

  // Iniciar admin
  renderTable();
}


// ===============================
// VALIDACIÓN FORMULARIO REGISTRO
// ===============================

document.addEventListener("DOMContentLoaded", function () {

  const form = document.getElementById("registerForm");

  if (!form) return;

  const nombre = document.getElementById("regNombre");
  const apellido = document.getElementById("regApellido");
  const email = document.getElementById("regEmail");
  const password = document.getElementById("regPassword");

  form.addEventListener("submit", function (e) {
    e.preventDefault();

    let valid = true;

    // Resetear estados
    [nombre, apellido, email, password].forEach(input => {
      input.classList.remove("is-invalid");
      input.classList.remove("is-valid");
    });

    // Validar nombre
    if (nombre.value.trim().length < 2) {
      nombre.classList.add("is-invalid");
      valid = false;
    } else {
      nombre.classList.add("is-valid");
    }

    // Validar apellido
    if (apellido.value.trim().length < 2) {
      apellido.classList.add("is-invalid");
      valid = false;
    } else {
      apellido.classList.add("is-valid");
    }

    // Validar email con regex
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email.value.trim())) {
      email.classList.add("is-invalid");
      valid = false;
    } else {
      email.classList.add("is-valid");
    }

    // Validar contraseña mínimo 6 caracteres
    if (password.value.trim().length < 6) {
      password.classList.add("is-invalid");
      valid = false;
    } else {
      password.classList.add("is-valid");
    }

    // 🔴 SOLUCIÓN: Solo ejecutamos el éxito si 'valid' se mantuvo en true
    if (valid) {
      
      // 1. Guardar usuario en localStorage (Movido aquí adentro)
      const userData = {
        nombre: nombre.value.trim(),
        apellido: apellido.value.trim(),
        email: email.value.trim(),
        password: password.value.trim()
      };
      localStorage.setItem("usuarioRegistrado", JSON.stringify(userData));

      // 2. Mostrar mensaje de éxito
      const successMessage = document.getElementById("successMessage");
      if (successMessage) successMessage.classList.remove("d-none");

      // 3. Esperar 1.5 segundos, luego limpiar y cerrar
      setTimeout(() => {
        if (successMessage) successMessage.classList.add("d-none");
        form.reset();

        [nombre, apellido, email, password].forEach(input => {
          input.classList.remove("is-valid");
        });

        // Asegurarnos de obtener la instancia correcta del modal de Bootstrap
        const modalEl = document.getElementById("registerModal");
        if (modalEl) {
          const modal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
          modal.hide();
        }

      }, 2000); 
    }
  });

  // ===============================
  // VALIDACIÓN EN TIEMPO REAL
  // ===============================

  [nombre, apellido, email, password].forEach(input => {
    input.addEventListener("input", function () {

      if (input.value.trim() === "") {
        input.classList.remove("is-valid");
        input.classList.remove("is-invalid");
        return;
      }

      if (input === email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (emailRegex.test(input.value.trim())) {
          input.classList.add("is-valid");
          input.classList.remove("is-invalid");
        } else {
          input.classList.add("is-invalid");
          input.classList.remove("is-valid");
        }
      } 
      else if (input === password) {
        if (input.value.trim().length >= 6) {
          input.classList.add("is-valid");
          input.classList.remove("is-invalid");
        } else {
          input.classList.add("is-invalid");
          input.classList.remove("is-valid");
        }
      } 
      else {
        if (input.value.trim().length >= 2) {
          input.classList.add("is-valid");
          input.classList.remove("is-invalid");
        } else {
          input.classList.add("is-invalid");
          input.classList.remove("is-valid");
        }
      }

    });
  });




// ===============================
// LOGIN DE USUARIO
// ===============================

const loginForm = document.getElementById("loginForm");

if (loginForm) {

  loginForm.addEventListener("submit", function (e) {
    e.preventDefault();

    const email = document.getElementById("loginEmail").value.trim();
    const password = document.getElementById("loginPassword").value.trim();
    const errorMessage = document.getElementById("loginError");

    const storedUser = JSON.parse(localStorage.getItem("usuarioRegistrado"));

    if (!storedUser) {
      errorMessage.textContent = "No existe ningún usuario registrado.";
      errorMessage.classList.remove("d-none");
      return;
    }

    if (email === storedUser.email && password === storedUser.password) {

      localStorage.setItem("usuarioActivo", JSON.stringify(storedUser));

      errorMessage.classList.add("d-none");

      const modal = bootstrap.Modal.getInstance(document.getElementById("loginModal"));
      modal.hide();

      actualizarNavbar();

    } else {
      errorMessage.textContent = "Correo o contraseña incorrectos.";
      errorMessage.classList.remove("d-none");
    }

  });

}



});