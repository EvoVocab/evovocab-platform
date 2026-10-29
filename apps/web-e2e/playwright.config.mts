import { defineConfig, devices } from '@playwright/test';
import { nxE2EPreset } from '@nx/playwright/preset';
import { workspaceRoot } from '@nx/devkit';

const isCI = !!process.env['CI'];

const webServerAddress = isCI ? 'http://localhost:4300' : 'http://localhost:4200';

// For CI, you may want to set BASE_URL to the deployed application.
const baseURL = process.env['BASE_URL'] || webServerAddress;

/**
- Read environment variables from file.
- https://github.com/motdotla/dotenv
 */
// import 'dotenv/config';

/**
- See https://playwright.dev/docs/test-configuration.
 *
- Generated as a .mts file so Node forces ESM regardless of workspace
- `type`. Playwright routes `.mts` through its ESM loader (dynamic import,
- bypassing the pirates CJS-compile path), and Nx's native TS strip loads
- `.mts` directly. Playwright's configLoader auto-discovers
- `playwright.config.mts` via its extension list
- (.ts/.js/.mts/.mjs/.cts/.cjs).
 */
export default defineConfig({
  ...nxE2EPreset(import.meta.dirname, { testDir: './src' }),
  /* Shared settings for all the projects below. See https://playwright.dev/docs/api/class-testoptions. */
  use: {
    baseURL,
    /* Collect trace when retrying the failed test. See https://playwright.dev/docs/trace-viewer */
    trace: 'on-first-retry',
  },
  webServer: {
    command: isCI ? 'npx nx run @evovocab/web:preview' : 'npx nx run @evovocab/web:dev',
    url: webServerAddress,
    reuseExistingServer: true,
    cwd: workspaceRoot,
  },
  projects: [
    /* ---------------------------------------------------------
       1. DESKTOP BROWSERS
       --------------------------------------------------------- */
    {
      name: 'Google Chrome',
      use: {
        ...devices['Desktop Chrome'],
        channel: 'chrome',
      },
    },
    {
      name: 'Firefox',
      use: { ...devices['Desktop Firefox'] },
    },
    {
      name: 'Safari',
      use: { ...devices['Desktop Safari'] },
    },
    // {
    //   name: 'Microsoft Edge',
    //   use: {
    //     ...devices['Desktop Edge'],
    //     channel: 'msedge',
    //   },
    // },

    /* ---------------------------------------------------------
       2. TABLETS
       --------------------------------------------------------- */
    {
      name: 'iPad Pro 11',
      use: {
        ...devices['iPad Pro 11'],
      },
    },
    {
      name: 'iPad Pro 11 landscape',
      use: {
        ...devices['iPad Pro 11 landscape'],
      },
    },

    /* ---------------------------------------------------------
       3. MOBILE DEVICES
       --------------------------------------------------------- */
    {
      name: 'iPhone 14',
      use: {
        ...devices['iPhone 14'],
      },
    },
    {
      name: 'iPhone 13 Mini',
      use: {
        ...devices['iPhone 13 Mini'],
      },
    },
    {
      name: 'Galaxy S22',
      use: {
        ...devices['Galaxy S22'],
      },
    },
  ],
});
