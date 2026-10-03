import axios from "axios";
import { getTokenFromLocalStorage } from "./AuthService";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

console.log("apiClient::API_BASE_URL:", API_BASE_URL);

const apiClient = axios.create({
    baseURL: API_BASE_URL
});

apiClient.interceptors.request.use(
    function (config) {
        const token = getTokenFromLocalStorage();

        if (token) {
            config.headers.Authorization = token;
        }

        return config;
    },
    function (error) {
        return Promise.reject(error);
    }
);

export default apiClient;