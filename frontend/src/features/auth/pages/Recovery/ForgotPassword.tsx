
import { useNavigate } from 'react-router-dom';
import { useForgotPassword } from '@/features/auth/hooks';
import { AuthLayout } from '@/features/auth/components';
import { EmailStep } from '@/features/auth/components/ForgotPassword/EmailStep';
import { OTPStep } from '@/features/auth/components/ForgotPassword/OTPStep';
import { PasswordStep } from '@/features/auth/components/ForgotPassword/PasswordStep';

export const ForgotPassword = () => {
  const navigate = useNavigate();
  const {
    step,
    emailForm,
    otpForm,
    passwordForm,
    handleSendOTP,
    handleVerifyOTP,
    handleResetPassword
  } = useForgotPassword();
  
  const renderStep = () => {
    switch (step) {
      case 1: return <EmailStep form={emailForm} onSubmit={handleSendOTP} />;
      case 2: return <OTPStep form={otpForm} onSubmit={handleVerifyOTP} />;
      case 3: return (
        <PasswordStep 
          form={passwordForm} 
          onSubmit={async (data) => {
            const success = await handleResetPassword(data);
            if (success) navigate('/login');
          }} 
        />
      );
      default: return null;
    }
  };

  return (
    <AuthLayout
      icon="shield_lock"
      imageAlt="Secure Access Management"
      imageSrc="https://images.unsplash.com/photo-1558449028-b53a39d100fc?auto=format&fit=crop&q=80&w=1000"
      subtitle="The portal uses multi-layered security to ensure your academic records and project data remain protected during password recovery."
      title="Access Recovery"
    >
      {renderStep()}
    </AuthLayout>
  );
};
