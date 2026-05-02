import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { ArrowLeft, Loader2 } from 'lucide-react';
import { academicService } from '@/features/academic/services/academic.service';
import type { CreateFacultyRequest } from '@/features/academic/types/academic.types';
import { FacultyBasicInfo } from '../components/CreateFaculty/FacultyBasicInfo';
import { FacultyLocationInfo } from '../components/CreateFaculty/FacultyLocationInfo';

export const CreateFacultyPage = () => {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    
    const { register, handleSubmit, setValue, formState: { errors }, watch } = useForm<CreateFacultyRequest>({
        defaultValues: { path: '', displayName: '' }
    });

    const onSubmit = async (data: CreateFacultyRequest) => {
        setLoading(true);
        setError(null);
        try {
            await academicService.createFaculty(data);
            alert('Faculty registration request submitted successfully!');
            navigate('/admin');
        } catch (err: any) {
            setError(err.response?.data?.message || 'Failed to create faculty');
        } finally { setLoading(false); }
    };

    const handleAddressSelect = (address: any) => {
        setValue('path', address.path);
        setValue('displayName', address.displayName);
    };

    return (
        <div className="min-h-screen bg-linear-to-br from-primary-50 via-sky-50 to-white py-12 px-4 shadow-inner">
            <div className="max-w-5xl mx-auto">
                <button onClick={() => navigate('/admin')} className="mb-8 flex items-center gap-2 text-primary-600 font-medium hover:text-primary-700 transition-all">
                    <ArrowLeft className="w-4 h-4" /> Back to Dashboard
                </button>
                <div className="text-center mb-10">
                    <h1 className="text-3xl font-extrabold text-navy-900 sm:text-4xl">Register New Faculty</h1>
                    <p className="mt-4 text-lg text-gray-600">Enter institution details and select precise location using the map.</p>
                </div>
                <form onSubmit={handleSubmit(onSubmit)} className="space-y-8">
                    <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                        <FacultyBasicInfo register={register} errors={errors} />
                        <FacultyLocationInfo register={register} watch={watch} onAddressSelect={handleAddressSelect} />
                    </div>
                    {error && <div className="p-4 bg-red-50 border border-red-200 text-red-700 rounded-2xl text-center animate-shake">{error}</div>}
                    <div className="flex justify-center pt-4">
                        <button type="submit" disabled={loading || !watch('displayName')}
                            className="inline-flex items-center gap-3 px-10 py-4 bg-linear-to-r from-primary-600 to-sky-600 text-white font-bold rounded-2xl shadow-xl hover:-translate-y-1 transition-all disabled:opacity-50">
                            {loading ? <Loader2 className="w-5 h-5 animate-spin" /> : 'Complete Institution Registration'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default CreateFacultyPage;
