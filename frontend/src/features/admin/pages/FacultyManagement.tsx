import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { DashboardLayout } from '@/components/layout/DashboardLayout';
import api from '@/shared/services/api';
import { 
  Plus, 
  Search, 
  Edit2, 
  Trash2, 
  School, 
  Building2,
  ChevronRight,
  ChevronLeft
} from 'lucide-react';

export const FacultyManagement = () => {
    const navigate = useNavigate();
    const [faculties, setFaculties] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);
    const [searchQuery, setSearchQuery] = useState('');

    const fetchFaculties = async () => {
        try {
            const response = await api.get('/api/faculties');
            setFaculties(response.data);
        } catch (error) {
            console.error("Failed to fetch faculties", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchFaculties();
    }, []);

    const filteredFaculties = faculties.filter(faculty => 
        faculty.name.toLowerCase().includes(searchQuery.toLowerCase())
    );

    const handleDelete = async (id: string) => {
        if (!confirm("Are you sure you want to delete this faculty? This action cannot be undone.")) return;
        try {
            await api.delete(`/api/admin/faculties/${id}`);
            setFaculties(prev => prev.filter(f => f.id !== id));
        } catch (error) {
            console.error("Failed to delete faculty", error);
        }
    };

    return (
        <DashboardLayout>
            <div className="max-w-[1440px] mx-auto space-y-8">
                {/* Header */}
                <div className="flex flex-col md:flex-row md:items-end justify-between gap-6">
                    <div className="space-y-1">
                        <h1 className="text-3xl font-bold text-slate-900 tracking-tight">Faculty Management</h1>
                        <p className="text-slate-500 font-medium">Manage academic structures, departments, and departmental heads.</p>
                    </div>
                    <button 
                        onClick={() => navigate('/admin/faculties/new')}
                        className="bg-primary text-white font-semibold px-4 py-2.5 rounded-xl hover:bg-primary/90 transition-all shadow-lg shadow-primary/20 flex items-center gap-2 active:scale-95"
                    >
                        <Plus className="w-4 h-4" />
                        Add Faculty
                    </button>
                </div>

                {/* Table & Search Container */}
                <div className="bg-white border border-slate-100 rounded-3xl shadow-sm overflow-hidden border-slate-200/60">
                    {/* Search Bar */}
                    <div className="px-6 py-5 border-b border-slate-100 bg-slate-50/50 flex justify-between items-center">
                        <div className="relative w-full md:w-80">
                            <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                            <input 
                                type="text"
                                placeholder="Search faculties..."
                                value={searchQuery}
                                onChange={(e) => setSearchQuery(e.target.value)}
                                className="w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all text-sm font-medium"
                            />
                        </div>
                        <div className="hidden md:flex items-center gap-2">
                            <span className="bg-slate-100 text-slate-500 font-bold text-[10px] px-2.5 py-1 rounded-lg uppercase tracking-wider">
                                {filteredFaculties.length} Total Units
                            </span>
                        </div>
                    </div>

                    <div className="overflow-x-auto">
                        <table className="w-full text-left border-collapse">
                            <thead>
                                <tr className="border-b border-slate-100">
                                    <th className="px-6 py-4 font-bold text-slate-400 text-[10px] uppercase tracking-widest">Faculty Details</th>
                                    <th className="px-6 py-4 font-bold text-slate-400 text-[10px] uppercase tracking-widest text-right">Students</th>
                                    <th className="px-6 py-4 font-bold text-slate-400 text-[10px] uppercase tracking-widest text-right">Advisors</th>
                                    <th className="px-6 py-4 font-bold text-slate-400 text-[10px] uppercase tracking-widest text-right">Actions</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-slate-100">
                                {loading ? (
                                    <tr>
                                        <td colSpan={4} className="py-20 text-center">
                                            <div className="w-10 h-10 border-4 border-primary/20 border-t-primary rounded-full animate-spin mx-auto mb-4" />
                                            <p className="text-slate-400 font-medium italic">Mapping academic units...</p>
                                        </td>
                                    </tr>
                                ) : filteredFaculties.length === 0 ? (
                                    <tr>
                                        <td colSpan={4} className="py-20 text-center space-y-3">
                                            <div className="w-16 h-16 bg-slate-50 rounded-full flex items-center justify-center mx-auto text-slate-300">
                                                <Building2 className="w-8 h-8" />
                                            </div>
                                            <h4 className="font-bold text-slate-900 text-lg">No faculties found</h4>
                                            <p className="text-slate-500 text-sm max-w-xs mx-auto">Start by adding your first university faculty or department.</p>
                                        </td>
                                    </tr>
                                ) : (
                                    filteredFaculties.map((faculty) => (
                                        <tr key={faculty.id} className="hover:bg-slate-50/80 transition-all group">
                                            <td className="px-6 py-4">
                                                <div className="flex items-center gap-4">
                                                    <div className="w-12 h-12 rounded-2xl bg-slate-100 flex items-center justify-center text-slate-400 group-hover:bg-primary/10 group-hover:text-primary transition-colors">
                                                        <School className="w-6 h-6" />
                                                    </div>
                                                    <div>
                                                        <h4 className="font-bold text-slate-900 group-hover:text-primary transition-colors">{faculty.name}</h4>
                                                        <p className="text-xs text-slate-400 font-medium">System ID: {faculty.id}</p>
                                                    </div>
                                                </div>
                                            </td>
                                            <td className="px-6 py-4 text-right font-bold text-slate-600">
                                                {faculty.studentCount || 0}
                                            </td>
                                            <td className="px-6 py-4 text-right font-bold text-slate-600">
                                                {faculty.advisorCount || 0}
                                            </td>
                                            <td className="px-6 py-4 text-right">
                                                <div className="flex items-center justify-end gap-2 opacity-0 group-hover:opacity-100 transition-all">
                                                    <button 
                                                        onClick={() => navigate(`/admin/faculties/edit/${faculty.id}`)}
                                                        className="p-2 bg-slate-50 text-slate-400 rounded-xl hover:bg-primary hover:text-white transition-all active:scale-90"
                                                    >
                                                        <Edit2 className="w-4 h-4" />
                                                    </button>
                                                    <button 
                                                        onClick={() => handleDelete(faculty.id)}
                                                        className="p-2 bg-slate-50 text-slate-400 rounded-xl hover:bg-rose-600 hover:text-white transition-all active:scale-90"
                                                    >
                                                        <Trash2 className="w-4 h-4" />
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>

                    {/* Pagination */}
                    <div className="px-6 py-4 bg-slate-50/50 border-t border-slate-100 flex items-center justify-between">
                        <p className="text-xs font-bold text-slate-400 uppercase tracking-widest">
                            Showing {filteredFaculties.length} entries
                        </p>
                        <div className="flex gap-2">
                            <button className="p-2 bg-white border border-slate-200 rounded-lg text-slate-400 cursor-not-allowed">
                                <ChevronLeft className="w-4 h-4" />
                            </button>
                            <button className="px-4 py-1.5 bg-primary text-white rounded-lg text-xs font-bold shadow-sm">1</button>
                            <button className="p-2 bg-white border border-slate-200 rounded-lg text-slate-400 hover:bg-slate-50 transition-colors">
                                <ChevronRight className="w-4 h-4" />
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </DashboardLayout>
    );
};

export default FacultyManagement;
