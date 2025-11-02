import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import './index.css';
import { BrowserRouter } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext.js';
import { CartProvider } from './context/CartContext.js'; // <-- Import CartProvider
import { Toaster } from 'react-hot-toast'; // <-- NEW IMPORT

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
      {/* AuthProvider must wrap CartProvider if cart logic depends on user data */}
      <AuthProvider>
        {/* Wrap the entire app with CartProvider */}
        <CartProvider>
            <App />
            <Toaster position="top-right" reverseOrder={false} /> {/* <-- ADD TOASTER */}
        </CartProvider>
      </AuthProvider>
    </BrowserRouter>
  </React.StrictMode>,
);