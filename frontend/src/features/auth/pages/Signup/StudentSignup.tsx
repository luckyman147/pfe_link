import React from 'react';
import { Link } from 'react-router-dom';
import { useStudentSignup } from '@/features/auth/hooks';
import { AuthLayout, StudentSignupForm } from '@/features/auth/components';

export const StudentSignup: React.FC = () => {
  const { form, faculties, cardUrl, handleCardChange, onSubmit } = useStudentSignup();

  return (
    <AuthLayout
      title="Student Onboarding"
      subtitle="Join the next generation of researchers. Create your profile to start collaborating on innovative PFE projects with top-tier advisors."
      imageSrc="https://images.unsplash.com/photo-1523050335102-c325090ea232?auto=format&fit=crop&q=80&w=1000"
      imageAlt="Students working together"
      icon="history_edu"
    >
      <div className="flex flex-col gap-2 mb-8">
        <h2 className="text-3xl font-bold tracking-tight text-stitch-on-surface">
          Student Profile
        </h2>
        <p className="text-stitch-on-surface-variant">
          Complete your registration to access the project catalog.
        </p>
      </div>

      <StudentSignupForm
        form={form}
        faculties={faculties}
        cardUrl={cardUrl}
        onCardChange={handleCardChange}
        onSubmit={onSubmit}
      />

      <div className="mt-8 text-center">
        <Link 
          to="/auth/signup" 
          className="inline-flex items-center gap-2 text-sm font-bold text-stitch-primary hover:text-stitch-primary-container transition-all group"
        >
          <span className="material-symbols-outlined text-[18px] group-hover:-translate-x-1 transition-transform">arrow_back</span>
          Change User Type
        </Link>
      </div>
    </AuthLayout>
  );
};
