import { Link } from 'react-router-dom';
import { Mail, ArrowLeft, RefreshCw } from 'lucide-react';

export const MustVerifyEmail = () => {
  return (
    <div className="min-h-screen bg-linear-to-br from-primary-50 via-sky-50 to-white flex flex-col justify-center py-12 px-4 shadow-2xl">
      <div className="relative z-10 sm:mx-auto sm:w-full sm:max-w-md text-center">
        {/* Decorative Background */}
        <div className="absolute inset-0 overflow-hidden pointer-events-none -z-10">
          <div className="absolute top-0 left-0 w-64 h-64 bg-primary-200/30 rounded-full blur-3xl" />
          <div className="absolute bottom-0 right-0 w-96 h-96 bg-sky-200/30 rounded-full blur-3xl" />
        </div>

        <div className="bg-white/80 backdrop-blur-xl rounded-3xl shadow-xl p-8 border border-white/50 animate-fade-in-up">
          <div className="w-20 h-20 bg-gradient-to-br from-primary-500 to-sky-500 rounded-3xl flex items-center justify-center mx-auto mb-8 shadow-lg shadow-primary-500/30">
            <Mail className="w-10 h-10 text-white" />
          </div>

          <h1 className="text-3xl font-bold text-navy-900 mb-4">Check your email</h1>
          <p className="text-gray-600 mb-8 leading-relaxed">
            We've sent a verification link to your email address. 
            Please follow the link to verify your account and continue your registration process.
          </p>

          <div className="space-y-4">
            <button
              onClick={() => window.location.reload()}
              className="w-full flex items-center justify-center gap-2 py-3 px-6 bg-gradient-to-r from-primary-500 to-sky-500 text-white font-semibold rounded-xl shadow-lg shadow-primary-500/30 hover:shadow-xl hover:-translate-y-0.5 transition-all"
            >
              <RefreshCw className="w-5 h-5" />
              I've verified my email
            </button>
            
            <p className="text-sm text-gray-500">
              Didn't receive an email? {' '}
              <button className="text-primary-600 font-semibold hover:underline">
                Resend link
              </button>
            </p>
          </div>

          <div className="mt-10 pt-6 border-t border-gray-100 uppercase tracking-widest text-xs font-bold text-gray-400">
            <Link 
              to="/login" 
              className="inline-flex items-center gap-2 hover:text-primary-600 transition-colors"
            >
              <ArrowLeft className="w-4 h-4" />
              Back to Login
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};
