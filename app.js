/*parte de funcion de el slider*/

(function () {
  const slider = document.querySelector('.testimonials-slider');
  const track  = document.querySelector('.testimonials-track');
  if (!slider || !track) return;

  // Clonar cards para loop infinito
  const cards = Array.from(track.children);
  cards.forEach(card => {
    const clone = card.cloneNode(true);
    clone.setAttribute('aria-hidden', 'true');
    track.appendChild(clone);
  });

  let startX    = 0;
  let offset    = 0;
  let isDragging = false;

  const gap       = 20;
  const cardWidth = () => track.children[0].offsetWidth + gap;
  const loopWidth = () => cardWidth() * cards.length;

  function normalize(x) {
    const lw = loopWidth();
    let norm = x % lw;
    if (norm > 0) norm -= lw;
    return norm;
  }

  track.style.transition = 'none';
  track.style.transform  = `translateX(${offset}px)`;

  // MOUSE
  slider.addEventListener('mousedown', e => {
    isDragging = true;
    startX = e.clientX - offset;
    track.style.transition = 'none';
    e.preventDefault();
  });

  window.addEventListener('mousemove', e => {
    if (!isDragging) return;
    offset = normalize(e.clientX - startX);
    track.style.transform = `translateX(${offset}px)`;
  });

  window.addEventListener('mouseup', () => {
    if (!isDragging) return;
    isDragging = false;
  });

  // TOUCH
  slider.addEventListener('touchstart', e => {
    isDragging = true;
    startX = e.touches[0].clientX - offset;
    track.style.transition = 'none';
  }, { passive: true });

  slider.addEventListener('touchmove', e => {
    offset = normalize(e.touches[0].clientX - startX);
    track.style.transform = `translateX(${offset}px)`;
  }, { passive: true });

  slider.addEventListener('touchend', () => {
    isDragging = false;
  });

})();