import * as React from 'react';
import { RegisterForm } from '@/features/auth/components/register-form';
import { TooltipProvider } from '@evovocab/ui';

function Register() {
  const [deplayTooltip] = React.useState(500);
  const [closeDeplayTooltip] = React.useState(500);
  const [timeoutTooltip] = React.useState(400);

  return (
    <TooltipProvider closeDelay={closeDeplayTooltip} delay={deplayTooltip} timeout={timeoutTooltip}>
      <div className="col-span-4 flex flex-col items-center gap-8 py-8 sm:col-start-2 lg:col-span-4 lg:col-start-5 lg:pt-24 lg:pb-12">
        <RegisterForm
          // Tính năng đang phát triển
          // onGoogleLogin={() => {
          //   console.log('Google login clicked');
          // }}
          onSubmitForm={(form, data) => {
            console.log('Form submitted with data:', data);
          }}
        />
      </div>
    </TooltipProvider>
  );
}

export default Register;
