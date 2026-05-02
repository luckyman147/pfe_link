import React from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Send, FileText, MessageSquare, Loader2, ArrowRight, Briefcase } from 'lucide-react';
import type { AdvisorProfile } from '@/features/academic/types/academic.types';

const selectionSchema = z.object({
  projectTitle: z.string().min(5, "Project title must be at least 5 characters"),
  message: z.string().min(20, "Please provide a more detailed message for the advisor"),
});

type SelectionFormData = z.infer<typeof selectionSchema>;

interface SelectionFormProps {
  selectedAdvisor: AdvisorProfile | null;
  submitting: boolean;
  onSubmit: (data: SelectionFormData) => Promise<void>;
}

export const SelectionForm: React.FC<SelectionFormProps> = ({ selectedAdvisor, submitting, onSubmit }) => {
  const { register, handleSubmit, formState: { errors }, reset } = useForm<SelectionFormData>({
    resolver: zodResolver(selectionSchema),
  });

  const handleFormSubmit = async (data: SelectionFormData) => {
    await onSubmit(data);
    reset();
  };

  if (!selectedAdvisor) {
    return (
      <div className="h-full bg-gray-50 rounded-3xl border-2 border-dashed border-gray-200 p-12 text-center flex flex-col items-center justify-center min-h-[400px]">
        <Briefcase className="w-12 h-12 text-gray-300 mb-4" />
        <p className="text-gray-400 font-bold italic tracking-wide">Select an advisor from the list to start your PFE request</p>
      </div>
    );
  }

  return (
    <div className="lg:sticky lg:top-8 bg-white rounded-3xl shadow-xl border border-gray-100 p-8 animate-fade-in">
      <div className="flex items-center gap-3 mb-6">
        <div className="w-10 h-10 bg-primary-100 rounded-xl flex items-center justify-center">
          <Send className="w-5 h-5 text-primary-600" />
        </div>
        <h3 className="text-xl font-bold text-navy-900 italic">Request {selectedAdvisor.fullName.split(' ')[0]}</h3>
      </div>

      <form onSubmit={handleSubmit(handleFormSubmit)} className="space-y-5">
        <div className="space-y-2">
          <label className="text-sm font-bold text-gray-700 uppercase tracking-wider pl-1">Project Title</label>
          <div className="relative">
            <FileText className="absolute left-4 top-3.5 w-5 h-5 text-gray-400" />
            <input 
              {...register('projectTitle')}
              placeholder="e.g. AI-driven logistics optimization"
              className="w-full pl-12 pr-4 py-3.5 bg-gray-50 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-primary-500/10 focus:border-primary-500 transition-all font-medium"
            />
          </div>
          {errors.projectTitle && <p className="text-xs text-red-500 font-bold pl-1 uppercase tracking-tight">{errors.projectTitle.message}</p>}
        </div>

        <div className="space-y-2">
          <label className="text-sm font-bold text-gray-700 uppercase tracking-wider pl-1">Your Message</label>
          <div className="relative">
            <MessageSquare className="absolute left-4 top-3.5 w-5 h-5 text-gray-400" />
            <textarea 
              {...register('message')}
              rows={5}
              placeholder="Describe your project idea and why you want to work with this advisor..."
              className="w-full pl-12 pr-4 py-3.5 bg-gray-50 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-primary-500/10 focus:border-primary-500 transition-all font-medium resize-none"
            />
          </div>
          {errors.message && <p className="text-xs text-red-500 font-bold pl-1 uppercase tracking-tight">{errors.message.message}</p>}
        </div>

        <button 
          type="submit"
          disabled={submitting}
          className="w-full py-4 bg-linear-to-r from-primary-600 to-sky-600 text-white font-black rounded-2xl shadow-lg shadow-primary-500/30 hover:shadow-xl hover:-translate-y-1 transition-all disabled:opacity-50 disabled:translate-y-0 group"
        >
          {submitting ? (
            <Loader2 className="w-6 h-6 animate-spin mx-auto" />
          ) : (
            <span className="flex items-center justify-center gap-2">
              Submit Request
              <ArrowRight className="w-5 h-5 group-hover:translate-x-1 transition-transform" />
            </span>
          )}
        </button>
      </form>
    </div>
  );
};
