import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
console.log("AuthService::API_BASE_URL:", API_BASE_URL);

const AUTH_REST_API_BASE_URL = API_BASE_URL + "/auth";

export const registerUser = (registerObj) => axios.post(AUTH_REST_API_BASE_URL + "/register", registerObj);

export const loginUser = (loginObj) => axios.post(AUTH_REST_API_BASE_URL + "/login", loginObj);

export const storeTokenInLocalStorage = (token) => localStorage.setItem("token", token);

export const getTokenFromLocalStorage = () => localStorage.getItem("token");

export const saveLoggedInUserInSessionStorage = (usernameOrEmail, role) => {
    sessionStorage.setItem("authenticatedUser", usernameOrEmail);
    sessionStorage.setItem("role", role);
}

export const isUserLoggedIn = () => {
    const usernameOrEmail = sessionStorage.getItem("authenticatedUser");
    if (usernameOrEmail == null) {
        return false;
    }
    else {
        return true;
    }
}

export const getLoggedInUser = () => {
    const usernameOrEmail = sessionStorage.getItem("authenticatedUser");
    return usernameOrEmail;
}

export const logoutUser = () => {
    localStorage.clear();
    sessionStorage.clear();
    // window.location.reload(false);
}

export const isLoggedInUserAdmin = () => {
    let role = sessionStorage.getItem("role");
    if (role != null && role === "ROLE_ADMIN") {
        return true;
    }
    else {
        return false;
    }
}