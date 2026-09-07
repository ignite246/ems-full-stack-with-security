import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
console.log("OfficeService::API_BASE_URL:", API_BASE_URL);

const OFFICE_REST_API_BASE_URL = API_BASE_URL + "/offices";

export const getAllOffices = () => axios.get(OFFICE_REST_API_BASE_URL);