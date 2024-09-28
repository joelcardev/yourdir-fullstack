import axios, { AxiosInstance } from "axios";

const baseURL = import.meta.env.VITE_API_BASE_URL;

const axiosInstance: AxiosInstance = axios.create({
  baseURL,
  headers: {
    "Content-Type": "application/json",
  },
});

const apiRequest = async (
  relativeOrFullURL: string,
  method: "get" | "post" | "put" | "delete",
  data?: any
) => {
  try {
    const response = await axiosInstance({
      url: relativeOrFullURL,
      method,
      data,
    });
    return response.data;
  } catch (error) {
    console.error("Erro ao fazer requisição:", error);
    throw error;
  }
};

export { axiosInstance, apiRequest };
