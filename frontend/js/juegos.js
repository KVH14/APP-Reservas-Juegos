// ════════════════════════════════════════════════════════════
//  juegos.js — Lista de juegos compartida
//  Importar en: catalogo.html, product-detail.html, admin.html
//  HU-36: los datos vienen del backend via GET /api/productos
// ════════════════════════════════════════════════════════════

const juegos = [];

const bgPorPlataforma = {
  PS5:    "game-thumb-purple",
  Xbox:   "game-thumb-red",
  Switch: "game-thumb-green",
  PC:     "game-thumb-blue",
};

async function cargarJuegosAPI() {
  try {
    const respuesta = await ProductoAPI.listar();
    const data = Array.isArray(respuesta) ? respuesta : respuesta?.contenido || [];

    juegos.length = 0; // limpiar antes de llenar

    data.forEach(p => {
      juegos.push({
        id:          p.id,
        titulo:      p.titulo,
        genero:      p.genero      ?? "Accion",
        plataforma:  p.plataforma  ?? "PC",
        precio:      p.precio      ?? 0,
        rating:      p.rating      ?? 0,
        stock:       p.stock       ?? 0,
        estado:      p.stock === 0 ? "agotado" : (p.estado ?? "disponible"),
        emoji:       p.emoji       ?? "🎮",
        imagenUrl:   p.imagenUrl   ?? "",
        bg:          bgPorPlataforma[p.plataforma] ?? "game-thumb-blue",
      });
    });

  } catch (error) {
    console.error("Error cargando productos del backend:", error);
  }
}
