import React, { useEffect, useRef, useState } from 'react';
import { Camera, X, ZoomIn } from 'lucide-react';

interface Props {
  onCapture: (dataUrl: string) => void;
  onClose: () => void;
}

export const CameraModal: React.FC<Props> = ({ onCapture, onClose }) => {
  const videoRef = useRef<HTMLVideoElement>(null);
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const [error, setError] = useState('');
  const [ready, setReady] = useState(false);
  const streamRef = useRef<MediaStream | null>(null);

  useEffect(() => {
    navigator.mediaDevices
      .getUserMedia({ video: { facingMode: 'environment' }, audio: false })
      .then((stream) => {
        streamRef.current = stream;
        if (videoRef.current) {
          videoRef.current.srcObject = stream;
          videoRef.current.onloadedmetadata = () => setReady(true);
        }
      })
      .catch(() => setError('Camera access denied. Please allow camera permission.'));

    return () => streamRef.current?.getTracks().forEach((t) => t.stop());
  }, []);

  const capture = () => {
    if (!videoRef.current || !canvasRef.current) return;
    const v = videoRef.current;
    canvasRef.current.width = v.videoWidth;
    canvasRef.current.height = v.videoHeight;
    canvasRef.current.getContext('2d')?.drawImage(v, 0, 0);
    onCapture(canvasRef.current.toDataURL('image/jpeg', 0.92));
    streamRef.current?.getTracks().forEach((t) => t.stop());
    onClose();
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-sm p-4">
      <div className="relative bg-gray-900 rounded-2xl overflow-hidden w-full max-w-md shadow-2xl">
        <button onClick={onClose}
          className="absolute top-3 right-3 z-10 p-2 bg-black/50 hover:bg-black/70 text-white rounded-full transition-all">
          <X className="w-4 h-4" />
        </button>

        <div className="relative aspect-4/3 bg-black flex items-center justify-center">
          {error ? (
            <p className="text-red-400 text-sm text-center px-6">{error}</p>
          ) : (
            <video ref={videoRef} autoPlay playsInline muted className="w-full h-full object-cover" />
          )}
          {/* Viewfinder overlay */}
          {ready && <div className="absolute inset-6 border-2 border-white/30 rounded-xl pointer-events-none" />}
        </div>

        <canvas ref={canvasRef} className="hidden" />

        <div className="p-4 flex items-center justify-between bg-gray-900">
          <p className="text-gray-400 text-xs flex items-center gap-1.5">
            <ZoomIn className="w-3 h-3" /> Position your student card inside the frame
          </p>
          <button onClick={capture} disabled={!ready || !!error}
            className="flex items-center gap-2 px-5 py-2.5 bg-primary-500 hover:bg-primary-600 disabled:opacity-40 text-white text-sm font-semibold rounded-xl transition-all shadow-lg">
            <Camera className="w-4 h-4" /> Snap
          </button>
        </div>
      </div>
    </div>
  );
};
