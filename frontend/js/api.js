// ════════════════════════════════════════════════════════════
//  api.js — Comunicación con el backend REST
//  Base: http://localhost:8080/api
// ════════════════════════════════════════════════════════════

const API_ROOT = "http://localhost:8080";
const API_BASE = `${API_ROOT}/api`;

// ── Función base ─────────────────────────────────────────────
async function apiFetch(path, options = {}) {
  const res = await fetch(API_BASE + path, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });
  if (res.status === 204) return null;
  if (!res.ok) {
    const msg = await res.text().catch(() => res.statusText);
    throw new Error(msg || `Error ${res.status}`);
  }
  return res.json();
}

async function rootFetch(path, options = {}) {
  const res = await fetch(API_ROOT + path, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });
  if (res.status === 204) return null;
  if (!res.ok) {
    const msg = await res.text().catch(() => res.statusText);
    throw new Error(msg || `Error ${res.status}`);
  }
  const type = res.headers.get("content-type") || "";
  return type.includes("application/json") ? res.json() : res.text();
}

// ── Productos ─────────────────────────────────────────────────
// Campos del backend: id, titulo, plataforma, genero, precio,
//                     stock, estado, rating, emoji, politicas,
//                     imagenUrl, totalVotos, sumaRatings
const ProductoAPI = {
  listar:   ()          => apiFetch("/productos"),
  obtener:  (id)        => apiFetch(`/productos/${id}`),
  crear:    (data)      => apiFetch("/productos",      { method: "POST",   body: JSON.stringify(data) }),
  actualizar:(id, data) => apiFetch(`/productos/${id}`,{ method: "PUT",    body: JSON.stringify(data) }),
  eliminar: (id)        => apiFetch(`/productos/${id}`,{ method: "DELETE" }),
  importar:  (data)      => apiFetch("/productos/importarRawg",  { method: "POST",   body: JSON.stringify(data) }),
  caracteristicas: (id) => apiFetch(`/productos/${id}/caracteristicas`),
  politicas: (id)       => apiFetch(`/productos/${id}/politicas`),
  compartir: (id)       => apiFetch(`/productos/${id}/compartir`),
  puntuar: (id, puntuacion) =>
    apiFetch(`/productos/${id}/puntuar`, { method: "POST", body: JSON.stringify({ puntuacion }) }),
};

// ── Categorías ────────────────────────────────────────────────
// Campos del backend: id, nombre, emoji, cantidadJuegos
const CategoriaAPI = {
  listar:    ()          => apiFetch("/categorias"),
  obtener:   (id)        => apiFetch(`/categorias/${id}`),
  crear:     (data)      => apiFetch("/categorias",      { method: "POST",   body: JSON.stringify(data) }),
  actualizar:(id, data)  => apiFetch(`/categorias/${id}`,{ method: "PUT",    body: JSON.stringify(data) }),
  eliminar:  (id)        => apiFetch(`/categorias/${id}`,{ method: "DELETE" }),
};

// ── Características ───────────────────────────────────────────
// Campos del backend: id, clave, valor, productoId
const ReservaAPI = {
  listar: () => apiFetch("/reservas"),
  obtener: (id) => apiFetch(`/reservas/${id}`),
  porEmail: (email) => apiFetch(`/reservas/email/${encodeURIComponent(email)}`),
  porProducto: (productoId) => apiFetch(`/reservas/producto/${productoId}`),
  crear: (data) => apiFetch("/reservas", { method: "POST", body: JSON.stringify(data) }),
  confirmar: (id) => apiFetch(`/reservas/${id}/confirmar`, { method: "PATCH" }),
  cancelar: (id) => apiFetch(`/reservas/${id}/cancelar`, { method: "PATCH" }),
  eliminar: (id) => apiFetch(`/reservas/${id}`, { method: "DELETE" }),
};

const CaracteristicaAPI = {
  listar:    ()          => apiFetch("/caracteristicas"),
  obtener:   (id)        => apiFetch(`/caracteristicas/${id}`),
  crear:     (data)      => apiFetch("/caracteristicas",      { method: "POST",   body: JSON.stringify(data) }),
  actualizar:(id, data)  => apiFetch(`/caracteristicas/${id}`,{ method: "PUT",    body: JSON.stringify(data) }),
  eliminar:  (id)        => apiFetch(`/caracteristicas/${id}`,{ method: "DELETE" }),
};

// ── Autenticación ─────────────────────────────────────────────
// Campos de respuesta: id, email, nombre, rol
const AuthAPI = {
  registro: (data) =>
    apiFetch("/auth/registro", { method: "POST", body: JSON.stringify(data) }),

  login: (data) =>
    apiFetch("/auth/login", { method: "POST", body: JSON.stringify(data) }),

  logout: () =>
    apiFetch("/auth/logout", { method: "POST" }),
};

const FavoritoAPI = {
  listar: () => rootFetch("/favoritos"),
  crear: (data) => rootFetch("/favoritos/crear", { method: "POST", body: JSON.stringify(data) }),
};

const DisponibilidadAPI = {
  verificar: (id) => rootFetch(`/disponibilidad/${id}`),
  cambiar: (id, disponible) =>
    rootFetch(`/disponibilidad/${id}?disponible=${Boolean(disponible)}`, { method: "PUT" }),
};

const UsuarioAPI = {
  listar: () => rootFetch("/usuarios"),
  obtener: (id) => rootFetch(`/usuarios/${id}`),
};
