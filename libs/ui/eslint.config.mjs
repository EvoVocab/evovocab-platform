import nx from '@nx/eslint-plugin';
import baseConfig from '../../eslint.config.mjs';
import globals from 'globals';
import tsParser from '@typescript-eslint/parser';
export default [
  ...baseConfig,
  ...nx.configs['flat/react'],
  {
    files: ['./src/**/*.ts', './src/**/*.tsx', './src/**/*.js', './src/**/*.jsx'],
    languageOptions: {
      globals: {
        ...globals.browser,
      },
      parserOptions: {
        project: ['./tsconfig.json', './tsconfig.lib.json', './tsconfig.spec.json'],
        tsconfigRootDir: import.meta.dirname,
        ecmaFeatures: {
          jsx: true,
        },
      },
    },
    rules: {
      'react/react-in-jsx-scope': 'off',
      'react/prop-types': 'off',
      '@typescript-eslint/no-unused-vars': [
        'warn',
        { argsIgnorePattern: '^_', varsIgnorePattern: '^_' },
      ],
    },
  },
  {
    files: ['**/*.spec.ts', '**/*.spec.tsx', '**/*.test.ts', '**/*.test.tsx'],
    rules: {
      '@typescript-eslint/no-unsafe-assignment': 'off',
      '@typescript-eslint/no-unsafe-call': 'off',
      '@typescript-eslint/no-unsafe-member-access': 'off',
      '@typescript-eslint/no-explicit-any': 'off',
    },
    languageOptions: {
      parser: tsParser,
      parserOptions: {
        ecmaVersion: 'latest',
        sourceType: 'module',
        tsconfigRootDir: import.meta.dirname,
        project: ['./tsconfig.spec.json'],
      },
    },
  },
  {
    ignores: ['**/out-tsc', '**/dist', '**/build', '.eslintrc.json', 'storybook-static/**'],
  },
];
