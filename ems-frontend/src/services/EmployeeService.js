import axios from "axios";
import { getTokenFromLocalStorage } from "./AuthService";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
console.log("EmployeeService::API_BASE_URL:", API_BASE_URL);

const EMP_REST_API_BASE_URL = API_BASE_URL + "/employees";


// export const listEmployees = () => {
//     return axios.get(REST_API_BASE_URL);
// }

export const listEmployees = () => axios.get(EMP_REST_API_BASE_URL);

export const createEmployee = (employee) => axios.post(EMP_REST_API_BASE_URL, employee);

export const getEmployee = (id) => axios.get(EMP_REST_API_BASE_URL + "/" + id);

export const updateEmployee = (id, employee) => axios.put(EMP_REST_API_BASE_URL + "/" + id, employee);

export const deleteEmployee = (id) => axios.delete(EMP_REST_API_BASE_URL + "/" + id);


// Add a request interceptor
axios.interceptors.request.use(
  function (config) {
    config.headers['Authorization'] = getTokenFromLocalStorage();
    return config;
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error);
  }
);