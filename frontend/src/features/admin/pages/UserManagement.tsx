import { useEffect, useState } from 'react';
import { DashboardLayout } from '@/components/layout/DashboardLayout';
import api from '@/shared/services/api';
import { 
  UserPlus, 
  Download, 
  Search, 
  Edit2, 
  Ban
} from 'lucide-react';

export const UserManagement = () => {
    const [users, setUsers] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);
    const [filter, setFilter] = useState('all');
    const [searchQuery, setSearchQuery] = useState('');

    const fetchUsers = async () => {
        try {
            const response = await api.get('/api/admin/users');
            setUsers(response.data);
        } catch (error) {
            console.error("Failed to fetch users", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    const filteredUsers = users.filter(user => {
        const matchesRole = filter === 'all' || user.role.toLowerCase() === filter.toLowerCase();
        const matchesSearch = user.fullName.toLowerCase().includes(searchQuery.toLowerCase()) || 
                             user.email.toLowerCase().includes(searchQuery.toLowerCase());
        return matchesRole && matchesSearch;
    });

    const getRoleBadge = (role: string) => {
        switch (role.toUpperCase()) {
            case 'ADMIN':
                return <span className="inline-flex items-center px-2.5 py-0.5 rounded-full bg-rose-50 text-rose-600 border border-rose-100 font-bold text-[10px] uppercase tracking-wider">Administrator</span>;
            case 'ADVISOR':
                return <span className="inline-flex items-center px-2.5 py-0.5 rounded-full bg-blue-50 text-blue-600 border border-blue-100 font-bold text-[10px] uppercase tracking-wider">Advisor</span>;
            case 'STUDENT':
                return <span className="inline-flex items-center px-2.5 py-0.5 rounded-full bg-slate-50 text-slate-600 border border-slate-200 font-bold text-[10px] uppercase tracking-wider">Student</span>;
            default:
                return <span className="inline-flex items-center px-2.5 py-0.5 rounded-full bg-slate-50 text-slate-400 border border-slate-200 font-bold text-[10px] uppercase tracking-wider">{role}</span>;
        }
    };



    return (
        <DashboardLayout>
            <div className="max-w-[1440px] mx-auto space-y-8">
                {/* Header */}
                <div className="flex flex-col md:flex-row md:items-end justify-between gap-6">
                    <div className="space-y-1">
                        <h1 className="text-3xl font-bold text-slate-900 tracking-tight">User Management</h1>
                        <p className="text-slate-500 font-medium">Oversee system access and manage permissions for all users.</p>
                    </div>
                    <div className="flex gap-3">
                        <button className="bg-white border border-slate-200 text-slate-700 font-semibold px-4 py-2.5 rounded-xl hover:bg-slate-50 transition-all shadow-sm flex items-center gap-2 active:scale-95">
                            <Download className="w-4 h-4 text-slate-400" />
                            Export CSV
                        </button>
                        <button className="bg-primary text-white font-semibold px-4 py-2.5 rounded-xl hover:bg-primary/90 transition-all shadow-lg shadow-primary/20 flex items-center gap-2 active:scale-95">
                            <UserPlus className="w-4 h-4" />
                            Add User
                        </button>
                    </div>
                </div>

                {/* Filters & Search */}
                <div className="bg-white border border-slate-100 rounded-2xl p-4 shadow-sm flex flex-col md:flex-row items-center justify-between gap-4">
                    <div className="flex p-1 bg-slate-50 rounded-xl border border-slate-100 w-full md:w-auto">
                        {['all', 'student', 'advisor', 'admin'].map((r) => (
                            <button
                                key={r}
                                onClick={() => setFilter(r)}
                                className={`px-4 py-2 rounded-lg font-bold text-xs uppercase tracking-widest transition-all ${
                                    filter === r 
                                    ? 'bg-white text-primary shadow-sm ring-1 ring-slate-200' 
                                    : 'text-slate-400 hover:text-slate-600'
                                }`}
                            >
                                {r === 'all' ? 'All Users' : r + 's'}
                            </button>
                        ))}
                    </div>
                    <div className="relative w-full md:w-80">
                        <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                        <input 
                            type="text"
                            placeholder="Search names, emails..."
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                            className="w-full pl-10 pr-4 py-2.5 bg-slate-50 border border-slate-100 rounded-xl focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all text-sm font-medium"
                        />
                    </div>
                </div>

                {/* Users Table */}
                <div className="bg-white border border-slate-100 rounded-3xl shadow-sm overflow-hidden border-slate-200/60">
                    <div className="overflow-x-auto">
                        <table className="w-full text-left border-collapse">
                            <thead>
                                <tr className="bg-slate-50/50 border-b border-slate-100">
                                    <th className="px-6 py-4 font-bold text-slate-400 text-[10px] uppercase tracking-widest">User Profile</th>
                                    <th className="px-6 py-4 font-bold text-slate-400 text-[10px] uppercase tracking-widest">System Role</th>
                                    <th className="px-6 py-4 font-bold text-slate-400 text-[10px] uppercase tracking-widest">Faculty</th>
                                    <th className="px-6 py-4 font-bold text-slate-400 text-[10px] uppercase tracking-widest">Status</th>
                                    <th className="px-6 py-4 font-bold text-slate-400 text-[10px] uppercase tracking-widest text-right">Actions</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-slate-100">
                                {loading ? (
                                    <tr>
                                        <td colSpan={5} className="py-20 text-center">
                                            <div className="w-10 h-10 border-4 border-primary/20 border-t-primary rounded-full animate-spin mx-auto mb-4" />
                                            <p className="text-slate-400 font-medium italic">Synchronizing user data...</p>
                                        </td>
                                    </tr>
                                ) : filteredUsers.length === 0 ? (
                                    <tr>
                                        <td colSpan={5} className="py-20 text-center space-y-3">
                                            <div className="w-16 h-16 bg-slate-50 rounded-full flex items-center justify-center mx-auto text-slate-300">
                                                <Search className="w-8 h-8" />
                                            </div>
                                            <h4 className="font-bold text-slate-900 text-lg">No users found</h4>
                                            <p className="text-slate-500 text-sm max-w-xs mx-auto">We couldn't find any users matching your current filters or search query.</p>
                                        </td>
                                    </tr>
                                ) : (
                                    filteredUsers.map((user) => (
                                        <tr key={user.id} className="hover:bg-slate-50/80 transition-all group">
                                            <td className="px-6 py-4">
                                                <div className="flex items-center gap-4">
                                                    <div className="w-10 h-10 rounded-2xl bg-slate-100 flex items-center justify-center text-slate-400 group-hover:bg-primary/10 group-hover:text-primary transition-colors font-bold">
                                                        {user.fullName.split(' ').map((n: string) => n[0]).join('')}
                                                    </div>
                                                    <div>
                                                        <h4 className="font-bold text-slate-900 group-hover:text-primary transition-colors">{user.fullName}</h4>
                                                        <p className="text-xs text-slate-400 font-medium">{user.email}</p>
                                                    </div>
                                                </div>
                                            </td>
                                            <td className="px-6 py-4">
                                                {getRoleBadge(user.role)}
                                            </td>
                                            <td className="px-6 py-4 font-semibold text-slate-500 text-sm">
                                                {user.facultyName || 'Central Administration'}
                                            </td>
                                            <td className="px-6 py-4">
                                                <div className="flex items-center gap-2 text-emerald-600 font-bold text-xs bg-emerald-50 w-fit px-2 py-1 rounded-lg">
                                                    <div className="w-1.5 h-1.5 rounded-full bg-emerald-500 animate-pulse" />
                                                    Active
                                                </div>
                                            </td>
                                            <td className="px-6 py-4 text-right">
                                                <div className="flex items-center justify-end gap-2 opacity-0 group-hover:opacity-100 transition-all">
                                                    <button className="p-2 bg-slate-50 text-slate-400 rounded-xl hover:bg-primary hover:text-white transition-all active:scale-90">
                                                        <Edit2 className="w-4 h-4" />
                                                    </button>
                                                    <button className="p-2 bg-slate-50 text-slate-400 rounded-xl hover:bg-rose-600 hover:text-white transition-all active:scale-90">
                                                        <Ban className="w-4 h-4" />
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
                            Showing {filteredUsers.length} of {users.length} entries
                        </p>
                        <div className="flex gap-2">
                            <button className="px-3 py-1.5 bg-white border border-slate-200 rounded-lg text-xs font-bold text-slate-400 cursor-not-allowed">Previous</button>
                            <button className="px-3 py-1.5 bg-primary text-white rounded-lg text-xs font-bold shadow-sm">1</button>
                            <button className="px-3 py-1.5 bg-white border border-slate-200 rounded-lg text-xs font-bold text-slate-600 hover:bg-slate-50">2</button>
                            <button className="px-3 py-1.5 bg-white border border-slate-200 rounded-lg text-xs font-bold text-slate-600 hover:bg-slate-50">Next</button>
                        </div>
                    </div>
                </div>
            </div>
        </DashboardLayout>
    );
};

export default UserManagement;
