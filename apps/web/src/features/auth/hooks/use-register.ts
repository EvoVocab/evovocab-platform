import { useMutation } from '@tanstack/react-query';
import { AuthControllerApiFactory, type RegisterRequest } from '@evovocab/shared-api';
import type { UseFormReturn } from 'react-hook-form';
import { config } from '@/config/api-config';
import type { RegisterFormValues } from '@/features/auth/schemas/register.schema';

function useRegister(form?: UseFormReturn<RegisterFormValues>) {
  const register = useMutation({
    mutationKey: ['register'],
    meta: {
      form,
    },
    mutationFn: async (data: RegisterRequest) => {
      const response = await AuthControllerApiFactory(config).register(data);
      return response.data;
    },
  });

  return register;
}

export { useRegister };
