import { useLocation } from "react-router-dom";
import { Menu, X } from "lucide-react";
import { useState } from "react";
import { NavLogo } from "./Navbar/NavLogo";
import { NavLinks } from "./Navbar/NavLinks";
import { LanguageSwitcher } from "./Navbar/LanguageSwitcher";
import { AuthActions } from "./Navbar/AuthActions";
import { MobileMenu } from "./Navbar/MobileMenu";

export const Navbar = () => {
    const location = useLocation();
    const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
    
    const isLandingPage = location.pathname === "/" || location.pathname === "/landing";

    return (
        <header className="h-20 flex justify-between items-center px-6 lg:px-12 sticky top-0 z-50 transition-all duration-300 bg-white/95 backdrop-blur-md shadow-sm">
            <NavLogo />
            <NavLinks />

            <div className="flex gap-3 items-center">
                <LanguageSwitcher />
                <AuthActions isLandingPage={isLandingPage} />

                <button 
                    onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
                    className={`md:hidden p-2 rounded-lg ${
                        isLandingPage ? 'text-white' : 'text-navy-800'
                    }`}
                >
                    {mobileMenuOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
                </button>
            </div>

            {mobileMenuOpen && (
                <MobileMenu onClose={() => setMobileMenuOpen(false)} />
            )}
        </header>
    );
};

export default Navbar;
