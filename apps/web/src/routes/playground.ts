import Playground from '@/page/playground/playground';
import { isDevelopment } from '@evovocab/shared-utils';
import { createFileRoute, redirect } from '@tanstack/react-router';

export const Route = createFileRoute('/playground')({
  beforeLoad: () => {
    if (!isDevelopment(import.meta.env)) {
      return redirect({ to: '/' });
    }
    return;
  },
  component: isDevelopment(import.meta.env) ? Playground : undefined,
});
