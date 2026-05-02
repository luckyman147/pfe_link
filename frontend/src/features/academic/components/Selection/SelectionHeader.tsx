import React from 'react';
import { Search } from 'lucide-react';

interface SelectionHeaderProps {
  searchTerm: string;
  setSearchTerm: (val: string) => void;
}

export const SelectionHeader: React.FC<SelectionHeaderProps> = ({ searchTerm, setSearchTerm }) => {
  return (
    <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 mb-12">
      <div>
        <h1 className="text-4xl font-black text-navy-900 mb-2 tracking-tight">Advisor Selection</h1>
        <p className="text-gray-500 font-medium">Browse and request a supervisor for your PFE project</p>
      </div>
      
      <div className="relative group">
        <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400 group-focus-within:text-primary-500 transition-colors" />
        <input 
          type="text" 
          placeholder="Search advisors..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="pl-12 pr-6 py-3 bg-white border border-gray-200 rounded-2xl w-full md:w-80 shadow-sm focus:outline-none focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500 transition-all font-medium"
        />
      </div>
    </div>
  );
};
