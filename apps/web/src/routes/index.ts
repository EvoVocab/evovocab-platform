import { createFileRoute } from '@tanstack/react-router';
import Home from '../page/home/home';

export const Route = createFileRoute('/')({
  component: Home,
});
