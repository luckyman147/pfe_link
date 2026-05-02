import { Link } from "react-router-dom";

interface NavLinkProps {
  to: string;
  label: string;
}

const NavLink = ({ to, label }: NavLinkProps) => (
  <Link 
    to={to}
    className="text-sm font-medium transition-all hover:-translate-y-0.5 text-gray-600 hover:text-primary-600"
  >
    {label}
  </Link>
);

export const NavLinks = () => {
  return (
    <nav className="hidden md:flex items-center gap-8">
      <NavLink to="/about" label="About" />
      <NavLink to="/how-it-works" label="How It Works" />
      <NavLink to="/contact" label="Contact" />
    </nav>
  );
};
