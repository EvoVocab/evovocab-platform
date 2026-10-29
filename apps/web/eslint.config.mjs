import nx from '@nx/eslint-plugin';
import baseConfig from '../../eslint.config.mjs';

import eslintPluginReactRefresh from 'eslint-plugin-react-refresh';
import eslintPluginUnicorn from 'eslint-plugin-unicorn';
import typescriptEslint from 'typescript-eslint';
import eslintPluginReact from 'eslint-plugin-react';
import tsParser from '@typescript-eslint/parser';

export default [
  ...nx.configs['flat/react'],
  ...baseConfig,
  {
    name: 'ignores',
    ignores: [
      'src/components/ui/**',
      'dist/**',
      'eslint.config.mjs',
      'tailwind.config.js',
      'vite.config.ts',
      'routeTree.gen.ts',
    ],
  },
  ...typescriptEslint.config(
    {
      name: 'react',
      files: ['**/*.ts', '**/*.tsx', '**/*.js', '**/*.jsx'],
      extends: [eslintPluginReact.configs.flat['jsx-runtime']],
      plugins: {
        'react-refresh': eslintPluginReactRefresh,
      },
      languageOptions: {
        parser: tsParser,
        parserOptions: {
          ecmaVersion: 'latest',
          sourceType: 'module',
          tsconfigRootDir: import.meta.dirname,
          project: ['./tsconfig.json', './tsconfig.app.json', './tsconfig.spec.json'],
        },
      },
      rules: {
        'import/no-anonymous-default-export': 'error',
        'react/jsx-boolean-value': 'error',
        'react/jsx-filename-extension': [2, { extensions: ['.js', '.jsx', '.ts', '.tsx'] }],
        'react/jsx-no-target-blank': 'off',
        'react/jsx-max-props-per-line': 'off',
        'react/jsx-sort-props': [
          'error',
          { callbacksLast: true, shorthandFirst: true, reservedFirst: true, multiline: 'last' },
        ],
        'react/no-unknown-property': 'off',
        'react/prop-types': 'off',
        'react/react-in-jsx-scope': 'off',
        'react-hooks/exhaustive-deps': 'error',
        'react-refresh/only-export-components': ['warn', { allowConstantExport: true }],
      },
    },
    {
      name: 'jsxA11y',
      files: ['**/*.ts', '**/*.tsx', '**/*.js', '**/*.jsx'],
      rules: {
        'jsx-a11y/alt-text': ['error', { elements: ['img'], img: ['Image'] }],
        'jsx-a11y/aria-props': 'error',
        'jsx-a11y/aria-proptypes': 'error',
        'jsx-a11y/aria-unsupported-elements': 'error',
        'jsx-a11y/role-has-required-aria-props': 'error',
        'jsx-a11y/role-supports-aria-props': 'error',
      },
    },
    {
      name: 'unicorn',
      files: ['**/*.ts', '**/*.tsx', '**/*.js', '**/*.jsx'],
      plugins: {
        unicorn: eslintPluginUnicorn,
      },
      rules: {
        'unicorn/custom-error-definition': 'error',
        'unicorn/empty-brace-spaces': 'error',
        'unicorn/no-array-for-each': 'off',
        'unicorn/no-array-reduce': 'off',
        'unicorn/no-console-spaces': 'error',
        'unicorn/no-null': 'off',
        'unicorn/filename-case': 'off',
        'unicorn/prevent-abbreviations': [
          'error',
          {
            replacements: {
              db: false,
              arg: false,
              args: false,
              env: false,
              fn: false,
              func: { fn: true, function: false },
              prop: false,
              props: false,
              ref: false,
              refs: false,
            },
            ignore: ['semVer', 'SemVer'],
          },
        ],
      },
    },
  ),
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
];
