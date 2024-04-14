/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}",],
  theme: {
    screens: {
      sm: '480px',
      md: '706px',
      lg: '1189px',
      xl: '1440px',
    },
    extend: {
      colors: {
        'text-color': '#05060A',
        'background-color': '#F9FAFC',
        'primary': '#5D66B5',
        'secondary': '#C6D099',
        'accent': '#78C18C',
      },
    },
  },
  plugins: [],
}

