import React, { useState } from 'react';

type Role = 'student' | 'advisor';

const STEPS: Record<Role, { step: string; title: string; description: string }[]> = {
  student: [
    { step: '01', title: 'Create Account', description: 'Sign up as a student and complete your profile with your faculty and project details.' },
    { step: '02', title: 'Await Faculty Approval', description: 'Your registration is reviewed by your faculty administrator. You\'ll be notified once approved.' },
    { step: '03', title: 'Choose Your Advisor', description: 'Browse approved advisors in your faculty and send a selection request to your preferred match.' },
  ],
  advisor: [
    { step: '01', title: 'Create Account', description: 'Sign up as an advisor and set your specialty, capacity, and faculty affiliation.' },
    { step: '02', title: 'Await Faculty Approval', description: 'Your profile is reviewed by the faculty admin before becoming visible to students.' },
    { step: '03', title: 'Find Your Students', description: 'Review incoming student requests and approve the ones that match your expertise and availability.' },
  ],
};

export const HowItWorksSection: React.FC = () => {
  const [role, setRole] = useState<Role>('student');
  const steps = STEPS[role];

  return (
    <section className="py-20 bg-navy-800">
      <div className="container mx-auto px-6">
        <div className="text-center mb-10">
          <h2 className="text-3xl md:text-4xl font-extrabold text-white mb-4">How HeySir Works</h2>
          <p className="text-white/60 text-lg max-w-2xl mx-auto mb-8">
            A clear, transparent process from registration to collaboration
          </p>
          <div className="inline-flex bg-navy-900/60 border border-white/10 rounded-xl p-1.5 gap-1">
            {(['student', 'advisor'] as Role[]).map((r) => (
              <button
                key={r}
                onClick={() => setRole(r)}
                className={`px-6 py-2 rounded-lg text-sm font-semibold capitalize transition-all duration-300 ${
                  role === r
                    ? 'bg-linear-to-r from-primary-400 to-sky-400 text-white shadow-lg'
                    : 'text-white/50 hover:text-white'
                }`}
              >
                {r === 'student' ? ' Student' : ' Advisor'}
              </button>
            ))}
          </div>
        </div>

        <div className="grid md:grid-cols-3 gap-8 max-w-4xl mx-auto">
          {steps.map((item, i) => (
            <div key={i} className="relative text-center animate-fade-in-up" style={{ animationDelay: `${i * 150}ms` }}>
              <div className="inline-flex items-center justify-center w-16 h-16 bg-gradient-to-br from-primary-400 to-sky-400 rounded-2xl text-white font-bold text-xl mb-4 shadow-lg">
                {item.step}
              </div>
              <h3 className="text-xl font-bold text-white mb-2">{item.title}</h3>
              <p className="text-white/60">{item.description}</p>
              {i < 2 && (
                <div className="hidden md:block absolute top-8 left-[60%] w-[80%] h-0.5 bg-linear-to-r from-primary-400/50 to-transparent" />
              )}
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default HowItWorksSection;
