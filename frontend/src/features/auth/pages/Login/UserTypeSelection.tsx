import React from 'react';
import { Link } from 'react-router-dom';
import { AuthLayout } from '@/features/auth/components';

export const UserTypeSelection: React.FC = () => {
  return (
    <AuthLayout
      title="Join the Academic Ecosystem"
      subtitle="Whether you're a student embarking on your final project or an expert mentor guiding the next generation, HeySir provides the tools you need to succeed."
      imageSrc="https://images.unsplash.com/photo-1523240715639-99a808e9956b?auto=format&fit=crop&q=80&w=1000"
      imageAlt="Collaborative university setting"
      icon="diversity_3"
    >
      <div className="flex flex-col gap-3 mb-10 text-center sm:text-left">
        <h2 className="text-4xl font-extrabold tracking-tight text-stitch-on-surface leading-tight">
          Choose Your Path
        </h2>
        <p className="text-stitch-on-surface-variant text-lg font-medium leading-relaxed max-w-[380px] mx-auto sm:mx-0">
          Select your institutional role to begin the registration process.
        </p>
      </div>

      <div className="flex flex-col gap-6">
        <Link
          to="/auth/signup/student"
          className="flex items-center gap-6 p-6 bg-white border border-stitch-outline-variant/30 rounded-[24px] hover:border-stitch-primary hover:shadow-2xl hover:shadow-stitch-primary/10 transition-all duration-500 group active:scale-[0.98] relative overflow-hidden"
        >
          {/* Animated Background Blur */}
          <div className="absolute -right-4 -top-4 w-24 h-24 bg-stitch-primary/5 rounded-full blur-2xl group-hover:bg-stitch-primary/10 transition-colors" />
          
          <div className="w-16 h-16 rounded-2xl bg-stitch-primary-container/10 text-stitch-primary flex items-center justify-center group-hover:scale-110 group-hover:bg-stitch-primary group-hover:text-white transition-all duration-500 shadow-sm z-10">
            <span className="material-symbols-outlined text-[36px] font-light">school</span>
          </div>
          
          <div className="flex-1 z-10">
            <h3 className="font-bold text-xl text-stitch-on-surface mb-1 group-hover:text-stitch-primary transition-colors">Student</h3>
            <p className="text-sm text-stitch-on-surface-variant/80 leading-relaxed font-medium">
              Find advisors, submit proposals, and manage your PFE milestones.
            </p>
          </div>
          
          <div className="w-10 h-10 rounded-full bg-stitch-surface-container-low flex items-center justify-center group-hover:bg-stitch-primary/10 transition-all">
            <span className="material-symbols-outlined text-stitch-outline-variant group-hover:text-stitch-primary group-hover:translate-x-1 transition-all text-[20px]">
              arrow_forward
            </span>
          </div>
        </Link>

        <Link
          to="/auth/signup/advisor"
          className="flex items-center gap-6 p-6 bg-white border border-stitch-outline-variant/30 rounded-[24px] hover:border-stitch-secondary hover:shadow-2xl hover:shadow-stitch-secondary/10 transition-all duration-500 group active:scale-[0.98] relative overflow-hidden"
        >
          {/* Animated Background Blur */}
          <div className="absolute -right-4 -top-4 w-24 h-24 bg-stitch-secondary/5 rounded-full blur-2xl group-hover:bg-stitch-secondary/10 transition-colors" />

          <div className="w-16 h-16 rounded-2xl bg-stitch-secondary-container/10 text-stitch-secondary flex items-center justify-center group-hover:scale-110 group-hover:bg-stitch-secondary group-hover:text-white transition-all duration-500 shadow-sm z-10">
            <span className="material-symbols-outlined text-[36px] font-light">engineering</span>
          </div>
          
          <div className="flex-1 z-10">
            <h3 className="font-bold text-xl text-stitch-on-surface mb-1 group-hover:text-stitch-secondary transition-colors">Advisor</h3>
            <p className="text-sm text-stitch-on-surface-variant/80 leading-relaxed font-medium">
              Supervise projects, mentor students, and validate academic progress.
            </p>
          </div>

          <div className="w-10 h-10 rounded-full bg-stitch-surface-container-low flex items-center justify-center group-hover:bg-stitch-secondary/10 transition-all">
            <span className="material-symbols-outlined text-stitch-outline-variant group-hover:text-stitch-secondary group-hover:translate-x-1 transition-all text-[20px]">
              arrow_forward
            </span>
          </div>
        </Link>
      </div>

      <div className="mt-12 text-center">
        <p className="text-sm text-stitch-on-surface-variant">
          Already registered?{' '}
          <Link 
            to="/auth/login" 
            className="text-stitch-primary hover:text-stitch-primary-container font-bold transition-colors underline underline-offset-4"
          >
            Access your account
          </Link>
        </p>
      </div>
    </AuthLayout>
  );
};
