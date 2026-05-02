import React, { useRef } from 'react';

interface OTPInputProps {
  length?: number;
  value: string;
  onChange: (value: string) => void;
  error?: boolean;
}

export const OTPInput: React.FC<OTPInputProps> = ({ 
  length = 6, 
  value, 
  onChange,
  error 
}) => {
  const inputs = useRef<(HTMLInputElement | null)[]>([]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>, index: number) => {
    const val = e.target.value;
    if (/^[0-9]$/.test(val)) {
      const newValue = value.split('');
      newValue[index] = val;
      const combinedValue = newValue.join('');
      onChange(combinedValue);
      
      if (index < length - 1) {
        inputs.current[index + 1]?.focus();
      }
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>, index: number) => {
    if (e.key === 'Backspace') {
      if (!value[index] && index > 0) {
        const newValue = value.split('');
        newValue[index - 1] = '';
        onChange(newValue.join(''));
        inputs.current[index - 1]?.focus();
      } else {
        const newValue = value.split('');
        newValue[index] = '';
        onChange(newValue.join(''));
      }
    }
  };

  return (
    <div className="flex justify-between items-center gap-2 sm:gap-4">
      {Array.from({ length }).map((_, i) => (
        <input
          key={i}
          ref={(el) => { inputs.current[i] = el; }}
          type="text"
          inputMode="numeric"
          maxLength={1}
          value={value[i] || ''}
          onChange={(e) => handleChange(e, i)}
          onKeyDown={(e) => handleKeyDown(e, i)}
          className={`w-10 sm:w-12 h-12 sm:h-14 text-center font-semibold text-xl rounded-lg border transition-all shadow-sm focus:outline-none focus:ring-2
            ${error 
              ? 'border-stitch-error bg-stitch-error-container/20 text-stitch-error focus:ring-stitch-error' 
              : 'border-stitch-outline-variant bg-stitch-surface-container-lowest text-stitch-on-surface focus:border-stitch-primary focus:ring-stitch-primary'
            }`}
        />
      ))}
    </div>
  );
};
