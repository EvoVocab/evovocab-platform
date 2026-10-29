module.exports = {
  extends: ['@commitlint/config-conventional'],
  rules: {
    'type-enum': [
      2,
      'always',
      [
        'feat',
        'fix',
        'perf',
        'refactor',
        'docs',
        'style',
        'test',
        'build',
        'ci',
        'chore',
        'revert',
      ],
    ],
    'scope-enum': [2, 'always', ['web', 'api', 'web-e2e', 'ui', 'deps', 'release', 'repo', 'ci']],
    'scope-empty': [2, 'never'],
    'subject-case': [2, 'always', ['lower-case', 'sentence-case']],
    'subject-full-stop': [2, 'never', '.'],
    'header-max-length': [2, 'always', 100],
  },
};
