import React from 'react';
import { Navbar } from './Navbar';
import { Footer } from './Footer';
import { Sidebar } from './Sidebar';
import { useAuth } from '@/features/auth';
import { useLocation } from 'react-router-dom';

export const MainLayout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const { isAuthenticated } = useAuth();
    const location = useLocation();
    
    const isLandingPage = location.pathname === "/" || location.pathname === "/landing";
    const showSidebar = isAuthenticated && !isLandingPage;

    return (
        <div className="min-h-screen bg-background text-foreground selection:bg-primary/20 flex flex-col">
            <Navbar />
            <div className="flex flex-1">
                {showSidebar && <Sidebar />}
                <main className={`flex-1 ${
                    isLandingPage 
                        ? '' 
                        : 'p-6 md:p-8 max-w-7xl'
                } ${showSidebar ? 'mx-0' : 'mx-auto'}`}>
                    <div className={isLandingPage ? '' : 'animate-in fade-in slide-in-from-bottom-2 duration-500'}>
                        {children}
                    </div>
                </main>
            </div>
            {isLandingPage && <Footer />}
        </div>
    );
};
