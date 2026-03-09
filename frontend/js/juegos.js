// ════════════════════════════════════════════════════════════
//  juegos.js — Lista de juegos compartida
//  Importar en: catalogo.html, product-detail.html, admin.html
// ════════════════════════════════════════════════════════════

const juegos = [];

async function cargarJuegosAPI() {

  const API_KEY = "400cc619f51f4b18b3c201a5670d7fdc";

  const url = `https://api.rawg.io/api/games?key=${API_KEY}&page_size=40`;

  try {

    const response = await fetch(url);
    const data = await response.json();

    data.results.forEach(game => {

  let generoAPI = game.genres.length ? game.genres[0].name : "";

  let genero = "Acción";

  if (generoAPI === "Action") genero = "Acción";
  if (generoAPI === "Adventure") genero = "Aventura";
  if (generoAPI === "Sports") genero = "Deportes";
  if (generoAPI === "Racing") genero = "Carreras";
  if (generoAPI === "RPG") genero = "RPG";
  if (generoAPI === "Shooter") genero = "Shooter";

  const plataformas = ["PS5", "Xbox", "Switch", "PC"];
  const plataformaRandom = plataformas[Math.floor(Math.random() * plataformas.length)];

  juegos.push({
    id: game.id,
    title: game.name,
    genre: genero,
    platform: plataformaRandom,
    price: (Math.random() * 50000) + 5000,
    rating: game.rating,
    stock: 50,
    status: "disponible",
    emoji: "🎮",
    bg: "game-thumb-blue",
    tag: "",
    tagClass: "",
    description: game.slug
  });

});

  } catch (error) {

    console.error("Error cargando API:", error);

  }

}