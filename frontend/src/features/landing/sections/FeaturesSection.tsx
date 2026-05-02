import React from 'react';
import { Link } from 'react-router-dom';
import { ArrowRight, GraduationCap, Briefcase, CheckCircle2 } from 'lucide-react';

export const FeaturesSection: React.FC = () => (
  <section className="py-20 bg-gradient-to-b from-white to-sky-50">
    <div className="container mx-auto px-6">
      <div className="text-center mb-16 animate-fade-in-up">
        <h2 className="text-3xl md:text-4xl font-extrabold text-navy-800 mb-4">
          Everything You Need for Your PFE
        </h2>
        <p className="text-gray-500 text-lg max-w-2xl mx-auto">
          HeySir provides all the tools students and advisors need to collaborate effectively
        </p>
      </div>
      
      <div className="grid md:grid-cols-2 gap-8 max-w-5xl mx-auto">
        <FeatureCard
          icon={<GraduationCap className="w-8 h-8 text-white" />}
          iconBg="from-primary-500 to-blue-500"
          shadowColor="shadow-primary-500/30"
          title="For Students"
          features={[
            "Browse and filter available advisors",
            "Submit applications with your project idea",
            "Track your application status in real-time",
            "Get approved faster with smart matching",
            "Communicate directly with your advisor",
          ]}
          linkTo="/auth/signup/student"
          linkText="Join as Student"
          linkColor="text-primary-600"
          delay="delay-100"
        />
        
        <FeatureCard
          icon={<Briefcase className="w-8 h-8 text-white" />}
          iconBg="from-sky-400 to-cyan-500"
          shadowColor="shadow-sky-500/30"
          title="For Advisors"
          features={[
            "Set your availability and capacity",
            "Define your areas of expertise",
            "Review student applications easily",
            "Accept or decline with one click",
            "Manage all your students in one place",
          ]}
          linkTo="/auth/signup/advisor"
          linkText="Join as Advisor"
          linkColor="text-sky-600"
          delay="delay-200"
        />
      </div>
    </div>
  </section>
);

interface FeatureCardProps {
  icon: React.ReactNode;
  iconBg: string;
  shadowColor: string;
  title: string;
  features: string[];
  linkTo: string;
  linkText: string;
  linkColor: string;
  delay: string;
}

const FeatureCard: React.FC<FeatureCardProps> = ({
  icon, iconBg, shadowColor, title, features, linkTo, linkText, linkColor, delay
}) => (
  <div className={`bg-white rounded-3xl p-8 shadow-xl border border-gray-100 card-hover animate-fade-in-up ${delay}`}>
    <div className={`w-16 h-16 bg-gradient-to-br ${iconBg} rounded-2xl flex items-center justify-center mb-6 shadow-lg ${shadowColor}`}>
      {icon}
    </div>
    
    <h3 className="text-2xl font-bold text-navy-800 mb-4">{title}</h3>
    
    <div className="space-y-4">
      {features.map((feature, i) => (
        <div key={i} className="flex items-start gap-3">
          <CheckCircle2 className="w-5 h-5 text-green-500 shrink-0 mt-0.5" />
          <span className="text-gray-600">{feature}</span>
        </div>
      ))}
    </div>
    
    <Link 
      to={linkTo}
      className={`mt-8 inline-flex items-center gap-2 ${linkColor} font-semibold hover:gap-3 transition-all`}
    >
      {linkText}
      <ArrowRight className="w-4 h-4" />
    </Link>
  </div>
);

export default FeaturesSection;
