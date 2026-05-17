import React from 'react';
import type { UseFormReturn } from 'react-hook-form';
import type { Faculty } from '../../types/auth.types';
import type { AdvisorFormData } from '../../hooks';

interface FacultySelectProps {
  form: UseFormReturn<AdvisorFormData>;
  faculties: Faculty[];
}

const OTHER = '__OTHER__';

export const FacultySelect: React.FC<FacultySelectProps> = ({ form, faculties }) => {
  const { register, watch, setValue, formState: { errors } } = form;
  const [mode, setMode] = React.useState<'select' | 'email'>('select');

  const inputBase = 'w-full px-4 py-3 bg-stitch-surface-container-lowest border rounded-xl text-sm outline-none transition-all';
  const inputErr = 'border-stitch-error focus:ring-1 focus:ring-stitch-error';
  const inputOk = 'border-stitch-outline-variant focus:border-stitch-primary focus:ring-1 focus:ring-stitch-primary';

  const selectValue = watch('facultyId') ?? '';

  const onSelectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    if (e.target.value === OTHER) {
      setMode('email');
      setValue('facultyId', '', { shouldValidate: true });
    } else {
      setValue('facultyId', e.target.value, { shouldValidate: true });
      setValue('facultyDomainEmail', '', { shouldValidate: true });
    }
  };

  const switchBackToSelect = () => {
    setMode('select');
    setValue('facultyDomainEmail', '', { shouldValidate: true });
  };

  return (
    <div className="flex flex-col gap-1.5">
      <label className="text-sm font-medium text-stitch-on-surface-variant px-1" htmlFor="facultyId">
        University / Faculty
      </label>

      {mode === 'select' ? (
        <select
          id="facultyId"
          value={selectValue === '' ? '' : selectValue}
          onChange={onSelectChange}
          className={`${inputBase} appearance-none cursor-pointer ${errors.facultyId ? inputErr : inputOk}`}
        >
          <option value="">Select your university / faculty</option>
          {faculties.map(f => <option key={f.id} value={f.id}>{f.name}</option>)}
          <option value={OTHER}>Other / Not in list</option>
        </select>
      ) : (
        <div className="flex gap-2">
          <input
            {...register('facultyDomainEmail')}
            id="facultyDomainEmail"
            type="email"
            placeholder="contact@your-faculty.edu"
            className={`${inputBase} flex-1 ${errors.facultyDomainEmail ? inputErr : inputOk}`}
          />
          <button
            type="button"
            onClick={switchBackToSelect}
            className="px-3 py-2 text-xs font-bold text-stitch-primary border border-stitch-outline-variant rounded-xl hover:bg-stitch-surface-container-low transition-all"
          >
            Pick from list
          </button>
        </div>
      )}

      {errors.facultyId && (
        <span className="text-xs text-stitch-error mt-1 px-1">{errors.facultyId.message as string}</span>
      )}
      {errors.facultyDomainEmail && (
        <span className="text-xs text-stitch-error mt-1 px-1">{errors.facultyDomainEmail.message as string}</span>
      )}
    </div>
  );
};
