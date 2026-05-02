import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '@/features/auth';
import {
  LayoutDashboard,
  FolderOpen,
  Users,
  GraduationCap,
  BarChart3,
  Settings,
  LogOut,
  HelpCircle,
  ShieldCheck
} from 'lucide-react';

interface MenuItem {
  label: string;
  path: string;
  icon: any;
  roles?: string[];
}

export const DashboardSidebar = () => {
  const { user, logout } = useAuth();
  const location = useLocation();

  const menuItems: MenuItem[] = [
    { label: 'Dashboard', path: '/admin', icon: LayoutDashboard, roles: ['STUDENT', 'ADVISOR', 'ADMIN'] },
    { label: 'Projects', path: '/projects', icon: FolderOpen, roles: ['STUDENT', 'ADVISOR', 'ADMIN'] },
    { label: 'Students', path: '/students', icon: Users, roles: ['ADVISOR', 'ADMIN'] },
    { label: 'Advisors', path: '/advisors', icon: GraduationCap, roles: ['STUDENT', 'ADMIN'] },
    { label: 'Analytics', path: '/analytics', icon: BarChart3, roles: ['ADMIN', 'ADVISOR'] },
    { label: 'Settings', path: '/settings', icon: Settings },
  ];

  const filteredItems = menuItems.filter(item => !item.roles || (user?.role && item.roles.includes(user.role)));

  return (
    <aside className="bg-background/80 backdrop-blur-xl border-r border-border/50 fixed left-0 top-0 h-screen w-[280px] z-[60] flex flex-col py-8 transition-all duration-300 hidden lg:flex shadow-2xl">
      {/* Header */}
      <div className="px-8 mb-10 flex items-center gap-4">
        <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-primary/80 to-primary flex items-center justify-center shrink-0 shadow-lg shadow-primary/20">
          <ShieldCheck className="w-6 h-6 text-primary-foreground" />
        </div>
        <div className="flex flex-col">
          <span className="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-foreground to-foreground/70 tracking-tight">HeySir PFE</span>
          <span className="text-primary text-[10px] uppercase tracking-[0.2em] font-extrabold mt-0.5">Admin Portal</span>
        </div>
      </div>

      {/* Main Navigation */}
      <nav className="flex-1 px-4 space-y-2 overflow-y-auto custom-scrollbar">
        {filteredItems.map((item) => {
          const isActive = location.pathname === item.path || (item.path !== '/admin' && location.pathname.startsWith(item.path));
          const Icon = item.icon;
          return (
            <Link
              key={item.path}
              to={item.path}
              className={`flex items-center gap-4 px-4 py-3.5 rounded-xl transition-all duration-300 group relative ${
                isActive
                  ? 'bg-primary text-primary-foreground shadow-md shadow-primary/20'
                  : 'text-muted-foreground hover:bg-accent/50 hover:text-foreground'
              }`}
            >
              {isActive && (
                <div className="absolute left-0 top-1/2 -translate-y-1/2 w-1.5 h-8 bg-background rounded-r-full" />
              )}
              <Icon className={`w-5 h-5 transition-transform duration-300 ${isActive ? 'scale-110' : 'group-hover:scale-110'}`} />
              <span className="font-semibold text-sm tracking-wide">{item.label}</span>
            </Link>
          );
        })}
      </nav>

      {/* Footer Navigation */}
      <div className="px-4 mt-auto pt-6 border-t border-border/50 space-y-2">
        <Link
          to="/support"
          className="text-muted-foreground px-4 py-3 hover:text-foreground hover:bg-accent/50 rounded-xl flex items-center gap-4 transition-all duration-300 group"
        >
          <HelpCircle className="w-5 h-5 group-hover:scale-110 transition-transform duration-300" />
          <span className="font-semibold text-sm tracking-wide">Support</span>
        </Link>
        <button
          onClick={() => logout()}
          className="w-full text-muted-foreground px-4 py-3 hover:text-destructive hover:bg-destructive/10 rounded-xl flex items-center gap-4 transition-all duration-300 group"
        >
          <LogOut className="w-5 h-5 group-hover:scale-110 transition-transform duration-300" />
          <span className="font-semibold text-sm tracking-wide text-left">Logout</span>
        </button>
      </div>
    </aside>
  );
};
