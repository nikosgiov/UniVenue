window.addEventListener("load", function () {
    document.querySelector(".headerLeft").classList.add("fadeIn");
    document.querySelector(".bannerImg").classList.add("fadeIn");
  });

const observer = new IntersectionObserver((entities) => {
    entities.forEach((entry) => {
      if (entry.isIntersecting) entry.target.classList.add('show');
      else entry.target.classList.remove('show');
    });
});
  
const hiddenElements = document.querySelectorAll('.hidden3');
hiddenElements.forEach((el) => observer.observe(el));