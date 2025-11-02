import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // This is the proxy configuration
    proxy: {
      // Proxy requests starting with /api (e.g., /api/auth/register)
      '/api': {
        // Target is your running Spring Boot backend
        target: 'http://localhost:8080',
        changeOrigin: true,
        // Rewrite the path so the backend only sees the actual API path (e.g., /auth/register)
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  },
})