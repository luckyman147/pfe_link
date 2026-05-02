import React from 'react';
import type { UseFormReturn } from 'react-hook-form';
import { StudentCardUpload } from '../Onboarding/StudentCardUpload';
import type { StudentFormData } from '../../hooks';
import type { Faculty } from '../../types/auth.types';
import { IdentityFields } from './IdentityFields';
import { AcademicFields } from './AcademicFields';

interface StudentSignupFormProps {
  form: UseFormReturn<StudentFormData>;
  faculties: Faculty[];
  cardUrl: string | null;
  onCardChange: (url: string, id?: string) => void;
  onSubmit: (data: StudentFormData) => void;
}

export const StudentSignupForm: React.FC<StudentSignupFormProps> = ({
  form,
  faculties,
  cardUrl,
  onCardChange,
  onSubmit
}) => {
  const { register, handleSubmit, formState: { errors, isSubmitting } } = form;

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-6">
      <IdentityFields register={register} errors={errors} />
      <AcademicFields register={register} errors={errors} faculties={faculties} />

      <div className="flex flex-col gap-1.5">
        <label className="text-sm font-medium text-stitch-on-surface-variant px-1" htmlFor="password">Password</label>
        <input
          {...register('password')}
          id="password"
          type="password"
          placeholder="••••••••"
          className={`w-full px-4 py-3 bg-stitch-surface-container-lowest border rounded-xl text-sm outline-none transition-all
            ${errors.password ? 'border-stitch-error focus:ring-1 focus:ring-stitch-error' : 'border-stitch-outline-variant focus:border-stitch-primary focus:ring-1 focus:ring-stitch-primary'}`}
        />
        {errors.password && <span className="text-xs text-stitch-error mt-1 px-1">{errors.password.message as string}</span>}
      </div>

      <div className="flex flex-col gap-2">
        <span className="text-sm font-medium text-stitch-on-surface-variant px-1">Institutional Proof</span>
        <StudentCardUpload cardUrl={cardUrl} onChange={onCardChange} />
      </div>

      {errors.root && (
        <div className="p-3.5 bg-stitch-error-container/20 border border-stitch-error/30 rounded-xl text-xs text-stitch-error">
          {errors.root.message}
        </div>
      )}

      <button
        disabled={isSubmitting}
        type="submit"
        className="w-full mt-4 bg-stitch-primary hover:bg-stitch-primary-container text-stitch-on-primary font-bold py-4 rounded-xl shadow-lg shadow-stitch-primary/20 transition-all active:scale-[0.98] disabled:opacity-50"
      >
        {isSubmitting ? 'Processing Registration...' : 'Complete Registration'}
      </button>
    </form>
  );
};
