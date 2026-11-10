import { createFileRoute } from '@tanstack/react-router';
import Home from '@/page/home/home.lazy';

export const Route = createFileRoute('/')({
  component: Home,
});
