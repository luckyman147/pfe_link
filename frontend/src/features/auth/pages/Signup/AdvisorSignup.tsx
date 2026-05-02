import React from 'react';
import { Link } from 'react-router-dom';
import { useAdvisorSignup } from '@/features/auth/hooks';
import { AuthLayout, AdvisorSignupForm } from '@/features/auth/components';

export const AdvisorSignup: React.FC = () => {
  const { form, cinUrl, handleCinChange, onSubmit } = useAdvisorSignup();

  return (
    <AuthLayout
      title="Advisor Registration"
      subtitle="Share your expertise with ambitious students. Set your project capacity and mentor the next generation of industry leaders."
      imageSrc="https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&q=80&w=1000"
      imageAlt="Professional advisor mentoring"
      icon="engineering"
    >
      <div className="flex flex-col gap-2 mb-8">
        <h2 className="text-3xl font-bold tracking-tight text-stitch-on-surface">
          Advisor Portal
        </h2>
        <p className="text-stitch-on-surface-variant">
          Register to oversee and supervise academic projects.
        </p>
      </div>

      <AdvisorSignupForm
        form={form}
        cinUrl={cinUrl}
        onCinChange={handleCinChange}
        onSubmit={onSubmit}
      />

      <div className="mt-8 text-center">
        <Link 
          to="/auth/signup" 
          className="inline-flex items-center gap-2 text-sm font-bold text-stitch-primary hover:text-stitch-primary-container transition-all group"
        >
          <span className="material-symbols-outlined text-[18px] group-hover:-translate-x-1 transition-transform">arrow_back</span>
          Change Role
        </Link>
      </div>
    </AuthLayout>
  );
};
