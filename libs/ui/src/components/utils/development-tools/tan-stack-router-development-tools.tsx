import React from 'react';
import { isProduction } from '../../../lib/utils';

export const TanStackRouterDevelopmentTools = isProduction
  ? (): null => null
  : React.lazy(() =>
      import('@tanstack/react-router-devtools').then((result) => ({
        default: result.TanStackRouterDevtools,
      })),
    );
