import React from 'react';
import { Link } from 'react-router-dom';
import type { UseFormReturn } from 'react-hook-form';
import type { EmailInput } from '../../hooks';

interface EmailStepProps {
  form: UseFormReturn<EmailInput>;
  onSubmit: (data: EmailInput) => void;
}

export const EmailStep: React.FC<EmailStepProps> = ({ form, onSubmit }) => {
  return (
    <div className="animate-in fade-in slide-in-from-right-4 duration-500">
      <div className="mb-8">
        <h2 className="text-3xl font-bold tracking-tight text-stitch-on-surface mb-2">Identify Yourself</h2>
        <p className="text-stitch-on-surface-variant">
          Enter your academic email to begin the secure identity verification process.
        </p>
      </div>

      <form onSubmit={form.handleSubmit(onSubmit)} className="flex flex-col gap-6">
        <div className="flex flex-col gap-1.5">
          <label className="text-sm font-medium text-stitch-on-surface-variant px-1" htmlFor="email">
            Academic Email
          </label>
          <div className="relative">
            <span className="absolute left-4 top-1/2 -translate-y-1/2 material-symbols-outlined text-[20px] text-stitch-outline">mail</span>
            <input
              {...form.register('email')}
              id="email"
              type="email"
              placeholder="name@university.edu"
              className={`w-full pl-12 pr-4 py-3.5 bg-stitch-surface-container-lowest border rounded-xl text-sm outline-none transition-all
                ${form.formState.errors.email 
                  ? 'border-stitch-error focus:ring-1 focus:ring-stitch-error' 
                  : 'border-stitch-outline-variant focus:border-stitch-primary focus:ring-1 focus:ring-stitch-primary'}`}
            />
          </div>
          {form.formState.errors.email && (
            <span className="text-xs text-stitch-error mt-1 px-1">{form.formState.errors.email.message as string}</span>
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
          className="w-full bg-stitch-primary hover:bg-stitch-primary-container text-stitch-on-primary font-bold py-3.5 rounded-xl shadow-lg shadow-stitch-primary/20 transition-all active:scale-[0.98] disabled:opacity-50"
        >
          {form.formState.isSubmitting ? 'Verifying Identity...' : 'Send Verification Code'}
        </button>
      </form>

      <div className="mt-8 text-center">
        <Link 
          className="inline-flex items-center gap-2 text-sm font-bold text-stitch-primary hover:text-stitch-primary-container transition-all group"
          to="/auth/login"
        >
          <span className="material-symbols-outlined text-[18px] group-hover:-translate-x-1 transition-transform">arrow_back</span>
          Return to Sign In
        </Link>
      </div>
    </div>
  );
};
