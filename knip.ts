import type { KnipConfig } from 'knip';

const config: KnipConfig = {
  ignoreDependencies: [
    '@babel/*',
    '@tailwindcss/postcss',
    'autoprefixer',
    'eslint-plugin-*',
    'postcss',
    'ts-node',
    '@swc/cli',
    'ajv',
  ],
  ignore: ['apps/web/src/test/**/*'],
};

export default config;
