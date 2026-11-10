/// <reference types='vitest' />
import { defineConfig, loadEnv } from 'vite';
import { tanstackRouter } from '@tanstack/router-plugin/vite';
import react from '@vitejs/plugin-react';
import { nxViteTsPaths } from '@nx/vite/plugins/nx-tsconfig-paths.plugin';
import tailwindcss from '@tailwindcss/vite';
import { ValidateEnv } from '@julr/vite-plugin-validate-env';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '') as unknown as ImportMetaEnv;
  const isProduction = mode === 'production';

  return {
    base: isProduction ? env.VITE_APP_BASE_URL : '/',
    root: import.meta.dirname,
    cacheDir: '../../node_modules/.vite/apps/web',
    server: {
      port: 4200,
      host: 'localhost',
    },
    preview: {
      port: 4300,
      host: 'localhost',
    },
    plugins: [
      tanstackRouter({
        autoCodeSplitting: true,
        routeFileIgnorePattern: isProduction ? '.*playground.*' : undefined,
      }),
      react(),
      nxViteTsPaths(),
      tailwindcss(),
      ValidateEnv({
        configFile: './src/config/env',
      }),
    ],
    // Uncomment this if you are using workers.
    // worker: {
    //  plugins: [],
    // },
    resolve: {
      tsconfigPaths: true,
    },
    build: {
      outDir: './dist',
      emptyOutDir: true,
      reportCompressedSize: true,
      commonjsOptions: {
        transformMixedEsModules: true,
      },
      rollupOptions: {
        output: {
          manualChunks(id) {
            if (id.includes('node_modules/react/') || id.includes('node_modules/react-dom/')) {
              return 'vendor-react';
            }

            if (id.includes('node_modules/@nivo/') || id.includes('node_modules/d3')) {
              return 'vendor-charts';
            }

            if (id.includes('node_modules/@tanstack/') || id.includes('node_modules/zustand/')) {
              return 'vendor-core-state';
            }

            if (
              id.includes('node_modules/@base-ui/') ||
              id.includes('node_modules/lucide-react/') ||
              id.includes('node_modules/class-variance-authority/') ||
              id.includes('node_modules/tailwind')
            ) {
              return 'vendor-ui';
            }

            if (id.includes('node_modules/@react-oauth/')) {
              return 'vendor-auth';
            }

            if (
              id.includes('node_modules/dayjs/') ||
              id.includes('node_modules/zod/') ||
              id.includes('node_modules/http-status-codes/')
            ) {
              return 'vendor-utils';
            }

            if (id.includes('node_modules/')) {
              return 'vendor-others';
            }

            return;
          },
        },
      },
    },
    test: {
      name: '@evovocab/web',
      watch: false,
      globals: true,
      environment: 'jsdom',
      setupFiles: ['./vitest.setup.ts'],
      include: ['{src,tests}/**/*.{test,spec}.{js,mjs,cjs,ts,mts,cts,jsx,tsx}'],
      reporters: ['default'],
      coverage: {
        reportsDirectory: './test-output/vitest/coverage',
        provider: 'v8' as const,
      },
    },
  };
});
