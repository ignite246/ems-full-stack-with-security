import axios from "axios";

const AUTH_SERVICE_REST_API_BASE_URL = "http://localhost:8080/api/auth";

export const registerUser = (registerObj) => axios.post(AUTH_SERVICE_REST_API_BASE_URL + "/register", registerObj);

export const loginUser = (loginObj) => axios.post(AUTH_SERVICE_REST_API_BASE_URL + "/login", loginObj);

export const storeToken = (token) => localStorage.setItem("token", token);

export const getToken = () => localStorage.getItem("token");

export const saveLoggedInUser = (usernameOrEmail) => sessionStorage.setItem("authenticatedUser", usernameOrEmail);

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