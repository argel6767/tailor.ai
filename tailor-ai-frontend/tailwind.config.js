/** @type {import('tailwindcss').Config} */
export default {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      colors: {
        primary: '#0C7C59', // Surfie Green
        secondary: '#58B19F', // Aquamarine
        accent: '#A8A8A8', // Muted Gray
        background: '#1C1C1C', // Dark Background
      },
      fontFamily: {
        sans: ['Arial', 'sans-serif'],
      },
    },
  },
  daisyui: {
    themes: [
      {
        mytheme: {
          primary: '#0C7C59',
          secondary: '#58B19F',
          accent: '#A8A8A8',
          neutral: '#1C1C1C',
          'base-100': '#1C1C1C', // For default background in DaisyUI
        },
      },
    ],
  },
  plugins: [require('daisyui')],
}

