import { CreateDirectoryDTO, Directory } from "../interfaces/diretory.inteface";
import { apiRequest } from "./api";

const BASE_URL = "/api/directories";

export const fetchDirectories = async (): Promise<Directory[]> => {
  return apiRequest(BASE_URL, "get");
};

export const createDirectory = (data: CreateDirectoryDTO) =>
  apiRequest(BASE_URL, "post", data);

export const deleteDirectory = (id: number) =>
  apiRequest(`${BASE_URL}/${id}`, "delete");
