import { useEffect, useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { CheckCircle2, XCircle, Loader2 } from 'lucide-react';
import { registrationService } from '@/features/auth/services/registration.service';

type Status = 'loading' | 'ok' | 'err';

export const VerifyEmailConfirm = () => {
  const [searchParams] = useSearchParams();
  const token = searchParams.get('token');
  const [status, setStatus] = useState<Status>('loading');
  const [message, setMessage] = useState<string>('');

  useEffect(() => {
    if (!token?.trim()) {
      setStatus('err');
      setMessage('Missing verification token. Open the link from your email.');
      return;
    }
    let cancelled = false;
    (async () => {
      try {
        await registrationService.verifyEmail(token);
        if (!cancelled) {
          setStatus('ok');
          setMessage('Your email is verified. You can sign in.');
        }
      } catch (e: unknown) {
        if (!cancelled) {
          setStatus('err');
          const msg =
            e && typeof e === 'object' && 'response' in e
              ? (e as { response?: { data?: { message?: string } } }).response?.data?.message
              : undefined;
          setMessage(msg || 'Verification failed. The link may be invalid or expired.');
        }
      }
    })();
    return () => {
      cancelled = true;
    };
  }, [token]);

  return (
    <div className="min-h-screen bg-linear-to-br from-primary-50 via-sky-50 to-white flex flex-col justify-center py-12 px-4">
      <div className="sm:mx-auto sm:w-full sm:max-w-md text-center">
        <div className="bg-white/80 backdrop-blur-xl rounded-3xl shadow-xl p-8 border border-white/50">
          {status === 'loading' && (
            <>
              <Loader2 className="w-14 h-14 text-primary-500 animate-spin mx-auto mb-6" />
              <h1 className="text-2xl font-bold text-navy-900 mb-2">Verifying your email</h1>
              <p className="text-gray-600">Please wait…</p>
            </>
          )}
          {status === 'ok' && (
            <>
              <CheckCircle2 className="w-14 h-14 text-green-600 mx-auto mb-6" />
              <h1 className="text-2xl font-bold text-navy-900 mb-2">Email verified</h1>
              <p className="text-gray-600 mb-8">{message}</p>
              <Link
                to="/login"
                className="inline-flex w-full justify-center py-3 px-6 bg-gradient-to-r from-primary-500 to-sky-500 text-white font-semibold rounded-xl"
              >
                Sign in
              </Link>
            </>
          )}
          {status === 'err' && (
            <>
              <XCircle className="w-14 h-14 text-red-600 mx-auto mb-6" />
              <h1 className="text-2xl font-bold text-navy-900 mb-2">Verification failed</h1>
              <p className="text-gray-600 mb-8">{message}</p>
              <Link to="/login" className="text-primary-600 font-semibold hover:underline">
                Back to sign in
              </Link>
            </>
          )}
        </div>
      </div>
    </div>
  );
};
