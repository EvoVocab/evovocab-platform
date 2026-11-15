import type { ErrorResponseDto } from '@evovocab/shared-api';
import { MutationCache, QueryClient } from '@tanstack/react-query';
import { HttpStatusCode, isAxiosError } from 'axios';

const queryClient = new QueryClient({
  mutationCache: new MutationCache({
    onError: (error, _variables, _context, mutation) => {
      const form = mutation.meta?.form;

      if (!form) {
        return;
      }

      if (isAxiosError<ErrorResponseDto>(error)) {
        const responseData = error.response?.data;
        const statusCode = error.response?.status;

        if (statusCode === HttpStatusCode.UnprocessableEntity) {
          const errors = responseData?.errors;

          if (!errors) {
            return;
          }

          Object.entries(errors).forEach(([field, messages]) => {
            const message = Array.isArray(messages) ? messages.join(', ') : messages;
            if (!message) return;

            form.setError(field, {
              type: 'server',
              message,
            });
          });
          return;
        }
      }
    },
  }),
});

export { queryClient };
