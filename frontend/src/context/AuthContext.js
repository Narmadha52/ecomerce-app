import React, { createContext, useState, useEffect } from 'react';
import AuthService from '../api/AuthService';

// Create the Context object
export const AuthContext = createContext();

// Create the Provider component
export const AuthProvider = ({ children }) => {
    // State to hold the current user object (retrieved from localStorage)
    const [currentUser, setCurrentUser] = useState(AuthService.getCurrentUser());

    // Function to check and set the user (used on initial load and login)
    useEffect(() => {
        const user = AuthService.getCurrentUser();
        if (user) {
            setCurrentUser(user);
        }
    }, []);

    // Login function updates local storage and state
    const login = (username, password) => {
        return AuthService.login(username, password).then(user => {
            setCurrentUser(user);
            return user;
        });
    };

    // Logout function clears local storage and state
    const logout = () => {
        AuthService.logout();
        setCurrentUser(null);
    };

    // Register function (just calls the service, state update happens on subsequent login)
    const register = (username, email, password) => {
        return AuthService.register(username, email, password);
    };

    // Bundle the state and functions to be exposed to consumers
    const value = {
        currentUser,
        login,
        logout,
        register,
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};