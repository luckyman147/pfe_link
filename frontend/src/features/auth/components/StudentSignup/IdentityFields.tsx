import React from 'react';
import type { UseFormRegister, FieldErrors } from 'react-hook-form';
import type { StudentFormData } from '../../hooks';

interface IdentityFieldsProps {
  register: UseFormRegister<StudentFormData>;
  errors: FieldErrors<StudentFormData>;
}

export const IdentityFields: React.FC<IdentityFieldsProps> = ({ register, errors }) => {
  return (
    <>
      <div className="flex flex-col gap-1.5">
        <label className="text-sm font-medium text-stitch-on-surface-variant px-1" htmlFor="fullName">Full Name</label>
        <input
          {...register('fullName')}
          id="fullName"
          placeholder="Jane Doe"
          className={`w-full px-4 py-3 bg-stitch-surface-container-lowest border rounded-xl text-sm outline-none transition-all
            ${errors.fullName ? 'border-stitch-error focus:ring-1 focus:ring-stitch-error' : 'border-stitch-outline-variant focus:border-stitch-primary focus:ring-1 focus:ring-stitch-primary'}`}
        />
        {errors.fullName && <span className="text-xs text-stitch-error mt-1 px-1">{errors.fullName.message as string}</span>}
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        <div className="flex flex-col gap-1.5">
          <label className="text-sm font-medium text-stitch-on-surface-variant px-1" htmlFor="email">Email</label>
          <input
            {...register('email')}
            id="email"
            type="email"
            placeholder="jane@university.edu"
            className={`w-full px-4 py-3 bg-stitch-surface-container-lowest border rounded-xl text-sm outline-none transition-all
              ${errors.email ? 'border-stitch-error focus:ring-1 focus:ring-stitch-error' : 'border-stitch-outline-variant focus:border-stitch-primary focus:ring-1 focus:ring-stitch-primary'}`}
          />
          {errors.email && <span className="text-xs text-stitch-error mt-1 px-1">{errors.email.message as string}</span>}
        </div>
        <div className="flex flex-col gap-1.5">
          <label className="text-sm font-medium text-stitch-on-surface-variant px-1" htmlFor="telephone">Phone</label>
          <input
            {...register('telephone')}
            id="telephone"
            placeholder="+216 22 333 444"
            className={`w-full px-4 py-3 bg-stitch-surface-container-lowest border rounded-xl text-sm outline-none transition-all
              ${errors.telephone ? 'border-stitch-error focus:ring-1 focus:ring-stitch-error' : 'border-stitch-outline-variant focus:border-stitch-primary focus:ring-1 focus:ring-stitch-primary'}`}
          />
          {errors.telephone && <span className="text-xs text-stitch-error mt-1 px-1">{errors.telephone.message as string}</span>}
        </div>
      </div>
    </>
  );
};
