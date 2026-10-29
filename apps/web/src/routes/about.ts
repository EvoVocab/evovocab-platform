import { createFileRoute } from '@tanstack/react-router';
import About from '../page/about/about';

export const Route = createFileRoute('/about')({
  component: About,
});
