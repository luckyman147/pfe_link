import { Link } from "react-router-dom";

export const NavLogo = () => {
  return (
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
        <div className="w-full h-full bg-gradient-to-br from-primary-500 to-sky-500 rounded-xl items-center justify-center shadow-lg" style={{ display: 'none' }}>
          <span className="text-white font-bold text-xl">HS</span>
        </div>
      </div>
      <div className="flex flex-col">
        <span className="text-[15px] font-medium uppercase tracking-widest text-gray-400">
          PFE Platform
        </span>
      </div>
    </Link>
  );
};
