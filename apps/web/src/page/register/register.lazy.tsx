import * as React from 'react';
import { RegisterForm } from '@/features/auth/components/register-form';
import { TooltipProvider } from '@evovocab/ui';
import type { UseFormReturn } from 'react-hook-form';
import type { RegisterFormValues } from '@/features/auth/schemas/register.schema';
import { toast } from 'sonner';
import type { ErrorResponseDto } from '@evovocab/shared-api';
import { HttpStatusCode, isAxiosError } from 'axios';
import { useRegister } from '@/features/auth/hooks/use-register';
function Register() {
  const [deplayTooltip] = React.useState(500);
  const [closeDeplayTooltip] = React.useState(500);
  const [timeoutTooltip] = React.useState(400);
  const [form, setForm] = React.useState<UseFormReturn<RegisterFormValues> | undefined>();
  const { mutateAsync: registerAsync } = useRegister(form);

  return (
    <TooltipProvider closeDelay={closeDeplayTooltip} delay={deplayTooltip} timeout={timeoutTooltip}>
      <div className="col-span-4 flex flex-col items-center gap-8 py-8 sm:col-start-2 lg:col-span-6 lg:col-start-4 lg:pt-24 lg:pb-12">
        <RegisterForm
          // Tính năng đang phát triển
          // onGoogleLogin={() => {
          //   console.log('Google login clicked');
          // }}
          onSubmitForm={async (form, data) => {
            // console.log('Form submitted with data:', data);
            setForm(form);
            await registerAsync(data, {
              onSuccess: (response) => {
                const { data, message } = response;
                console.log(data);
                toast.success(message);
              },
              onError: (error) => {
                if (isAxiosError<ErrorResponseDto>(error)) {
                  const responseData = error.response?.data;
                  const statusCode = error.response?.status;
                  if (statusCode === HttpStatusCode.Conflict) {
                    const message = responseData?.detail;
                    if (!message) return;

                    form.setError('email', {
                      type: 'server',
                      message,
                    });
                  }
                }
              },
            });
          }}
        />
      </div>
    </TooltipProvider>
  );
}

export default Register;
