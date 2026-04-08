import api from "./axios";

export const uploadPhoto = (entryId, file) => {
  const formData = new FormData();
  formData.append("file", file);
  return api.post(`/photos/${entryId}`, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
};

export const deletePhoto = (photoId) => api.delete(`/photos/${photoId}`);
