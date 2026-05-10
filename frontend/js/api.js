// ════════════════════════════════════════════════════════════
//  api.js — Comunicación con el backend REST
//  Base: http://localhost:8080/api
// ════════════════════════════════════════════════════════════

const API_BASE = "http://localhost:8080/api";

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
