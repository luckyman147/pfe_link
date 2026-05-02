import api from './api';

export interface UploadResponse {
  draftId: string;
  url: string;
}

const uploadService = {
  uploadDraft: async (file: File): Promise<UploadResponse> => {
    const formData = new FormData();
    formData.append('file', file);

    const { data } = await api.post('/api/v1/uploads/draft', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    return data.data;
  },
};

export default uploadService;
