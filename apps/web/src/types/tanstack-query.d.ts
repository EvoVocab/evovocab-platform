import '@tanstack/react-query';
import type { UseFormReturn } from 'react-hook-form';

declare module '@tanstack/react-query' {
  interface Register {
    mutationMeta: {
      form?: UseFormReturn<Any>;
      skipGlobalErrorToast?: boolean;
    };
  }
}
