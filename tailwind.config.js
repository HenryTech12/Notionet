// tailwind.config.js
module.exports = {
  content: [
    './src/main/resources/templates/**/*.html', // for Thymeleaf HTML files
    './src/main/resources/static/**/*.html'     // if you serve static HTML
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
