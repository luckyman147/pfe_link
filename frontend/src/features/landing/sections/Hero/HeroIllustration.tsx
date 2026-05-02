import React from 'react';
import { Users, Star, Zap } from 'lucide-react';

export const HeroIllustration: React.FC = () => (
  <div className="flex-1 relative animate-fade-in-right delay-200">
    <div className="relative max-w-md mx-auto">
      {/* Main Card */}
      <div className="bg-white/10 backdrop-blur-xl rounded-3xl p-8 border border-white/20 shadow-2xl">
        <div className="flex items-center gap-4 mb-6">
          <div className="w-16 h-16 bg-linear-to-br from-primary-400 to-sky-400 rounded-2xl flex items-center justify-center">
            <Users className="w-8 h-8 text-white" />
          </div>
          <div>
            <h3 className="text-white font-bold text-lg">Find Your Match</h3>
            <p className="text-white/60 text-sm">Connect in seconds</p>
          </div>
        </div>
        
        {/* Advisor Cards */}
        <div className="space-y-3">
          {[
            { name: "Dr. Ahmed Ben Ali", role: "AI & Machine Learning", rating: 4.9 },
            { name: "Prof. Fatma Souissi", role: "Web Development", rating: 4.8 },
            { name: "Dr. Mohamed Tounsi", role: "Mobile Apps", rating: 4.7 },
          ].map((advisor, i) => (
            <div key={i} className="flex items-center gap-3 p-3 bg-white/10 rounded-xl hover:bg-white/20 transition-colors cursor-pointer">
              <div className="w-10 h-10 bg-linear-to-br from-sky-300 to-blue-400 rounded-full" />
              <div className="flex-1 text-white">
                <p className="font-medium text-sm">{advisor.name}</p>
                <p className="text-white/60 text-xs">{advisor.role}</p>
              </div>
              <div className="flex items-center gap-1 text-yellow-300 text-sm">
                <Star className="w-4 h-4 fill-current" />
                {advisor.rating}
              </div>
            </div>
          ))}
        </div>
      </div>
      
      {/* Badges */}
      <div className="absolute -top-4 -right-4 bg-linear-to-r from-yellow-400 to-orange-400 text-white px-4 py-2 rounded-full font-bold text-sm shadow-lg animate-float">100% Free</div>
      <div className="absolute -bottom-4 -left-4 bg-white rounded-2xl px-5 py-3 shadow-xl animate-float delay-300">
        <div className="flex items-center gap-2">
          <Zap className="w-5 h-5 text-yellow-500" />
          <span className="font-bold text-navy-800">98%</span>
          <span className="text-gray-500 text-sm">Match Rate</span>
        </div>
      </div>
    </div>
  </div>
);
