import { useTranslation } from "react-i18next";
import { Link, useLocation } from "react-router-dom";
import { Menu, X, LogIn, LogOut, User } from "lucide-react";
import { useAuth } from "@/features/auth";
import { useState } from "react";
import type { FC } from "react";

export const Navbar = () => {
    const { i18n } = useTranslation();
    const { isAuthenticated, logout } = useAuth();
    const location = useLocation();
    const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
    
    const isLandingPage = location.pathname === "/" || location.pathname === "/landing";

    return (
        <header className={`h-20 flex justify-between items-center px-6 lg:px-12 sticky top-0 z-50 transition-all duration-300 ${
           'bg-white/95 backdrop-blur-md shadow-sm'
        }`}>
            {/* Logo */}
            <Link to="/" className="flex items-center gap-3 group">
                <div className="w-14 h-14 relative">
                    <img 
                        src="/logo.png" 
                        alt="HeySir Logo" 
                        className="w-full h-full object-contain"
                        onError={(e) => {
                            e.currentTarget.style.display = 'none';
                            const fallback = e.currentTarget.nextElementSibling as HTMLElement;
                            if (fallback) fallback.style.display = 'flex';
                        }}
                    />
                    {/* Fallback Logo */}
                    <div className="w-full h-full bg-gradient-to-br from-primary-500 to-sky-500 rounded-xl items-center justify-center shadow-lg" style={{ display: 'none' }}>
                        <span className="text-white font-bold text-xl">HS</span>
                    </div>
                </div>
                <div className="flex flex-col">
               
                    <span className={`text-[15px] font-medium uppercase tracking-widest ${
                    'text-gray-400'
                    }`}>
                        PFE Platform
                    </span>
                </div>
            </Link>

            {/* Desktop Navigation */}
            <nav className="hidden md:flex items-center gap-8">
                <NavLink to="/about" label="About" />
                <NavLink to="/how-it-works" label="How It Works" />
                <NavLink to="/contact" label="Contact" />
            </nav>

            {/* Right Actions */}
            <div className="flex gap-3 items-center">
                {/* Language Switcher */}
                <div className={`hidden sm:flex rounded-full p-1 ${
                 'bg-gray-100'
                }`}>
                    <button 
                        onClick={() => i18n.changeLanguage('en')} 
                        className={`px-3 py-1.5 text-xs font-bold rounded-full transition-all ${
                            i18n.language === 'en' 
                                ? 
                                     'bg-white shadow text-primary-600'
                                :  'text-gray-500 hover:text-gray-700'
                        }`}
                    >
                        EN
                    </button>
                    <button 
                        onClick={() => i18n.changeLanguage('fr')} 
                        className={`px-3 py-1.5 text-xs font-bold rounded-full transition-all ${
                            i18n.language === 'fr' 
                                ? isLandingPage 
                                    ? 'bg-white text-primary-600' 
                                    : 'bg-white shadow text-primary-600'
                                : isLandingPage 
                                    ? 'text-white/80 hover:text-white' 
                                    : 'text-gray-500 hover:text-gray-700'
                        }`}
                    >
                        FR
                    </button>
                </div>

                {/* Auth Buttons */}
                {isAuthenticated ? (
                    <div className="flex items-center gap-2">
                        <Link 
                            to="/dashboard"
                            className={`hidden sm:flex items-center gap-2 px-4 py-2 text-sm font-medium rounded-full transition-all ${
                                isLandingPage
                                    ? 'bg-white/10 text-white hover:bg-white/20 backdrop-blur-sm'
                                    : 'bg-primary-50 text-primary-600 hover:bg-primary-100'
                            }`}
                        >
                            <User className="w-4 h-4" />
                            Dashboard
                        </Link>
                        <button 
                            onClick={logout} 
                            className={`flex items-center gap-2 px-4 py-2 text-sm font-medium rounded-full transition-all ${
                                isLandingPage
                                    ? 'bg-red-500/20 text-white hover:bg-red-500/30 backdrop-blur-sm'
                                    : 'bg-red-50 text-red-600 hover:bg-red-100'
                            }`}
                        >
                            <LogOut className="w-4 h-4" />
                            <span className="hidden sm:inline">Logout</span>
                        </button>
                    </div>
                ) : (
                    <div className="flex items-center gap-2">
                        <Link 
                            to="/login"
                            className={`flex items-center gap-2 px-5 py-2.5 text-sm font-semibold rounded-full transition-all ${
                                isLandingPage
                                    ? 'bg-white text-primary-600 hover:bg-white/90 shadow-lg shadow-black/10'
                                    : 'bg-primary-500 text-white hover:bg-primary-600 shadow-lg shadow-primary-500/30'
                            }`}
                        >
                            <LogIn className="w-4 h-4" />
                            Sign In
                        </Link>
                    </div>
                )}

                {/* Mobile Menu Button */}
                <button 
                    onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
                    className={`md:hidden p-2 rounded-lg ${
                        isLandingPage ? 'text-white' : 'text-navy-800'
                    }`}
                >
                    {mobileMenuOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
                </button>
            </div>

            {/* Mobile Menu */}
            {mobileMenuOpen && (
                <div className="absolute top-full left-0 right-0 bg-white shadow-xl border-t md:hidden">
                    <nav className="flex flex-col p-4 gap-2">
                        <MobileNavLink to="/about" label="About" onClick={() => setMobileMenuOpen(false)} />
                        <MobileNavLink to="/how-it-works" label="How It Works" onClick={() => setMobileMenuOpen(false)} />
                        <MobileNavLink to="/contact" label="Contact" onClick={() => setMobileMenuOpen(false)} />
                    </nav>
                </div>
            )}
        </header>
    );
};

interface NavLinkProps {
    to: string;
    label: string;
}

const NavLink: FC<NavLinkProps> = ({ to, label }) => (
    <Link 
        to={to}
        className={`text-sm font-medium transition-all hover:-translate-y-0.5 ${
           'text-gray-600 hover:text-primary-600'
        }`}
    >
        {label}
    </Link>
);

interface MobileNavLinkProps {
    to: string;
    label: string;
    onClick: () => void;
}

const MobileNavLink: React.FC<MobileNavLinkProps> = ({ to, label, onClick }) => (
    <Link 
        to={to}
        onClick={onClick}
        className="px-4 py-3 text-navy-800 font-medium hover:bg-gray-50 rounded-lg transition-colors"
    >
        {label}
    </Link>
);

export default Navbar;
