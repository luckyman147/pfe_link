import React from 'react';

interface AuthLayoutProps {
  children: React.ReactNode;
  title: string;
  subtitle: string;
  imageSrc: string;
  imageAlt: string;
  icon?: string;
  brandTitle?: string;
}

export const AuthLayout: React.FC<AuthLayoutProps> = ({
  children,
  title,
  subtitle,
  imageSrc,
  imageAlt,
  icon = "school",
  brandTitle = "HeySir PFE-Link"
}) => {
  return (
    <div className="flex w-full min-h-screen font-sans antialiased text-stitch-on-surface bg-stitch-surface-container-lowest overflow-hidden rounded-2xl">
      {/* Left Panel: Brand / Visual Anchor (Hidden on mobile) */}
      <div className="hidden lg:flex basis-[45%] flex-shrink-0 relative flex-col justify-between overflow-hidden shadow-2xl rounded-2xl">
        {/* Visual Anchor Image with a parallax-like hover effect */}
        <div 
          className="absolute inset-0 bg-cover bg-center z-0 transition-transform duration-[4000ms] ease-out hover:scale-10 rounded-2xl "
          style={{ backgroundImage: `url('${imageSrc}')` }}
          aria-label={imageAlt}
        />
        
        {/* Modern Cinematic Overlays */}
        <div className="absolute inset-0 bg-stitch-primary/30 mix-blend-overlay z-10" />
        <div className="absolute inset-0 bg-gradient-to-br from-stitch-primary via-stitch-primary/70 to-transparent z-10 opacity-90" />
        <div className="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-black/20 z-10" />
        
        {/* Grain / Noise Texture for Premium Feel */}
        <div className="absolute inset-0 opacity-[0.03] z-10 pointer-events-none mix-blend-overlay" 
             style={{ backgroundImage: 'url("https://www.transparenttextures.com/patterns/carbon-fibre.png")' }} 
        />
        
        {/* Brand & Narrative Content */}
        <div className="relative z-20 p-2xl flex flex-col h-full justify-between text-stitch-on-primary p-4">
          <div className="flex items-center gap-4 group cursor-default">
            <div className="w-14 h-14 rounded-2xl bg-white/15 backdrop-blur-xl border border-white/20 flex items-center justify-center shadow-2xl transition-all duration-700 group-hover:rotate-[360deg] group-hover:bg-white/25">
              <span className="material-symbols-outlined text-[32px] text-white font-light">{icon}</span>
            </div>
            <div className="flex flex-col">
              <span className="font-display text-2xl tracking-tighter font-black text-white drop-shadow-md leading-none">{brandTitle}</span>
              <span className="text-[10px] font-bold uppercase tracking-[0.2em] text-white/60 mt-1">Platform</span>
            </div>
          </div>
          
          <div className="max-w-md pb-12 animate-in fade-in slide-in-from-bottom-12 duration-1000 ease-out">
            <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-white/10 backdrop-blur-md border border-white/10 mb-8 shadow-inner">
              <span className="flex h-2 w-2 rounded-full bg-sky-400 animate-pulse shadow-[0_0_8px_rgba(56,189,248,0.8)]" />
              <span className="text-[11px] font-black uppercase tracking-[0.15em] text-white/90">Institutional Gateway</span>
            </div>
            <h1 className="font-display text-5xl xl:text-6xl mb-8 leading-[1.05] text-white font-black tracking-tight drop-shadow-2xl">
              {title}
            </h1>
            <p className="text-xl text-white/85 leading-relaxed text-balance font-medium tracking-tight">
              {subtitle}
            </p>
          </div>
        </div>
      </div>

      {/* Right Panel: Functional Area */}
      <div className="flex-1 flex flex-col items-center justify-center p-md sm:p-xl bg-stitch-surface-container-low relative">
        {/* Mobile Brand Header */}
        <div className="lg:hidden w-full max-w-[440px] mb-12 flex items-center gap-sm">
          <div className="w-10 h-10 rounded-xl bg-stitch-primary text-stitch-on-primary flex items-center justify-center shadow-lg">
            <span className="material-symbols-outlined text-[20px]">{icon}</span>
          </div>
          <span className="font-display text-2xl font-bold text-stitch-primary tracking-tight">{brandTitle}</span>
        </div>

        {/* Content Container */}
        <div className="w-full max-w-[440px] flex flex-col mt-12 animate-in fade-in slide-in-from-bottom-6 duration-700">
          {children}
        </div>

        {/* System Footer */}
        <div className="mt-16 text-center">
          <p className="text-[11px] font-medium tracking-wide uppercase text-stitch-on-surface-variant/40">
            © {new Date().getFullYear()} {brandTitle} <span className="mx-2">•</span> Secure Academic Gateway
          </p>
        </div>
      </div>
    </div>
  );
};
