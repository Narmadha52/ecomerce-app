import React, { useContext } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import toast from 'react-hot-toast';

/**
 * Component to protect routes based on authentication status and user roles.
 * @param {string} allowedRoles - Comma-separated string of roles (e.g., "ROLE_ADMIN,ROLE_USER").
 */
const ProtectedRoute = ({ allowedRoles }) => {
    const { currentUser } = useContext(AuthContext);

    // Convert allowedRoles string to an array for easy checking
    const roles = allowedRoles ? allowedRoles.split(',').map(role => role.trim()) : [];

    // --- 1. Check Authentication ---
    if (!currentUser) {
        // User is not logged in, show toast and redirect to login page.
        toast.error("Please log in to view this page.");
        return <Navigate to="/login" replace />;
    }

    // --- 2. Check Role Authorization ---
    if (roles.length > 0) {
        // Check if the user has AT LEAST ONE of the allowed roles
        const userHasRequiredRole = currentUser.roles.some(userRole =>
            roles.includes(userRole)
        );

        if (!userHasRequiredRole) {
            // User is logged in but does not have the necessary role (e.g., non-admin user trying to access /admin)
            toast.error("Access denied. You do not have the required permissions.");
            // Redirect to the home page or a specific access denied page
            return <Navigate to="/" replace />;
        }
    }

    // --- 3. Allow Access ---
    // If authenticated and authorized, render the child routes/elements.
    return <Outlet />;
};

export default ProtectedRoute;
