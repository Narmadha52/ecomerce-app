import React, { useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import { useCart } from '../../context/CartContext';
import AuthService from '../../api/AuthService';
import toast from 'react-hot-toast';

const Header = () => {
  const { currentUser, setCurrentUser } = useContext(AuthContext);
  const { totalItems } = useCart(); // Access total items for cart icon badge
  const navigate = useNavigate();

  const handleLogout = () => {
    AuthService.logout();
    setCurrentUser(null);
    toast.success("You have been logged out.");
    navigate('/');
    // Force page reload to ensure full state reset and proper routing:
    window.location.reload();
  };

  const isAdmin = currentUser && currentUser.roles.includes('ROLE_ADMIN');

  return (
    <header className="navbar">
      <div className="container">
        {/* Left Side: Logo and Main Links */}
        <div className="flex items-center space-x-6">
          <Link to="/" className="text-2xl font-bold hover:bg-transparent p-0">
            E-Shop
          </Link>
          <Link to="/products" className="hidden sm:inline-block">Products</Link>
        </div>

        {/* Right Side: Auth, Cart, and Admin Links */}
        <nav className="flex items-center space-x-4">

          {/* Cart Link */}
          <Link to="/cart" className="relative">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z"></path></svg>
            {totalItems > 0 && (
              <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs font-bold rounded-full h-5 w-5 flex items-center justify-center">
                {totalItems}
              </span>
            )}
          </Link>

          {currentUser ? (
            // Authenticated User Menu (Visible for logged-in users)
            <div className="flex items-center space-x-4">

              {/* Admin Dashboard Link (Conditional) */}
              {isAdmin && (
                <Link to="/admin" className="bg-red-500 hover:bg-red-600 px-3 py-1.5 font-bold">
                  Admin
                </Link>
              )}

              {/* Profile and Orders Links */}
              <Link to="/profile" className="hidden sm:inline-block">
                Profile
              </Link>
              <Link to="/orders" className="hidden sm:inline-block">
                Orders
              </Link>

              <button onClick={handleLogout}>
                Logout
              </button>
            </div>
          ) : (
            // Guest User Menu (Visible for logged-out users)
            <div className="flex items-center space-x-2">
              <Link to="/login" className="px-3 py-1.5">Login</Link>
              <Link to="/register" className="bg-white text-indigo-600 hover:bg-gray-100 px-3 py-1.5 rounded-lg font-bold transition-colors">Register</Link>
            </div>
          )}
        </nav>
      </div>
    </header>
  );
};

export default Header;
