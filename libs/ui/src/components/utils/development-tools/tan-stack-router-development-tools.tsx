import React from 'react';
import { isProduction } from '@evovocab/shared-utils';
import type { TanStackRouterDevtools } from '@tanstack/react-router-devtools';

type DevtoolsProps = React.ComponentProps<typeof TanStackRouterDevtools>;

export const TanStackRouterDevelopmentTools: React.ComponentType<DevtoolsProps> = isProduction(
  import.meta.env,
)
  ? (): null => null
  : React.lazy(() =>
      import('@tanstack/react-router-devtools').then((result) => ({
        default: result.TanStackRouterDevtools,
      })),
    );
