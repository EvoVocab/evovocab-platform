import React from 'react';
import { isProduction } from '@evovocab/shared-utils';

export const TanStackTableDevelopmentTools = isProduction(import.meta.env)
  ? (): null => null
  : React.lazy(() =>
      import('@tanstack/react-table-devtools').then((result) => ({
        default: result.ReactTableDevtools,
      })),
    );
