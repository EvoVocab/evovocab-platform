import { createRoute } from '@tanstack/react-router';
import { rootRoute } from './router-utilities';

type CreateRouteOptions = Parameters<typeof createRoute>[0];

type AppRouteComponent = NonNullable<CreateRouteOptions['component']>;

type MockRouteOptions = Omit<Partial<CreateRouteOptions>, 'getParentRoute' | 'path' | 'component'>;

export const createMockRoute = <TPath extends string>(
  path: TPath,
  component: AppRouteComponent,
  options: MockRouteOptions = {},
) => {
  return createRoute({
    getParentRoute: () => rootRoute,
    path,
    component,
    ...(options as Record<string, unknown>),
  });
};
