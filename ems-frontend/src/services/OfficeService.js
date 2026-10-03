import apiClient from "./ApiClient";

const OFFICE_REST_API_BASE_URL = "/offices";

export const getAllOffices = () =>
    apiClient.get(OFFICE_REST_API_BASE_URL);