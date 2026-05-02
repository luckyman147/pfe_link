import { useEffect, useState } from 'react';
import api from '@/shared/services/api';

export const useAdminDashboard = () => {
    const [pendingStudents, setPendingStudents] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);

    const fetchPendingStudents = async () => {
        try {
            const response = await api.get('/api/admin/students/pending');
            setPendingStudents(response.data);
        } catch (error) {
            console.error("Failed to fetch pending students", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchPendingStudents();
    }, []);

    const handleApprove = async (studentId: string) => {
        try {
            await api.post(`/api/admin/students/${studentId}/approve`);
            setPendingStudents(prev => prev.filter(s => s.id !== studentId));
        } catch (error) {
            console.error("Failed to approve student", error);
        }
    };

    const handleReject = async (studentId: string) => {
        const reason = prompt("Enter rejection reason:");
        if (!reason) return;

        try {
            await api.post(`/api/admin/students/${studentId}/reject`, { reason });
            setPendingStudents(prev => prev.filter(s => s.id !== studentId));
        } catch (error) {
            console.error("Failed to reject student", error);
        }
    };

    return {
        pendingStudents,
        loading,
        handleApprove,
        handleReject
    };
};
