import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Toaster } from 'react-hot-toast'; // Global toast notifications

// --- Contexts ---
import { AuthProvider } from './context/AuthContext';
import { CartProvider } from './context/CartContext';

// --- Layout & Components ---
import Navbar from './components/layout/Navbar';
import Footer from './components/layout/Footer';
import ProtectedRoute from './components/auth/ProtectedRoute';

// --- Page Imports ---
// Public
import HomePage from './pages/home/HomePage';
import ProductListPage from './pages/product/ProductListPage';
import ProductDetailPage from './pages/product/ProductDetailPage';
import CartPage from './pages/cart/CartPage';

// Auth
import LoginPage from './pages/auth/LoginPage';
import RegisterPage from './pages/auth/RegisterPage';

// Authenticated
import CheckoutPage from './pages/order/CheckoutPage';
import OrderSuccessPage from './pages/order/OrderSuccessPage';
import OrderHistoryPage from './pages/order/OrderHistoryPage';
import ProfilePage from './pages/user/ProfilePage';

// Admin
import AdminDashboard from './pages/admin/AdminDashboard';


const App = () => {
    return (
        // Global Providers (must wrap Router)
        <AuthProvider>
            <CartProvider>
                <Router>
                    {/* Toaster component for global notifications */}
                    <Toaster position="top-right" reverseOrder={false} />

                    <div className="flex flex-col min-h-screen">
                        <Navbar />

                        <main className="flex-grow p-4 bg-gray-50">
                            <Routes>
                                {/* --- Public Routes --- */}
                                <Route path="/" element={<HomePage />} />
                                <Route path="/products" element={<ProductListPage />} />
                                <Route path="/products/:id" element={<ProductDetailPage />} />
                                <Route path="/cart" element={<CartPage />} />

                                {/* --- Authentication Routes --- */}
                                <Route path="/login" element={<LoginPage />} />
                                <Route path="/register" element={<RegisterPage />} />

                                {/* --- Protected Routes (Requires Login) --- */}
                                <Route
                                    path="/checkout"
                                    element={<ProtectedRoute><CheckoutPage /></ProtectedRoute>}
                                />
                                <Route
                                    path="/order-success"
                                    element={<ProtectedRoute><OrderSuccessPage /></ProtectedRoute>}
                                />
                                <Route
                                    path="/orders"
                                    element={<ProtectedRoute><OrderHistoryPage /></ProtectedRoute>}
                                />
                                <Route
                                    path="/profile"
                                    element={<ProtectedRoute><ProfilePage /></ProtectedRoute>}
                                />

                                {/* --- Admin Protected Route (Requires Login AND ADMIN Role) --- */}
                                <Route
                                    path="/admin"
                                    element={<ProtectedRoute requiredRole="ADMIN"><AdminDashboard /></ProtectedRoute>}
                                />

                                {/* --- Fallback Route (404) --- */}
                                <Route path="*" element={<h1 className="text-center py-20 text-4xl text-red-500">404 - Page Not Found</h1>} />
                            </Routes>
                        </main>

                        <Footer />
                    </div>
                </Router>
            </CartProvider>
        </AuthProvider>
    );
};

export default App;
