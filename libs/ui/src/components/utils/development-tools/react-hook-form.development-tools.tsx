import React from 'react';
import { isProduction } from '@evovocab/shared-utils';

export const ReactHookFormDevelopmentTools = isProduction(import.meta.env)
  ? (): null => null
  : React.lazy(() =>
      import('@hookform/devtools').then((result) => ({
        default: result.DevTool,
      })),
    );
