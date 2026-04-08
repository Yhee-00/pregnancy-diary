import api from "./axios";

export const getDiaryList = (pregnancyId) => api.get(`/diary/${pregnancyId}`);
export const getDiaryEntry = (entryId) => api.get(`/diary/entry/${entryId}`);
export const createDiaryEntry = (pregnancyId, data) =>
  api.post(`/diary/${pregnancyId}`, data);
export const updateDiaryEntry = (entryId, data) =>
  api.put(`/diary/entry/${entryId}`, data);
export const deleteDiaryEntry = (entryId) =>
  api.delete(`/diary/entry/${entryId}`);
