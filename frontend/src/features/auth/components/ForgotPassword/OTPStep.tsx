import React from 'react';
import type { UseFormReturn } from 'react-hook-form';
import type { OTPInputData } from '../../hooks';
import { OTPInput } from '../Common/OTPInput';

interface OTPStepProps {
  form: UseFormReturn<OTPInputData>;
  onSubmit: (data: OTPInputData) => void;
}

export const OTPStep: React.FC<OTPStepProps> = ({ form, onSubmit }) => {
  const otpValue = form.watch('otpCode') || '';

  return (
    <div className="animate-in fade-in slide-in-from-right-4 duration-500">
      <div className="mb-8">
        <h2 className="text-3xl font-bold tracking-tight text-stitch-on-surface mb-2">Check Email</h2>
        <p className="text-stitch-on-surface-variant">
          We've sent a unique security code to your inbox. Please enter it to authorize your password reset.
        </p>
      </div>

      <form onSubmit={form.handleSubmit(onSubmit)} className="flex flex-col gap-8">
        <div className="flex justify-center py-6 bg-stitch-surface-container-low/30 rounded-2xl border border-stitch-surface-variant/50">
          <OTPInput
            error={!!form.formState.errors.otpCode || !!form.formState.errors.root}
            onChange={(val:any) => form.setValue('otpCode', val)}
            value={otpValue}
          />
        </div>

        {form.formState.errors.root && (
          <div className="p-3.5 bg-stitch-error-container/20 border border-stitch-error/30 rounded-xl text-xs text-stitch-error text-center">
            {form.formState.errors.root.message as string}
          </div>
        )}

        <button
          disabled={form.formState.isSubmitting}
          type="submit"
          className="w-full bg-stitch-primary hover:bg-stitch-primary-container text-stitch-on-primary font-bold py-3.5 rounded-xl shadow-lg shadow-stitch-primary/20 transition-all active:scale-[0.98] disabled:opacity-50"
        >
          {form.formState.isSubmitting ? 'Authenticating...' : 'Authorize Reset'}
        </button>
      </form>
      
      <p className="mt-8 text-center text-xs text-stitch-on-surface-variant/60">
        Didn't get the code? Check your spam folder or try resending.
      </p>
    </div>
  );
};
