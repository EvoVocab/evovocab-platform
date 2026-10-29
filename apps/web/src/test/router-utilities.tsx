import {
  createMemoryHistory,
  createRootRoute,
  createRouter,
  Outlet,
  RouterContextProvider,
  type AnyRoute,
  type AnyRouter,
} from '@tanstack/react-router';
import { render, type RenderOptions } from '@testing-library/react';

export const rootRoute = createRootRoute({
  component: () => <Outlet />,
});

// Test router factory
export function createTestRouter(routes: Array<AnyRoute>, initialLocation = '/') {
  const routeTree = rootRoute.addChildren(routes);

  const router = createRouter({
    routeTree,
    history: createMemoryHistory({
      initialEntries: [initialLocation],
    }),
  });

  return router;
}

// Custom render function with router
interface RenderWithRouterOptions extends Omit<RenderOptions, 'wrapper'> {
  router?: AnyRouter;
  initialLocation?: string;
  routes?: Array<AnyRoute>;
}

export function renderWithRouter(
  ui: React.ReactElement,
  { router, initialLocation = '/', routes = [], ...renderOptions }: RenderWithRouterOptions = {},
) {
  if (!router && routes.length > 0) {
    router = createTestRouter(routes, initialLocation);
  }

  if (!router) {
    throw new Error('Router is required. Provide either a router or routes array.');
  }

  const resolvedRouter = router;

  function Wrapper({ children }: { children: React.ReactNode }) {
    return <RouterContextProvider router={resolvedRouter}>{children}</RouterContextProvider>;
  }

  return {
    ...render(ui, { wrapper: Wrapper, ...renderOptions }),
    router: resolvedRouter,
  };
}
