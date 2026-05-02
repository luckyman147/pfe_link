import React from 'react';
import { Link } from 'react-router-dom';
import { ArrowRight } from 'lucide-react';

export const CTASection: React.FC = () => (
  <section className="py-20 bg-linear-to-br from-primary-500 via-blue-500 to-sky-500 relative overflow-hidden">
    {/* Background Pattern */}
    <div className="absolute inset-0 opacity-10">
      <div className="absolute inset-0" style={{
        backgroundImage: `radial-gradient(circle at 1px 1px, white 1px, transparent 0)`,
        backgroundSize: '40px 40px'
      }} />
    </div>
    
    <div className="container mx-auto px-6 relative z-10 text-center">
      <h2 className="text-3xl md:text-5xl font-extrabold text-white mb-6 animate-fade-in-up">
        Ready to Start Your PFE Journey?
      </h2>
      <p className="text-white/80 text-lg md:text-xl mb-10 max-w-2xl mx-auto animate-fade-in-up delay-100">
        Join hundreds of students who found their perfect advisor through HeySir
      </p>
      <div className="flex flex-col sm:flex-row gap-4 justify-center animate-fade-in-up delay-200">
        <Link 
          to="/auth/signup"
          className="inline-flex items-center justify-center gap-2 px-10 py-4 bg-white text-primary-600 font-bold rounded-full shadow-xl hover:shadow-2xl hover:-translate-y-1 transition-all"
        >
          Create Free Account
          <ArrowRight className="w-5 h-5" />
        </Link>
        <Link 
          to="/admin/faculties/new"
          className="inline-flex items-center justify-center gap-2 px-10 py-4 bg-white/10 backdrop-blur-sm text-white font-bold rounded-full border border-white/20 hover:bg-white/20 hover:-translate-y-1 transition-all"
        >
          Register Faculty
        </Link>
        <Link 
          to="/login"
          className="inline-flex items-center justify-center gap-2 px-10 py-4 bg-transparent text-white font-semibold rounded-full border-2 border-white/30 hover:bg-white/10 transition-all"
        >
          Sign In
        </Link>
      </div>
    </div>
  </section>
);

export default CTASection;
