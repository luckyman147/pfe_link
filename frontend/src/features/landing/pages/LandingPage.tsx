import React from 'react';
import { CTASection, FeaturesSection, HeroSection, HowItWorksSection, StatsSection } from '../sections';

export const LandingPage: React.FC = () => {
  return (
    <div className="min-h-screen">
      <HeroSection />
      <StatsSection />
      <FeaturesSection />
      <HowItWorksSection />
      <CTASection />
    </div>
  );
};

export default LandingPage;
