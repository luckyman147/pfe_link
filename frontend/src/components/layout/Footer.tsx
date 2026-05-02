import { Link } from 'react-router-dom';
import { Mail, MapPin, Phone, Github, Linkedin, Twitter } from 'lucide-react';

export const Footer = () => {
    const currentYear = new Date().getFullYear();

    return (
        <footer className="bg-navy-800 text-white">
            {/* Main Footer */}
            <div className="container mx-auto px-6 py-16">
                <div className="grid md:grid-cols-4 gap-12">
                    {/* Brand */}
                    <div className="md:col-span-1">
                        <div className="flex items-center gap-3 mb-4">
                            <div className="w-10 h-10 bg-gradient-to-br from-primary-400 to-sky-400 rounded-xl flex items-center justify-center">
                                <span className="text-white font-bold text-lg">HS</span>
                            </div>
                            <span className="text-xl font-bold">HeySir</span>
                        </div>
                        <p className="text-white/60 text-sm leading-relaxed mb-6">
                            Tunisia's premier platform connecting students with expert advisors for successful PFE projects.
                        </p>
                        <div className="flex gap-4">
                            <SocialLink href="#" icon={<Twitter className="w-5 h-5" />} />
                            <SocialLink href="#" icon={<Linkedin className="w-5 h-5" />} />
                            <SocialLink href="#" icon={<Github className="w-5 h-5" />} />
                        </div>
                    </div>

                    {/* Quick Links */}
                    <div>
                        <h4 className="font-semibold text-white mb-4">Quick Links</h4>
                        <ul className="space-y-3">
                            <FooterLink to="/about" label="About Us" />
                            <FooterLink to="/how-it-works" label="How It Works" />
                            <FooterLink to="/contact" label="Contact" />
                            <FooterLink to="/faq" label="FAQ" />
                        </ul>
                    </div>

                    {/* For Users */}
                    <div>
                        <h4 className="font-semibold text-white mb-4">Get Started</h4>
                        <ul className="space-y-3">
                            <FooterLink to="/auth/signup/student" label="Join as Student" />
                            <FooterLink to="/auth/signup/advisor" label="Join as Advisor" />
                            <FooterLink to="/login" label="Sign In" />
                            <FooterLink to="/auth/forgot-password" label="Reset Password" />
                        </ul>
                    </div>

                    {/* Contact */}
                    <div>
                        <h4 className="font-semibold text-white mb-4">Contact Us</h4>
                        <ul className="space-y-3">
                            <li className="flex items-center gap-3 text-white/60 text-sm">
                                <Mail className="w-4 h-4 text-primary-400" />
                                [EMAIL_ADDRESS]
                            </li>
                            <li className="flex items-center gap-3 text-white/60 text-sm">
                                <Phone className="w-4 h-4 text-primary-400" />
                                +216 28663780
                            </li>
                            <li className="flex items-start gap-3 text-white/60 text-sm">
                                <MapPin className="w-4 h-4 text-primary-400 flex-shrink-0 mt-0.5" />
                                Tunis, Tunisia
                            </li>
                        </ul>
                    </div>
                </div>
            </div>

            {/* Bottom Bar */}
            <div className="border-t border-white/10">
                <div className="container mx-auto px-6 py-6">
                    <div className="flex flex-col md:flex-row justify-between items-center gap-4">
                        <p className="text-white/40 text-sm">
                            © {currentYear} HeySir. All rights reserved.
                        </p>
                        <div className="flex gap-6">
                            <Link to="/privacy" className="text-white/40 text-sm hover:text-white transition-colors">
                                Privacy Policy
                            </Link>
                            <Link to="/terms" className="text-white/40 text-sm hover:text-white transition-colors">
                                Terms of Service
                            </Link>
                        </div>
                    </div>
                </div>
            </div>
        </footer>
    );
};

interface FooterLinkProps {
    to: string;
    label: string;
}

const FooterLink: React.FC<FooterLinkProps> = ({ to, label }) => (
    <li>
        <Link 
            to={to} 
            className="text-white/60 text-sm hover:text-white transition-colors"
        >
            {label}
        </Link>
    </li>
);

interface SocialLinkProps {
    href: string;
    icon: React.ReactNode;
}

const SocialLink: React.FC<SocialLinkProps> = ({ href, icon }) => (
    <a 
        href={href}
        target="_blank"
        rel="noopener noreferrer"
        className="w-10 h-10 bg-white/10 rounded-lg flex items-center justify-center text-white/60 hover:bg-white/20 hover:text-white transition-all"
    >
        {icon}
    </a>
);

export default Footer;
