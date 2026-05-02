import React from 'react';
import { RequestItem } from './RequestItem';

interface RequestListProps {
  requests: any[];
}

export const RequestList: React.FC<RequestListProps> = ({ requests }) => {
  return (
    <div className="bg-white border border-slate-200 rounded-3xl overflow-hidden shadow-sm">
      <div className="p-6 border-b border-slate-100 flex items-center justify-between bg-slate-50/50">
        <h2 className="font-bold text-slate-900">Pending Review</h2>
        <span className="px-2 py-1 bg-primary/10 text-primary text-[10px] font-black rounded-md uppercase tracking-wider">Priority Sorted</span>
      </div>
      
      <div className="divide-y divide-slate-100">
        {requests.map((request) => (
          <RequestItem key={request.id} request={request} />
        ))}
      </div>

      <div className="p-4 bg-slate-50 text-center">
        <button className="text-xs font-bold text-primary hover:underline uppercase tracking-widest">Load More Requests</button>
      </div>
    </div>
  );
};
