import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
console.log("DepartmentService::API_BASE_URL:", API_BASE_URL);

const DEPT_REST_API_BASE_URL = API_BASE_URL + "/departments";

export const getAllDepartments = () => axios.get(DEPT_REST_API_BASE_URL);

export const createDepartment = (department) => axios.post(DEPT_REST_API_BASE_URL, department);

export const deleteDepartmentById = (id) => axios.delete(DEPT_REST_API_BASE_URL + "/" + id);

export const getDepartmentById = (id) => axios.get(DEPT_REST_API_BASE_URL + "/" + id);

export const updateDepartment = (id, department) => axios.put(DEPT_REST_API_BASE_URL + "/" + id, department);