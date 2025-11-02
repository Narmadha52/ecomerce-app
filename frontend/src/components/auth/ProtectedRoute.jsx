import React, { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import toast from 'react-hot-toast';

/**
 * A wrapper component to protect routes that require authentication or a specific role.
 * @param {object} children - The component to render if authentication passes.
 * @param {string} requiredRole - Optional role (e.g., 'ADMIN') needed for access.
 */
const ProtectedRoute = ({ children, requiredRole }) => {
    const { currentUser, loading } = useContext(AuthContext);

    // 1. Still loading authentication state
    if (loading) {
        // Return a simple div while the auth state is being determined
        return <div className="text-center py-20 text-xl text-indigo-600">Checking authentication...</div>;
    }

    // 2. Not logged in
    if (!currentUser) {
        toast.error("You must be logged in to view this page.");
        return <Navigate to="/login" replace />;
    }

    // 3. Logged in, but role check required (e.g., for Admin Dashboard)
    if (requiredRole && (!currentUser.roles || !currentUser.roles.includes(requiredRole))) {
        toast.error("Access denied. You do not have the required permissions.");
        return <Navigate to="/" replace />; // Redirect non-authorized users
    }

    // 4. Authentication and role check passed
    return children;
};

export default ProtectedRoute;
