import { Link } from "react-router-dom";

interface MobileMenuProps {
  onClose: () => void;
}

const MobileNavLink = ({ to, label, onClick }: { to: string; label: string; onClick: () => void }) => (
  <Link 
    to={to}
    onClick={onClick}
    className="px-4 py-3 text-navy-800 font-medium hover:bg-gray-50 rounded-lg transition-colors"
  >
    {label}
  </Link>
);

export const MobileMenu = ({ onClose }: MobileMenuProps) => {
  return (
    <div className="absolute top-full left-0 right-0 bg-white shadow-xl border-t md:hidden">
      <nav className="flex flex-col p-4 gap-2">
        <MobileNavLink to="/about" label="About" onClick={onClose} />
        <MobileNavLink to="/how-it-works" label="How It Works" onClick={onClose} />
        <MobileNavLink to="/contact" label="Contact" onClick={onClose} />
      </nav>
    </div>
  );
};
