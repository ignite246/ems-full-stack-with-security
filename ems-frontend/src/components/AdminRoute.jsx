import { Navigate } from "react-router-dom";
import { isUserLoggedIn, isLoggedInUserAdmin } from "../services/AuthService";

const AdminRoute = ({ children }) => {

    if (!isUserLoggedIn()) {
        return <Navigate to="/login" replace />;
    }

    if (!isLoggedInUserAdmin()) {
        return <Navigate to="/employees" replace />;
    }

    return children;
};

export default AdminRoute;