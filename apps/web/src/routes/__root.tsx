import { createRootRoute, Outlet } from '@tanstack/react-router';
import { MainLayout } from '@evovocab/ui/components/layouts';

export const Route = createRootRoute({
  component: () => (
    <MainLayout>
      <Outlet />
    </MainLayout>
  ),
});
