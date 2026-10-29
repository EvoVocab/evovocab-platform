import { defineConfig } from '@julr/vite-plugin-validate-env';
import { z } from 'zod';

export default defineConfig({
  validator: 'standard',
  schema: {
    VITE_APP_TITLE: z.string(),
    VITE_APP_BASE_URL: z.string(),
  },
});
