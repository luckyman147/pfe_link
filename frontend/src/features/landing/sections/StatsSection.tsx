import React from 'react';

export const StatsSection: React.FC = () => (
  <section className="py-16 bg-white">
    <div className="container mx-auto px-6">
      <div className="grid grid-cols-2 md:grid-cols-4 gap-8">
        {[
          { value: "500+", label: "Active Students" },
          { value: "100+", label: "Expert Advisors" },
          { value: "95%", label: "Success Rate" },
          { value: "24/7", label: "Support" },
        ].map((stat, i) => (
          <div key={i} className="text-center animate-fade-in-up" style={{ animationDelay: `${i * 100}ms` }}>
            <div className="text-3xl md:text-4xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-primary-500 to-sky-500 mb-2">
              {stat.value}
            </div>
            <div className="text-gray-500 font-medium">{stat.label}</div>
          </div>
        ))}
      </div>
    </div>
  </section>
);

export default StatsSection;
