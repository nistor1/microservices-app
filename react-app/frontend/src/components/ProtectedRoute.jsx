// components/ProtectedRoute.js
import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const ProtectedRoute = ({ roles, component: Component, redirectTo = "/login" }) => {
    const { user, logout } = useAuth();

    // Check if user is authenticated
    if (!user || !user.logged) {
        console.log('User is not authenticated');
        return <Navigate to="/login" />;
    }

    // Check if user has at least one of the required roles
    const hasRequiredRole = roles.some(role => user.role.includes(role));

    if (!hasRequiredRole) {
        console.log('User does not have permission to access this route');
        logout(); // Log out the user if they don’t have the required role
        return <Navigate to="/login" />;
    }

    return <Component />;
};

export default ProtectedRoute;
