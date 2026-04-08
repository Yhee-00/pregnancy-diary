import api from "./axios";

export const getPregnancy = () => api.get("/pregnancy/1");
export const createPregnancy = (data) => api.post("/pregnancy", data);
