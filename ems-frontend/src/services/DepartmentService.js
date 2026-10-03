import apiClient from "./ApiClient";

const DEPT_REST_API_BASE_URL = "/departments";

export const getAllDepartments = () =>
    apiClient.get(DEPT_REST_API_BASE_URL);

export const createDepartment = (department) =>
    apiClient.post(DEPT_REST_API_BASE_URL, department);

export const deleteDepartmentById = (id) =>
    apiClient.delete(DEPT_REST_API_BASE_URL + "/" + id);

export const getDepartmentById = (id) =>
    apiClient.get(DEPT_REST_API_BASE_URL + "/" + id);

export const updateDepartment = (id, department) =>
    apiClient.put(DEPT_REST_API_BASE_URL + "/" + id, department);