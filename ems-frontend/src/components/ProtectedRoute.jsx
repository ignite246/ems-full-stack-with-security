/**
 * So logging out only removed the session, but didn't stop React Router from rendering the page.
 * 
 * Why is this called a Higher-Order Component?

Technically, this pattern is a wrapper component because it wraps other components and decides whether to render them.

It acts like a security guard:
 */

import { isUserLoggedIn } from "../services/AuthService";
import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children }) => {
    const isAuthenticated = isUserLoggedIn();

    return isAuthenticated
        ? children
        : <Navigate to="/login" replace />;
};

export default ProtectedRoute;