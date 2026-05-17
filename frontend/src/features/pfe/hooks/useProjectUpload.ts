import { useState, useCallback } from 'react';
import uploadService from '@/shared/services/upload.service';
import type { UploadedFile } from '@/features/pfe/types/project.types';

interface UseProjectUploadReturn {
  uploadedFiles: UploadedFile[];
  isUploading: boolean;
  error: string | null;
  uploadFile: (file: File) => Promise<void>;
  deleteFile: (draftId: string) => Promise<void>;
  addFileToList: (file: UploadedFile) => void;
  removeFileFromList: (draftId: string) => void;
}

const ALLOWED_IMAGE_TYPES = [
  'image/jpeg',
  'image/png',
  'image/gif',
  'image/webp',
];
const ALLOWED_VIDEO_TYPES = ['video/mp4', 'video/webm', 'video/ogg'];
const MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
const MAX_VIDEO_SIZE = 50 * 1024 * 1024; // 50MB

export const useProjectUpload = (): UseProjectUploadReturn => {
  const [uploadedFiles, setUploadedFiles] = useState<UploadedFile[]>([]);
  const [isUploading, setIsUploading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const getFileType = (mimeType: string): 'image' | 'video' | null => {
    if (ALLOWED_IMAGE_TYPES.includes(mimeType)) return 'image';
    if (ALLOWED_VIDEO_TYPES.includes(mimeType)) return 'video';
    return null;
  };

  const validateFile = (file: File): string | null => {
    const fileType = getFileType(file.type);

    if (!fileType) {
      return 'File type not supported. Use images (JPEG, PNG, GIF, WebP) or videos (MP4, WebM, OGG).';
    }

    const maxSize = fileType === 'video' ? MAX_VIDEO_SIZE : MAX_IMAGE_SIZE;
    if (file.size > maxSize) {
      const maxMB = maxSize / (1024 * 1024);
      return `File size exceeds ${maxMB}MB limit.`;
    }

    return null;
  };

  const uploadFile = useCallback(async (file: File) => {
    const validationError = validateFile(file);
    if (validationError) {
      setError(validationError);
      throw new Error(validationError);
    }

    try {
      setError(null);
      setIsUploading(true);

      const response = await uploadService.uploadDraft(file);
      const fileType = getFileType(file.type);

      const uploadedFile: UploadedFile = {
        draftId: response.draftId,
        publicId: response.draftId,
        url: response.url,
        fileName: file.name,
        mimeType: file.type,
        type: fileType || 'image',
        uploadedAt: new Date().toISOString(),
      };

      setUploadedFiles((prev) => [...prev, uploadedFile]);
    } catch (err: any) {
      const errorMessage =
        err.message || err.response?.data?.message || 'Failed to upload file';
      setError(errorMessage);
      throw err;
    } finally {
      setIsUploading(false);
    }
  }, []);

  const deleteFile = useCallback(async (draftId: string) => {
    try {
      setError(null);
      // Backend delete endpoint: DELETE /api/v1/uploads/draft/{publicId}
      // For now, just remove from local state; implement actual deletion if needed
      setUploadedFiles((prev) =>
        prev.filter((file) => file.draftId !== draftId)
      );
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to delete file');
      throw err;
    }
  }, []);

  const addFileToList = useCallback((file: UploadedFile) => {
    setUploadedFiles((prev) => [...prev, file]);
  }, []);

  const removeFileFromList = useCallback((draftId: string) => {
    setUploadedFiles((prev) =>
      prev.filter((file) => file.draftId !== draftId)
    );
  }, []);

  return {
    uploadedFiles,
    isUploading,
    error,
    uploadFile,
    deleteFile,
    addFileToList,
    removeFileFromList,
  };
};
