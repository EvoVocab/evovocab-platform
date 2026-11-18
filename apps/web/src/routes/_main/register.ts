import { createFileRoute } from '@tanstack/react-router';
import Register from '@/page/register/register.lazy';

export const Route = createFileRoute('/_main/register')({
  component: Register,
});
