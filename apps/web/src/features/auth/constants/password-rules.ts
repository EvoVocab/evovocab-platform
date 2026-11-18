const PASSWORD_MIN_LENGTH = 8;
const PASSWORD_MAX_LENGTH = 72;
const PASSWORD_PATTERN_SPECIAL_CHARACTERS = /[!"#$%&'()*+,\-./:;<=>?@[\\\]^_`{|}~]/;

const PASSWORD_RULES = [
  {
    id: 'length',
    label: `Từ ${PASSWORD_MIN_LENGTH} đến ${PASSWORD_MAX_LENGTH} ký tự`,
    test: (v: string) => v.length >= PASSWORD_MIN_LENGTH && v.length <= PASSWORD_MAX_LENGTH,
  },
  {
    id: 'uppercase',
    label: 'Ít nhất 1 chữ cái viết hoa (A-Z)',
    test: (v: string) => /[A-Z]/.test(v),
  },
  {
    id: 'lowercase',
    label: 'Ít nhất 1 chữ cái viết thường (a-z)',
    test: (v: string) => /[a-z]/.test(v),
  },
  {
    id: 'number',
    label: 'Ít nhất 1 chữ số (0-9)',
    test: (v: string) => /\d/.test(v),
  },
  {
    id: 'special',
    label: 'Ít nhất 1 ký tự đặc biệt (ví dụ: !@#$%^&*)',
    test: (v: string) => PASSWORD_PATTERN_SPECIAL_CHARACTERS.test(v),
  },
] as const;

export {
  PASSWORD_MIN_LENGTH,
  PASSWORD_MAX_LENGTH,
  PASSWORD_RULES,
  PASSWORD_PATTERN_SPECIAL_CHARACTERS,
};
