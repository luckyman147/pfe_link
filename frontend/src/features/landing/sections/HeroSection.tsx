import React from 'react';
import { HeroContent } from './Hero/HeroContent';
import { HeroIllustration } from './Hero/HeroIllustration';
import { HeroBackground } from './Hero/HeroBackground';

export const HeroSection: React.FC = () => (
  <section className="relative bg-linear-to-br from-primary-600 to-blue-700 min-h-[85vh] flex items-center overflow-hidden">
    <HeroBackground />
    
    <div className="container mx-auto px-6 py-20 relative z-10">
      <div className="flex flex-col lg:flex-row items-center gap-16">
        <HeroContent />
        <HeroIllustration />
      </div>
    </div>
    
    {/* Wave Bottom */}
    <svg className="absolute bottom-0 left-0 w-full" viewBox="0 0 1440 120" fill="none">
      <path d="M0,80 C360,120 720,40 1080,80 C1260,100 1380,60 1440,80 L1440,120 L0,120 Z" fill="white"/>
    </svg>
  </section>
);

export default HeroSection;
