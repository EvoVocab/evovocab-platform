import Playground from '@/page/playground/playground';
import { createFileRoute } from '@tanstack/react-router';

export const Route = createFileRoute('/playground')({
  component: Playground,
});
