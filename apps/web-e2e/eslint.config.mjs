import nx from '@nx/eslint-plugin';
import baseConfig from '../../eslint.config.mjs';
import playwright from 'eslint-plugin-playwright';
import tsParser from '@typescript-eslint/parser';

export default [
  ...baseConfig,
  ...nx.configs['flat/typescript'],

  {
    ignores: [
      'playwright-report/**',
      'test-results/**',
      'blob-report/**',
      'eslint.config.mjs',
      'playwright.config.mts',
    ],
  },
  {
    files: ['**/*.ts', '**/*.spec.ts', '**/*.e2e.ts'],
    plugins: {
      playwright,
    },
    languageOptions: {
      parser: tsParser,
      parserOptions: {
        ecmaVersion: 'latest',
        sourceType: 'module',
        tsconfigRootDir: import.meta.dirname,
        project: ['./tsconfig.json'],
      },
    },
    rules: {
      ...playwright.configs['flat/recommended'].rules,
      '@typescript-eslint/no-non-null-assertion': 'off',
      '@typescript-eslint/no-floating-promises': 'error',
      'playwright/expect-expect': 'warn',
      'playwright/no-wait-for-timeout': 'warn',
    },
  },
];
