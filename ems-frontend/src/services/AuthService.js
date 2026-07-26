import axios from "axios";

const AUTH_SERVICE_REST_API_BASE_URL = "http://localhost:8080/api/auth";

export const registerUser = (registerObj) => axios.post(AUTH_SERVICE_REST_API_BASE_URL + "/register", registerObj);

export const loginUser = (loginObj) => axios.post(AUTH_SERVICE_REST_API_BASE_URL + "/login", loginObj);

