import { createFileRoute } from '@tanstack/react-router';
import About from '@/page/about/about.lazy';

export const Route = createFileRoute('/_main/about')({
  component: About,
});
