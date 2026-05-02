import { useEffect, useState } from 'react';
import { useAuth } from '@/features/auth';
import { academicService } from '@/features/academic/services/academic.service';
import type { AdvisorProfile, StudentProfile } from '@/features/academic/types/academic.types';
import { Briefcase, MessageSquare, FileText, Send, AlertCircle, Loader2, Search, ArrowRight } from 'lucide-react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';

const selectionSchema = z.object({
  projectTitle: z.string().min(5, "Project title must be at least 5 characters"),
  message: z.string().min(20, "Please provide a more detailed message for the advisor"),
});

type SelectionFormData = z.infer<typeof selectionSchema>;

export const AdvisorSelection = () => {
  const { user } = useAuth();
  const [profile, setProfile] = useState<StudentProfile | null>(null);
  const [advisors, setAdvisors] = useState<AdvisorProfile[]>([]);
  const [selectedAdvisor, setSelectedAdvisor] = useState<AdvisorProfile | null>(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');

  const { register, handleSubmit, formState: { errors }, reset } = useForm<SelectionFormData>({
    resolver: zodResolver(selectionSchema),
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        const studentProfile = await academicService.getMyProfile();
        setProfile(studentProfile);
        
        if (studentProfile.status === 'APPROVED') {
          const advisorList = await academicService.getAdvisors(studentProfile.facultyId);
          setAdvisors(advisorList);
        }
      } catch (error) {
        console.error("Failed to fetch data:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const onSubmit = async (data: SelectionFormData) => {
    if (!profile || !selectedAdvisor) return;

    setSubmitting(true);
    try {
      await academicService.submitSelection({
        studentUserId: user?.id || '',
        advisorProfileId: selectedAdvisor.id,
        facultyId: profile.facultyId,
        projectTitle: data.projectTitle,
        message: data.message
      });
      alert("Selection request submitted successfully!");
      setSelectedAdvisor(null);
      reset();
    } catch (error: any) {
      console.error(error);
      alert(error.response?.data?.message || "Failed to submit selection request");
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-[60vh] flex flex-col items-center justify-center space-y-4">
        <Loader2 className="w-10 h-10 text-primary-500 animate-spin" />
        <p className="text-gray-500 font-medium tracking-wide">Loading your academic profile...</p>
      </div>
    );
  }

  if (profile?.status !== 'APPROVED') {
    return (
      <div className="max-w-2xl mx-auto mt-12 p-8 bg-white rounded-3xl shadow-xl border border-gray-100 text-center animate-fade-in">
        <div className="w-20 h-20 bg-amber-50 rounded-2xl flex items-center justify-center mx-auto mb-6">
          <AlertCircle className="w-10 h-10 text-amber-500" />
        </div>
        <h2 className="text-2xl font-bold text-navy-900 mb-4">Pending Approval</h2>
        <p className="text-gray-600 leading-relaxed">
          Your student profile is currently <strong>{profile?.status.toLowerCase()}</strong>. 
          You will be able to select an advisor once your faculty approves your registration.
        </p>
      </div>
    );
  }

  const filteredAdvisors = advisors.filter(advisor => 
    advisor.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    advisor.department?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    advisor.specialization?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="max-w-6xl mx-auto px-4 py-12">
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

      <div className="grid lg:grid-cols-3 gap-8">
        {/* Advisor List */}
        <div className="lg:col-span-2 space-y-4 max-h-[70vh] overflow-y-auto pr-2 custom-scrollbar">
          {filteredAdvisors.length > 0 ? (
            filteredAdvisors.map(advisor => (
              <div 
                key={advisor.id} 
                onClick={() => setSelectedAdvisor(advisor)}
                className={`p-6 bg-white rounded-2xl border-2 transition-all cursor-pointer group ${
                  selectedAdvisor?.id === advisor.id 
                  ? 'border-primary-500 shadow-lg shadow-primary-500/10' 
                  : 'border-transparent hover:border-gray-200 shadow-sm'
                }`}
              >
                <div className="flex items-start gap-4">
                  <div className={`w-14 h-14 rounded-xl flex items-center justify-center text-xl font-bold shadow-sm ${
                    selectedAdvisor?.id === advisor.id ? 'bg-primary-500 text-white' : 'bg-gray-100 text-gray-500'
                  }`}>
                    {advisor.fullName.charAt(0)}
                  </div>
                  <div className="flex-1">
                    <h3 className="text-xl font-bold text-navy-900 group-hover:text-primary-600 transition-colors">{advisor.fullName}</h3>
                    <div className="flex flex-wrap gap-4 mt-2">
                       <span className="inline-flex items-center gap-1.5 text-sm text-gray-500 font-medium">
                         <Briefcase className="w-4 h-4" />
                         {advisor.department || 'All Departments'}
                       </span>
                       <span className="inline-flex items-center gap-1.5 text-sm text-gray-500 font-medium">
                         <FileText className="w-4 h-4" />
                         {advisor.specialization || 'General Supervision'}
                       </span>
                    </div>
                  </div>
                  <div className={`w-6 h-6 rounded-full border-2 flex items-center justify-center transition-all ${
                    selectedAdvisor?.id === advisor.id ? 'border-primary-500 bg-primary-50' : 'border-gray-200'
                  }`}>
                    {selectedAdvisor?.id === advisor.id && <div className="w-3 h-3 bg-primary-500 rounded-full" />}
                  </div>
                </div>
              </div>
            ))
          ) : (
            <div className="p-12 text-center bg-gray-50 rounded-3xl border-2 border-dashed border-gray-200">
              <p className="text-gray-400 font-medium">No advisors found matching your search</p>
            </div>
          )}
        </div>

        {/* Selection Form */}
        <div className="lg:sticky lg:top-8">
          {selectedAdvisor ? (
            <div className="bg-white rounded-3xl shadow-xl border border-gray-100 p-8 animate-fade-in">
              <div className="flex items-center gap-3 mb-6">
                <div className="w-10 h-10 bg-primary-100 rounded-xl flex items-center justify-center">
                  <Send className="w-5 h-5 text-primary-600" />
                </div>
                <h3 className="text-xl font-bold text-navy-900 italic">Request {selectedAdvisor.fullName.split(' ')[0]}</h3>
              </div>

              <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
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
          ) : (
            <div className="h-full bg-gray-50 rounded-3xl border-2 border-dashed border-gray-200 p-12 text-center flex flex-col items-center justify-center">
              <Briefcase className="w-12 h-12 text-gray-300 mb-4" />
              <p className="text-gray-400 font-bold italic tracking-wide">Select an advisor from the list to start your PFE request</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default AdvisorSelection;
