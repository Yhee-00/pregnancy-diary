import api from "./axios";

export const createOrder = (data) => api.post("/books/order", data);
export const getOrders = (pregnancyId) =>
  api.get(`/books/order/${pregnancyId}`);
