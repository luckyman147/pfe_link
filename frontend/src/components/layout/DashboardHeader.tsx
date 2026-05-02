import { useAuth } from '@/features/auth';

export const DashboardHeader = () => {
  const { user } = useAuth();

  return (
    <header className="bg-surface-container-lowest/80 dark:bg-surface-dim/80 backdrop-blur-md font-body-md antialiased text-body-sm w-full sticky top-0 z-40 h-16 border-b border-outline-variant shadow-sm transition-all duration-200 flex items-center justify-between px-md lg:px-xl gap-md">
      {/* Left: Mobile Menu & Search */}
      <div className="flex items-center gap-md flex-1">
        <button className="lg:hidden text-on-surface-variant hover:bg-surface-container-low p-2 rounded-full transition-colors">
          <span className="material-symbols-outlined">menu</span>
        </button>
        <div className="relative hidden sm:block max-w-md w-full">
          <span className="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-outline-variant text-[18px]">search</span>
          <input
            className="w-full pl-10 pr-4 py-2 bg-surface-container-low border border-outline-variant rounded-full text-body-sm focus:border-primary focus:ring-1 focus:ring-primary outline-none transition-all"
            placeholder="Search projects, documents..."
            type="text"
          />
        </div>
      </div>

      {/* Center: Brand (Mobile only) */}
      <div className="lg:hidden text-lg font-bold tracking-tight text-on-surface truncate">
        HeySir PFE-Link
      </div>

      {/* Right: Actions & Profile */}
      <div className="flex items-center gap-sm lg:gap-md">
        <button className="text-on-surface-variant hover:bg-surface-container-low transition-colors duration-200 p-2 rounded-full relative">
          <span className="material-symbols-outlined text-[20px]">notifications</span>
          <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-error rounded-full border-2 border-surface-container-lowest"></span>
        </button>
        <button className="hidden sm:flex text-on-surface-variant hover:bg-surface-container-low transition-colors duration-200 p-2 rounded-full">
          <span className="material-symbols-outlined text-[20px]">help_outline</span>
        </button>
        
        <div className="flex items-center gap-sm pl-sm border-l border-outline-variant ml-sm">
          <div className="hidden sm:flex flex-col items-end mr-sm">
            <span className="font-label-md text-label-md text-on-surface font-semibold">{user?.fullName}</span>
            <span className="font-label-sm text-[10px] text-on-surface-variant uppercase tracking-wider">{user?.role}</span>
          </div>
          <div className="w-8 h-8 rounded-full overflow-hidden border border-outline-variant cursor-pointer hover:border-primary transition-colors">
            <img
              alt="User profile"
              className="w-full h-full object-cover"
              src={user?.imageUrl || "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?auto=format&fit=facearea&facepad=2&w=256&h=256&q=80"}
            />
          </div>
        </div>
      </div>
    </header>
  );
};
