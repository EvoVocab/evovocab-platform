import { MainLayout } from '@/components/layouts';
import { createFileRoute, Outlet } from '@tanstack/react-router';

export const Route = createFileRoute('/_main')({
  component: () => (
    <MainLayout>
      <Outlet />
    </MainLayout>
  ),
});
