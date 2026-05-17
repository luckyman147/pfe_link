import React from 'react';
import type { UseFormReturn } from 'react-hook-form';
import { StudentCardUpload } from '../Onboarding/StudentCardUpload';
import type { AdvisorFormData } from '../../hooks';
import type { Faculty } from '../../types/auth.types';
import { AdvisorFormFields } from './AdvisorFormFields';
import { FacultySelect } from './FacultySelect';

interface AdvisorSignupFormProps {
  form: UseFormReturn<AdvisorFormData>;
  faculties: Faculty[];
  cinUrl: string | null;
  onCinChange: (url: string, id?: string) => void;
  onSubmit: (data: AdvisorFormData) => void;
}

export const AdvisorSignupForm: React.FC<AdvisorSignupFormProps> = ({
  form, faculties, cinUrl, onCinChange, onSubmit
}) => {
  const { register, handleSubmit, formState: { errors, isSubmitting } } = form;

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-6">
      <AdvisorFormFields form={form} />

      <FacultySelect form={form} faculties={faculties} />

      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        <div className="flex flex-col gap-1.5">
          <label className="text-sm font-medium text-stitch-on-surface-variant px-1" htmlFor="cinNumber">CIN Number</label>
          <input
            {...register('cinNumber')}
            id="cinNumber"
            maxLength={8}
            placeholder="08765432"
            className={`w-full px-4 py-3 bg-stitch-surface-container-lowest border rounded-xl text-sm outline-none transition-all
              ${errors.cinNumber ? 'border-stitch-error focus:ring-1 focus:ring-stitch-error' : 'border-stitch-outline-variant focus:border-stitch-primary focus:ring-1 focus:ring-stitch-primary'}`}
          />
        </div>
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
        </div>
      </div>

      <div className="flex flex-col gap-2">
        <span className="text-sm font-medium text-stitch-on-surface-variant px-1">
          Upload your identity card or advisor card
        </span>
        <StudentCardUpload
          cardUrl={cinUrl}
          onChange={onCinChange}
          uploadTitle="Upload your identity card or advisor card"
          previewAlt="Advisor identity card"
        />
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
        {isSubmitting ? 'Registering Expert...' : 'Create Advisor Account'}
      </button>
    </form>
  );
};
