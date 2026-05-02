import React from 'react';
import { AlertCircle } from 'lucide-react';

interface FormFieldProps {
  label: string;
  icon?: React.ReactNode;
  error?: string;
  children: React.ReactNode;
}

export const FormField: React.FC<FormFieldProps> = ({ label, icon, error, children }) => (
  <div className="space-y-1.5">
    <label className="block text-xs font-bold uppercase tracking-widest text-gray-400">{label}</label>
    <div className={`relative rounded-xl border transition-all duration-200
      ${icon ? '[&_input]:pl-10 [&_select]:pl-10' : ''}
      ${error
        ? 'border-red-400/70 bg-red-50/30'
        : 'border-gray-200 bg-white focus-within:border-primary-400 focus-within:shadow-[0_0_0_3px_rgba(0,102,255,0.08)]'}`}>
      {icon && (
        <span className="absolute left-3.5 top-1/2 -translate-y-1/2 text-gray-400 pointer-events-none z-10">
          {icon}
        </span>
      )}
      {children}
    </div>
    {error && (
      <p className="flex items-center gap-1.5 text-xs text-red-500 font-medium">
        <AlertCircle className="w-3.5 h-3.5 shrink-0" /> {error}
      </p>
    )}
  </div>
);
