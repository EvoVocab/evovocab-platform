import { createFileRoute } from '@tanstack/react-router';
import Playground from '@/page/playground/playground.lazy';

export const Route = createFileRoute('/_main/playground')({
  component: Playground,
});
