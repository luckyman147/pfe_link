import { Link, useLocation } from "react-router-dom";
import { useAuth } from "@/features/auth";
import { 
    LayoutDashboard, 
    Briefcase, 
    Settings, 
    ClipboardList,
    ChevronRight
} from "lucide-react";

export const Sidebar = () => {
    const { user } = useAuth();
    const role = user?.role;
    const location = useLocation();

    const menuItems = [
        { 
            label: "Dashboard", 
            path: "/", 
            icon: LayoutDashboard,
            roles: ["STUDENT", "TEACHER", "ADMIN"] 
        },
        { 
            label: "PFE Management", 
            path: "/pfes", 
            icon: Briefcase,
            roles: ["TEACHER", "ADMIN"] 
        },
        { 
            label: "My Applications", 
            path: "/applications", 
            icon: ClipboardList,
            roles: ["STUDENT"] 
        },
        { 
            label: "Settings", 
            path: "/settings", 
            icon: Settings,
            roles: ["STUDENT", "TEACHER", "ADMIN"] 
        },
    ];

    const filteredItems = menuItems.filter(item => !item.roles || (role && item.roles.includes(role)));

    return (
        <aside className="w-64 border-r bg-card/30 backdrop-blur-sm hidden lg:flex flex-col sticky top-16 h-[calc(100vh-4rem)] transition-all duration-300">
            <div className="flex-1 py-6 px-4 space-y-1">
                {filteredItems.map((item) => {
                    const isActive = location.pathname === item.path;
                    return (
                        <Link
                            key={item.path}
                            to={item.path}
                            className={`flex items-center justify-between px-4 py-3 rounded-xl transition-all group ${
                                isActive 
                                ? "bg-primary text-primary-foreground shadow-lg shadow-primary/20" 
                                : "hover:bg-accent text-muted-foreground hover:text-foreground"
                            }`}
                        >
                            <div className="flex items-center gap-3">
                                <item.icon className={`w-5 h-5 ${isActive ? "text-primary-foreground" : "group-hover:text-primary"} transition-colors`} />
                                <span className="font-medium text-sm">{item.label}</span>
                            </div>
                            <ChevronRight className={`w-4 h-4 opacity-0 group-hover:opacity-100 transition-opacity ${isActive ? "hidden" : ""}`} />
                        </Link>
                    );
                })}
            </div>

            <div className="p-4 border-t mt-auto">
                <div className="bg-accent/40 rounded-2xl p-4 border border-border/50">
                    <div className="flex items-center gap-3 mb-1">
                        <div className="w-2 h-2 rounded-full bg-green-500 animate-pulse" />
                        <span className="text-xs font-bold text-muted-foreground uppercase tracking-wider">{role}</span>
                    </div>
                </div>
            </div>
        </aside>
    );
};
