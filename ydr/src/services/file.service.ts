import { File } from "../interfaces/file.interface";
import { apiRequest } from "./api";

const BASE_URL = "/api/files";

export const fetchFiles = () => apiRequest(BASE_URL, "get");

export const uploadFile = (data: File) =>
  apiRequest(`${BASE_URL}/directory/${data.directoryId}`, "post", {
    name: data.name,
    content: data.content,
  });

export const editFile = (id: number, data: File) =>
  apiRequest(`${BASE_URL}/${id}`, "put", {
    name: data.name,
    content: data.content,
  });

export const deleteFile = (id: number) =>
  apiRequest(`${BASE_URL}/${id}`, "delete");
