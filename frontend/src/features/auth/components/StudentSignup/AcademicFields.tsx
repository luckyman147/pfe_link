import React from 'react';
import type { UseFormRegister, FieldErrors } from 'react-hook-form';
import type { StudentFormData } from '../../hooks';
import type { Faculty } from '../../types/auth.types';

interface AcademicFieldsProps {
  register: UseFormRegister<StudentFormData>;
  errors: FieldErrors<StudentFormData>;
  faculties: Faculty[];
}

export const AcademicFields: React.FC<AcademicFieldsProps> = ({ register, errors, faculties }) => {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
      <div className="flex flex-col gap-1.5">
        <label className="text-sm font-medium text-stitch-on-surface-variant px-1" htmlFor="cinNumber">CIN Number</label>
        <input
          {...register('cinNumber')}
          id="cinNumber"
          maxLength={8}
          placeholder="07654321"
          className={`w-full px-4 py-3 bg-stitch-surface-container-lowest border rounded-xl text-sm outline-none transition-all
            ${errors.cinNumber ? 'border-stitch-error focus:ring-1 focus:ring-stitch-error' : 'border-stitch-outline-variant focus:border-stitch-primary focus:ring-1 focus:ring-stitch-primary'}`}
        />
        {errors.cinNumber && <span className="text-xs text-stitch-error mt-1 px-1">{errors.cinNumber.message as string}</span>}
      </div>
      <div className="flex flex-col gap-1.5">
        <label className="text-sm font-medium text-stitch-on-surface-variant px-1" htmlFor="facultyId">University / Faculty</label>
        <select
          {...register('facultyId')}
          id="facultyId"
          className={`w-full px-4 py-3 bg-stitch-surface-container-lowest border rounded-xl text-sm outline-none transition-all appearance-none cursor-pointer
            ${errors.facultyId ? 'border-stitch-error focus:ring-1 focus:ring-stitch-error' : 'border-stitch-outline-variant focus:border-stitch-primary focus:ring-1 focus:ring-stitch-primary'}`}
        >
          <option value="">Select your university / faculty</option>
          {faculties.map(f => <option key={f.id} value={f.id}>{f.name}</option>)}
        </select>
        {errors.facultyId && <span className="text-xs text-stitch-error mt-1 px-1">{errors.facultyId.message as string}</span>}
      </div>
    </div>
  );
};
