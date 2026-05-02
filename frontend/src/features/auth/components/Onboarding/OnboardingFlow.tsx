import React from 'react';
import { useTranslation } from 'react-i18next';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useAuth, AccountStatus, UserRole } from '@/features/auth';
import api from '@/shared/services/api';
import { onboardingStudentSchema, onboardingTeacherSchema, type OnboardingStudentInput, type OnboardingTeacherInput } from '@/lib/validations/auth';


export const OnboardingFlow: React.FC = () => {
  const { t } = useTranslation();
  const { user } = useAuth();
  

  const needOnboarding = user?.status === AccountStatus.PENDING;
  
  const [selectedRole, setSelectedRole] = React.useState<UserRole | null>(null);

  const studentForm = useForm<OnboardingStudentInput>({ 
    resolver: zodResolver(onboardingStudentSchema),
    defaultValues: { studentCard: '' }
  });
  const teacherForm = useForm<OnboardingTeacherInput>({ resolver: zodResolver(onboardingTeacherSchema) });

  const studentCardValue = studentForm.watch('studentCard');

  const onStudentSubmit = async (data: OnboardingStudentInput) => {
    await api.post('/api/auth/signup/student', data);
    window.location.reload();
  };

  const onTeacherSubmit = async (data: OnboardingTeacherInput) => {
    await api.post('/api/auth/signup/advisor', data);
    window.location.reload();
  };

  if (!needOnboarding) return null;

  return (
    <div className="fixed inset-0 bg-background/80 flex items-center justify-center p-4">
      <div className="bg-card p-6 rounded-lg shadow-xl max-w-md w-full">
        <h2 className="text-2xl font-bold mb-4">{t('onboarding.title')}</h2>
        
        {!selectedRole ? (
          <div className="grid grid-cols-2 gap-4">
            <button onClick={() => setSelectedRole(UserRole.STUDENT)} className="p-4 border rounded hover:bg-accent transition">Student</button>
            <button onClick={() => setSelectedRole(UserRole.ADVISOR)} className="p-4 border rounded hover:bg-accent transition">Advisor</button>
          </div>
        ) : (
          <div>
            {selectedRole === UserRole.STUDENT ? (
              <form onSubmit={studentForm.handleSubmit(onStudentSubmit)} className="space-y-4">
                <p>{t('onboarding.student_card')}</p>
                
                <div className="flex flex-col items-center gap-4 border-2 border-dashed border-stitch-outline-variant rounded-2xl p-8 bg-stitch-surface-container-lowest transition-all hover:border-stitch-primary/50 group">
                  <div className="w-12 h-12 rounded-full bg-stitch-primary/10 flex items-center justify-center text-stitch-primary mb-2 group-hover:scale-110 transition-transform">
                    <span className="material-symbols-outlined text-[28px]">cloud_upload</span>
                  </div>
                  
                  <div className="text-center">
                    <p className="text-sm font-bold text-stitch-on-surface">Click to upload or drag and drop</p>
                    <p className="text-xs text-stitch-on-surface-variant mt-1">Student ID Card (PDF, PNG, JPG)</p>
                  </div>

                  <input
                    type="file"
                    accept="image/*,application/pdf"
                    onChange={async (e) => {
                      const file = e.target.files?.[0];
                      if (!file) return;

                      const formData = new FormData();
                      formData.append('file', file);

                      try {
                        const response = await api.post('/api/v1/uploads/draft', formData, {
                          headers: { 'Content-Type': 'multipart/form-data' }
                        });
                        // Assuming response.data contains the publicId or url
                        const fileUrl = response.data.url || response.data.id;
                        studentForm.setValue('studentCard', fileUrl);
                      } catch (error: any) {
                        alert(error.response?.data?.message || "Upload failed");
                      }
                    }}
                    className="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
                  />

                  {studentCardValue && (
                    <div className="mt-4 flex items-center gap-2 px-3 py-1.5 bg-emerald-50 text-emerald-700 rounded-full text-xs font-bold animate-in zoom-in-95">
                      <span className="material-symbols-outlined text-[16px]">check_circle</span>
                      Document Uploaded
                    </div>
                  )}
                </div>

                <input type="hidden" {...studentForm.register('studentCard')} />
                <button 
                  type="submit" 
                  disabled={!studentCardValue}
                  className="w-full bg-primary text-primary-foreground p-2 rounded disabled:opacity-50"
                >
                  {t('onboarding.submit')}
                </button>
              </form>
            ) : (
              <form onSubmit={teacherForm.handleSubmit(onTeacherSubmit)} className="space-y-4">
                <p>{t('onboarding.supervision_capacity')}</p>
                <input type="number" {...teacherForm.register('supervisionCapacity', { valueAsNumber: true })} className="w-full p-2 border rounded" />
                <button type="submit" className="w-full bg-primary text-primary-foreground p-2 rounded">{t('onboarding.submit')}</button>
              </form>
            )}
          </div>
        )}
      </div>
    </div>
  );
};
