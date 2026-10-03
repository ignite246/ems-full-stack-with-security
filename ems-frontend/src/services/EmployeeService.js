import apiClient from "./ApiClient";

const EMP_REST_API_BASE_URL = "/employees";

export const listEmployees = () =>
  apiClient.get(EMP_REST_API_BASE_URL);

export const createEmployee = (employee) =>
  apiClient.post(EMP_REST_API_BASE_URL, employee);

export const getEmployee = (id) =>
  apiClient.get(EMP_REST_API_BASE_URL + "/" + id);

export const updateEmployee = (id, employee) =>
  apiClient.put(EMP_REST_API_BASE_URL + "/" + id, employee);

export const deleteEmployee = (id) =>
  apiClient.delete(EMP_REST_API_BASE_URL + "/" + id);