import { Navigate } from "react-router-dom";
import { isUserLoggedIn } from "../services/AuthService";

const PublicRoute = ({ children }) => {

    const isAuthenticated = isUserLoggedIn();

    return isAuthenticated
        ? <Navigate to="/employees" replace />
        : children;
};

export default PublicRoute;