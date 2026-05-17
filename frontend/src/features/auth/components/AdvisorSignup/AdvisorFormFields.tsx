import React from 'react';
import type { UseFormReturn } from 'react-hook-form';
import type { AdvisorFormData } from '../../hooks';
import { phoneCountries } from '@/shared/lib/phoneCountries';

interface AdvisorFormFieldsProps {
  form: UseFormReturn<AdvisorFormData>;
}

export const AdvisorFormFields: React.FC<AdvisorFormFieldsProps> = ({ form }) => {
  const { register, errors, setValue, watch } = {
    register: form.register,
    errors: form.formState.errors,
    setValue: form.setValue,
    watch: form.watch,
  };
  const [dialCode, setDialCode] = React.useState('+216');
  const phoneValue = watch('telephone') || '';

  const handleDialCodeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const newDialCode = e.target.value;
    setDialCode(newDialCode);
    const localNumber = phoneValue.replace(/^\+\d+\s*/, '');
    setValue('telephone', `${newDialCode} ${localNumber}`.trim(), { shouldValidate: true });
  };

  const handleTelephoneChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const localNumber = e.target.value.replace(/^\+\d+\s*/, '');
    setValue('telephone', `${dialCode} ${localNumber}`.trim(), { shouldValidate: true });
  };

  return (
    <>
      <div className="flex flex-col gap-1.5">
        <label className="text-sm font-medium text-stitch-on-surface-variant px-1" htmlFor="fullName">Full Name</label>
        <input
          {...register('fullName')}
          id="fullName"
          placeholder="Dr. John Doe"
          className={`w-full px-4 py-3 bg-stitch-surface-container-lowest border rounded-xl text-sm outline-none transition-all
            ${errors.fullName ? 'border-stitch-error focus:ring-1 focus:ring-stitch-error' : 'border-stitch-outline-variant focus:border-stitch-primary focus:ring-1 focus:ring-stitch-primary'}`}
        />
        {errors.fullName && <span className="text-xs text-stitch-error mt-1 px-1">{errors.fullName.message as string}</span>}
      </div>

      <div className="flex flex-col gap-1.5">
        <label className="text-sm font-medium text-stitch-on-surface-variant px-1" htmlFor="email">Email</label>
        <input
          {...register('email')}
          id="email"
          type="email"
          placeholder="john.doe@university.edu"
          className={`w-full px-4 py-3 bg-stitch-surface-container-lowest border rounded-xl text-sm outline-none transition-all
            ${errors.email ? 'border-stitch-error focus:ring-1 focus:ring-stitch-error' : 'border-stitch-outline-variant focus:border-stitch-primary focus:ring-1 focus:ring-stitch-primary'}`}
        />
      </div>

      <div className="flex flex-col gap-1.5">
        <label className="text-sm font-medium text-stitch-on-surface-variant px-1" htmlFor="dialCode">Phone</label>
        <input type="hidden" {...register('telephone')} />
        <div className="flex gap-2">
          <select
            id="dialCode"
            value={dialCode}
            onChange={handleDialCodeChange}
            className="w-40 px-4 py-3 bg-stitch-surface-container-lowest border border-stitch-outline-variant rounded-xl text-sm outline-none transition-all focus:border-stitch-primary focus:ring-1 focus:ring-stitch-primary"
          >
            {phoneCountries.map((country) => (
              <option key={country.code} value={country.dialCode}>
                {country.flag} {country.dialCode}
              </option>
            ))}
          </select>
          <input
            id="telephone"
            value={phoneValue.replace(/^\+\d+\s*/, '')}
            onChange={handleTelephoneChange}
            placeholder="22 333 444"
            className={`flex-1 px-4 py-3 bg-stitch-surface-container-lowest border rounded-xl text-sm outline-none transition-all
              ${errors.telephone ? 'border-stitch-error focus:ring-1 focus:ring-stitch-error' : 'border-stitch-outline-variant focus:border-stitch-primary focus:ring-1 focus:ring-stitch-primary'}`}
          />
        </div>
        {errors.telephone && <span className="text-xs text-stitch-error mt-1 px-1">{errors.telephone.message as string}</span>}
      </div>
    </>
  );
};
