import nx from '@nx/eslint-plugin';
import eslintJS from '@eslint/js';
import eslintConfigPrettier from 'eslint-config-prettier';
import typescriptEslint from 'typescript-eslint';
import tsParser from '@typescript-eslint/parser';
import globals from 'globals';

export default [
  ...nx.configs['flat/base'],
  ...nx.configs['flat/typescript'],
  ...nx.configs['flat/javascript'],
  {
    ignores: [
      '**/dist',
      '**/out-tsc',
      '**/vite.config.*.timestamp*',
      '**/vitest.config.*.timestamp*',
      '**/test-output',
    ],
  },
  {
    files: ['**/*.ts', '**/*.tsx', '**/*.js', '**/*.jsx'],
    rules: {
      '@nx/enforce-module-boundaries': [
        'error',
        {
          enforceBuildableLibDependency: true,
          allow: ['^.*/eslint(\\.base)?\\.config\\.[cm]?[jt]s$'],
          depConstraints: [
            {
              sourceTag: '*',
              onlyDependOnLibsWithTags: ['*'],
            },
          ],
        },
      ],
    },
  },
  ...typescriptEslint.config(
    {
      name: 'base-eslint',
      files: ['**/*.ts', '**/*.tsx', '**/*.js', '**/*.jsx'],
      extends: [eslintJS.configs.recommended],
      rules: {
        'no-await-in-loop': 'error',
        'no-constant-binary-expression': 'error',
        'no-duplicate-imports': 'error',
        'no-new-native-nonconstructor': 'error',
        'no-promise-executor-return': 'error',
        'no-self-compare': 'error',
        'no-template-curly-in-string': 'error',
        'no-unmodified-loop-condition': 'error',
        'no-unreachable-loop': 'error',
        'no-unused-private-class-members': 'error',
        'no-use-before-define': 'error',
        'require-atomic-updates': 'error',
        camelcase: 'error',
      },
    },
    {
      name: 'typescript',
      files: ['**/*.ts', '**/*.tsx'],
      extends: [...typescriptEslint.configs.recommendedTypeChecked],
      languageOptions: {
        parser: tsParser,
        parserOptions: {
          ecmaFeatures: { modules: true },
          ecmaVersion: 'latest',
          tsconfigRootDir: import.meta.dirname,
          project: ['./tsconfig.json', './tsconfig.base.json', 'libs/ui/tsconfig.storybook.json'],
        },
        globals: {
          ...globals.builtin,
          ...globals.browser,
          ...globals.es2025,
        },
      },
      linterOptions: {
        reportUnusedDisableDirectives: 'error',
      },
      rules: {
        '@typescript-eslint/adjacent-overload-signatures': 'error',
        '@typescript-eslint/array-type': ['error', { default: 'generic' }],
        '@typescript-eslint/consistent-type-exports': 'error',
        '@typescript-eslint/consistent-type-imports': 'error',
        '@typescript-eslint/explicit-function-return-type': 'error',
        '@typescript-eslint/explicit-member-accessibility': 'error',
        '@typescript-eslint/explicit-module-boundary-types': 'error',
        '@typescript-eslint/no-confusing-void-expression': 'error',
        '@typescript-eslint/no-import-type-side-effects': 'error',
        '@typescript-eslint/no-require-imports': 'error',
        '@typescript-eslint/no-unused-vars': 'error',
        '@typescript-eslint/no-useless-empty-export': 'error',
        '@typescript-eslint/prefer-enum-initializers': 'error',
        '@typescript-eslint/prefer-readonly': 'error',
        'no-return-await': 'off',
        '@typescript-eslint/return-await': 'error',
        '@typescript-eslint/no-misused-promises': [
          'error',
          { checksVoidReturn: { attributes: false } },
        ],
        '@typescript-eslint/adjacent-overload-signatures': 'error',
        '@typescript-eslint/explicit-function-return-type': 'off',
        '@typescript-eslint/explicit-module-boundary-types': 'off',
      },
      settings: {
        'import/resolver': {
          typescript: {
            alwaysTryTypes: true,
            project: ['./tsconfig.json', './tsconfig.base.json', 'libs/ui/tsconfig.storybook.json'],
          },
        },
      },
    },
    eslintConfigPrettier,
  ),
];
