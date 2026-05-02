import React from 'react';
import type { UseFormReturn } from 'react-hook-form';
import type { PasswordInput } from '../../hooks';

interface PasswordStepProps {
  form: UseFormReturn<PasswordInput>;
  onSubmit: (data: PasswordInput) => void;
}

export const PasswordStep: React.FC<PasswordStepProps> = ({ form, onSubmit }) => {
  return (
    <div className="animate-in fade-in slide-in-from-right-4 duration-500">
      <div className="mb-8">
        <h2 className="text-3xl font-bold tracking-tight text-stitch-on-surface mb-2">New Password</h2>
        <p className="text-stitch-on-surface-variant">
          Create a strong, unique password to secure your academic account.
        </p>
      </div>

      <form onSubmit={form.handleSubmit(onSubmit)} className="flex flex-col gap-5">
        <div className="flex flex-col gap-1.5">
          <label className="text-sm font-medium text-stitch-on-surface-variant px-1" htmlFor="newPassword">
            New Password
          </label>
          <div className="relative">
            <span className="absolute left-4 top-1/2 -translate-y-1/2 material-symbols-outlined text-[20px] text-stitch-outline">lock</span>
            <input
              {...form.register('newPassword')}
              id="newPassword"
              type="password"
              placeholder="••••••••"
              className={`w-full pl-12 pr-4 py-3.5 bg-stitch-surface-container-lowest border rounded-xl text-sm outline-none transition-all
                ${form.formState.errors.newPassword 
                  ? 'border-stitch-error focus:ring-1 focus:ring-stitch-error' 
                  : 'border-stitch-outline-variant focus:border-stitch-primary focus:ring-1 focus:ring-stitch-primary'}`}
            />
          </div>
          {form.formState.errors.newPassword && (
            <span className="text-xs text-stitch-error mt-1 px-1">{form.formState.errors.newPassword.message as string}</span>
          )}
        </div>

        <div className="flex flex-col gap-1.5">
          <label className="text-sm font-medium text-stitch-on-surface-variant px-1" htmlFor="confirmPassword">
            Confirm Password
          </label>
          <div className="relative">
            <span className="absolute left-4 top-1/2 -translate-y-1/2 material-symbols-outlined text-[20px] text-stitch-outline">lock_reset</span>
            <input
              {...form.register('confirmPassword')}
              id="confirmPassword"
              type="password"
              placeholder="••••••••"
              className={`w-full pl-12 pr-4 py-3.5 bg-stitch-surface-container-lowest border rounded-xl text-sm outline-none transition-all
                ${form.formState.errors.confirmPassword 
                  ? 'border-stitch-error focus:ring-1 focus:ring-stitch-error' 
                  : 'border-stitch-outline-variant focus:border-stitch-primary focus:ring-1 focus:ring-stitch-primary'}`}
            />
          </div>
          {form.formState.errors.confirmPassword && (
            <span className="text-xs text-stitch-error mt-1 px-1">{form.formState.errors.confirmPassword.message as string}</span>
          )}
        </div>

        {form.formState.errors.root && (
          <div className="p-3.5 bg-stitch-error-container/20 border border-stitch-error/30 rounded-xl text-xs text-stitch-error">
            {form.formState.errors.root.message as string}
          </div>
        )}

        <button
          disabled={form.formState.isSubmitting}
          type="submit"
          className="w-full mt-2 bg-stitch-primary hover:bg-stitch-primary-container text-stitch-on-primary font-bold py-3.5 rounded-xl shadow-lg shadow-stitch-primary/20 transition-all active:scale-[0.98] disabled:opacity-50"
        >
          {form.formState.isSubmitting ? 'Updating Security...' : 'Apply New Password'}
        </button>
      </form>
    </div>
  );
};
