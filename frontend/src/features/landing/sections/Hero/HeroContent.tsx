import React from 'react';
import { Link } from 'react-router-dom';
import { ArrowRight, Sparkles } from 'lucide-react';

export const HeroContent: React.FC = () => (
  <div className="flex-1 text-center lg:text-left animate-fade-in-left">
    <div className="inline-flex items-center gap-2 px-4 py-2 bg-white/10 backdrop-blur-sm rounded-full text-white/90 text-sm font-medium mb-6">
      <Sparkles className="w-4 h-4" />
      Tunisia's #1 PFE Management Platform
    </div>
    
    <h1 className="text-4xl md:text-5xl lg:text-6xl font-extrabold text-white leading-tight mb-6">
      Connect with the
      <span className="block text-transparent bg-clip-text bg-linear-to-r from-yellow-300 to-orange-300">
        Perfect Advisor
      </span>
      for Your PFE
    </h1>
    
    <p className="text-lg md:text-xl text-white/80 mb-8 max-w-xl mx-auto lg:mx-0">
      HeySir bridges the gap between students and advisors. 
      Find mentors, manage projects, and succeed in your final year project.
    </p>
    
    <div className="flex flex-col sm:flex-row flex-wrap gap-4 justify-center lg:justify-start">
      <Link 
        to="/auth/signup" 
        className="inline-flex items-center justify-center gap-2 px-8 py-4 bg-white text-primary-600 font-bold rounded-full shadow-xl shadow-black/20 hover:shadow-2xl hover:-translate-y-1 active:translate-y-0 transition-all"
      >
        Get Started Free
        <ArrowRight className="w-5 h-5" />
      </Link>
      <Link 
        to="/admin/faculties/new" 
        className="inline-flex items-center justify-center gap-2 px-8 py-4 bg-primary-600 text-white font-bold rounded-full shadow-xl shadow-primary-500/20 hover:shadow-2xl hover:-translate-y-1 active:translate-y-0 transition-all"
      >
        Create Faculty
      </Link>
      <Link 
        to="/how-it-works" 
        className="inline-flex items-center justify-center gap-2 px-8 py-4 bg-white/10 backdrop-blur-sm text-white font-semibold rounded-full border border-white/20 hover:bg-white/20 transition-all"
      >
        Learn More
      </Link>
    </div>
    
    <div className="flex items-center gap-6 mt-10 justify-center lg:justify-start text-white/80 text-sm">
      <div className="flex -space-x-3">
        {[...Array(4)].map((_, i) => (
          <div key={i} className="w-10 h-10 rounded-full border-2 border-white bg-linear-to-br from-sky-400 to-blue-500" />
        ))}
      </div>
      <div><span className="font-bold text-white">500+</span> students already joined</div>
    </div>
  </div>
);
