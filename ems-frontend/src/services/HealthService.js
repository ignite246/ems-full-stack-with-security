import axios from "axios";

const ACTUATOR_BASE_URL = import.meta.env.VITE_ACTUATOR_BASE_URL;
const HEALTH_API_BASE_URL = ACTUATOR_BASE_URL + "/health";
console.log("HealthService::HEALTH_API_BASE_URL:", HEALTH_API_BASE_URL);

export const getHealth = () => axios.get(HEALTH_API_BASE_URL);