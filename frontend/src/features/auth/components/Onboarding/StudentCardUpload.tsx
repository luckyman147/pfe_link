import React, { useRef, useState } from 'react';
import { Upload, Camera, CheckCircle, X, RefreshCw, Loader2 } from 'lucide-react';
import { CameraModal } from './CameraModal';
import uploadService from '@/shared/services/upload.service';

interface Props {
  cardUrl: string | null;
  onChange: (url: string, id?: string) => void;
}

export const StudentCardUpload: React.FC<Props> = ({ cardUrl, onChange }) => {
  const fileRef = useRef<HTMLInputElement>(null);
  const [showCamera, setShowCamera] = useState(false);
  const [isUploading, setIsUploading] = useState(false);

  const handleFile = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;
    if (!file.type.startsWith('image/')) { alert('Please select an image.'); return; }
    if (file.size > 5 * 1024 * 1024) { alert('File must be under 5 MB.'); return; }

    setIsUploading(true);
    try {
      const response = await uploadService.uploadDraft(file);
      onChange(response.url, response.draftId);
    } catch (error) {
      console.error('Upload failed:', error);
      alert('Upload failed. Please try again.');
    } finally {
      setIsUploading(false);
      e.target.value = '';
    }
  };

  return (
    <>
      {showCamera && (
        <CameraModal onCapture={(url) => { onChange(url); setShowCamera(false); }} onClose={() => setShowCamera(false)} />
      )}
      <input ref={fileRef} type="file" accept="image/*" className="hidden" onChange={handleFile} />

      <div className="relative rounded-xl overflow-hidden border-2 border-dashed transition-all min-h-[180px]"
        style={{ borderColor: cardUrl ? '#86efac' : '#e5e7eb' }}>
        
        {isUploading && (
          <div className="absolute inset-0 z-20 bg-white/80 backdrop-blur-sm flex flex-col items-center justify-center gap-3">
            <Loader2 className="w-8 h-8 text-primary-500 animate-spin" />
            <p className="text-sm font-semibold text-primary-700 font-inter">Processing image...</p>
          </div>
        )}

        {cardUrl ? (
          <div className="relative w-full min-h-[180px]">
            <img src={cardUrl} alt="Student Card" className="w-full h-full object-cover min-h-[180px]" />
            <div className="absolute top-2 left-2 flex items-center gap-1.5 bg-green-500/90 text-white text-xs font-semibold px-2.5 py-1 rounded-full shadow">
              <CheckCircle className="w-3 h-3" /> Uploaded
            </div>
            <div className="absolute bottom-0 left-0 right-0 flex gap-2 p-2 bg-black/50 backdrop-blur-sm">
              <button type="button" onClick={() => fileRef.current?.click()}
                className="flex-1 flex items-center justify-center gap-1.5 py-1.5 bg-white/20 hover:bg-white/30 text-white text-xs font-medium rounded-lg transition-all">
                <RefreshCw className="w-3 h-3" /> Replace
              </button>
              <button type="button" onClick={() => setShowCamera(true)}
                className="flex-1 flex items-center justify-center gap-1.5 py-1.5 bg-white/20 hover:bg-white/30 text-white text-xs font-medium rounded-lg transition-all">
                <Camera className="w-3 h-3" /> Retake
              </button>
              <button type="button" onClick={() => onChange('', '')}
                className="py-1.5 px-3 bg-red-500/70 hover:bg-red-500/90 text-white rounded-lg transition-all">
                <X className="w-3 h-3" />
              </button>
            </div>
          </div>
        ) : (
          <div className="flex flex-col items-center justify-center gap-4 p-8 bg-gray-50/50 hover:bg-primary-50/20 transition-all min-h-[180px]">
            <Upload className="w-10 h-10 text-gray-300" />
            <div className="text-center">
              <p className="text-sm font-semibold text-gray-600">Upload your student card</p>
              <p className="text-xs text-gray-400 mt-0.5">PNG, JPG up to 5 MB</p>
            </div>
            <div className="flex gap-3">
              <button type="button" onClick={() => fileRef.current?.click()}
                className="flex items-center gap-2 px-4 py-2 bg-primary-500 hover:bg-primary-600 text-white text-sm font-medium rounded-xl shadow transition-all">
                <Upload className="w-4 h-4" /> Browse
              </button>
              <button type="button" onClick={() => setShowCamera(true)}
                className="flex items-center gap-2 px-4 py-2 bg-white border border-gray-200 hover:border-primary-300 hover:bg-primary-50 text-gray-700 text-sm font-medium rounded-xl shadow-sm transition-all">
                <Camera className="w-4 h-4 text-primary-500" /> Camera
              </button>
            </div>
          </div>
        )}
      </div>
    </>
  );
};
