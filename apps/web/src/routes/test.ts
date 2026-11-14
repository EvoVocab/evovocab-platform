import { createFileRoute } from '@tanstack/react-router';
import { Test } from '@/page/test/test.lazy';

export const Route = createFileRoute('/test')({
  component: Test,
});
