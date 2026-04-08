import api from "./axios";

export const getAiMessage = (pregnancyId, date) =>
  api.get(`/ai/message?pregnancyId=${pregnancyId}&date=${date}`);
