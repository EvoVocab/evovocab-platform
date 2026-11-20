import { describe, expect, it } from 'vitest';
import {
  PASSWORD_MAX_LENGTH,
  PASSWORD_MIN_LENGTH,
  PASSWORD_PATTERN_SPECIAL_CHARACTERS,
  PASSWORD_RULES,
} from './password-rules';

describe('Password Constants & Validation Rules', () => {
  const getRuleTest = (id: (typeof PASSWORD_RULES)[number]['id']) => {
    const rule = PASSWORD_RULES.find((r) => r.id === id);
    if (!rule) throw new Error(`Rule "${id}" not found`);
    return rule.test;
  };
  describe('Constants values & structure', () => {
    it('should define expected min and max length', () => {
      expect(PASSWORD_MIN_LENGTH).toBe(8);
      expect(PASSWORD_MAX_LENGTH).toBe(72);
    });

    it('should have 5 distinct rules in exact order', () => {
      const ruleIds = PASSWORD_RULES.map((rule) => rule.id);
      expect(ruleIds).toEqual(['length', 'uppercase', 'lowercase', 'number', 'special']);
    });

    it('should interpolate min and max length into the length rule label', () => {
      const lengthRule = PASSWORD_RULES.find((r) => r.id === 'length');
      expect(lengthRule?.label).toBe('Từ 8 đến 72 ký tự');
    });
  });

  describe('Rule: length', () => {
    const testLength = getRuleTest('length');

    it('should fail when empty or shorter than min length', () => {
      expect(testLength('')).toBe(false);
      expect(testLength('a'.repeat(PASSWORD_MIN_LENGTH - 1))).toBe(false);
    });

    it('should pass at exact min and max boundaries', () => {
      expect(testLength('a'.repeat(PASSWORD_MIN_LENGTH))).toBe(true);
      expect(testLength('a'.repeat(PASSWORD_MAX_LENGTH))).toBe(true);
    });

    it('should fail when exceeding max length', () => {
      expect(testLength('a'.repeat(PASSWORD_MAX_LENGTH + 1))).toBe(false);
    });
  });

  describe('Rule: uppercase', () => {
    const testUppercase = getRuleTest('uppercase');

    it('should pass when containing at least one ASCII uppercase letter', () => {
      expect(testUppercase('A')).toBe(true);
      expect(testUppercase('passwordZ1!')).toBe(true);
    });

    it('should fail when containing no ASCII uppercase letters', () => {
      expect(testUppercase('')).toBe(false);
      expect(testUppercase('password123!')).toBe(false);
      // Kiểm tra ký tự có dấu tiếng Việt (chỉ chấp nhận A-Z chuẩn ASCII)
      expect(testUppercase('ăâđêôơư')).toBe(false);
      expect(testUppercase('ĂÂĐÊÔƠƯ')).toBe(false);
    });
  });

  describe('Rule: lowercase', () => {
    const testLowercase = getRuleTest('lowercase');

    it('should pass when containing at least one ASCII lowercase letter', () => {
      expect(testLowercase('a')).toBe(true);
      expect(testLowercase('PASSWORDz1!')).toBe(true);
    });

    it('should fail when containing no ASCII lowercase letters', () => {
      expect(testLowercase('')).toBe(false);
      expect(testLowercase('PASSWORD123!')).toBe(false);
      expect(testLowercase('ăâđêôơư')).toBe(false);
    });
  });

  describe('Rule: number', () => {
    const testNumber = getRuleTest('number');

    it('should pass when containing at least one digit (0-9)', () => {
      expect(testNumber('0')).toBe(true);
      expect(testNumber('Pass9word!')).toBe(true);
    });

    it('should fail when containing no digits', () => {
      expect(testNumber('')).toBe(false);
      expect(testNumber('Password!@#')).toBe(false);
    });
  });

  describe('Rule: special', () => {
    const testSpecial = getRuleTest('special');

    it('should pass for every allowed OWASP ASCII special character', () => {
      const allAllowedSpecialChars = `!"#$%&'()*+,-./:;<=>?@[\\]^_\`{|}~`;

      for (const char of allAllowedSpecialChars) {
        expect(testSpecial(char), `Expected special character "${char}" to pass`).toBe(true);
        expect(PASSWORD_PATTERN_SPECIAL_CHARACTERS.test(char)).toBe(true);
      }
    });

    it('should fail for alphanumeric characters, whitespace, and non-ASCII symbols', () => {
      expect(testSpecial('')).toBe(false);
      expect(testSpecial('Password123')).toBe(false);
      expect(testSpecial('   ')).toBe(false);
      expect(testSpecial('\t\n')).toBe(false);
      expect(testSpecial('₫€¥©®™😀')).toBe(false);
    });
  });

  describe('Combined: All rules together', () => {
    const passesAllRules = (password: string) =>
      PASSWORD_RULES.every((rule) => rule.test(password));

    it('should pass a strong valid password', () => {
      expect(passesAllRules('StrongP@ss1')).toBe(true);
    });

    it('should fail if any single criterion is missing', () => {
      expect(passesAllRules('Short1!')).toBe(false); // Thiếu độ dài (7 ký tự)
      expect(passesAllRules('nouppercase1!')).toBe(false); // Thiếu chữ hoa
      expect(passesAllRules('NOLOWERCASE1!')).toBe(false); // Thiếu chữ thường
      expect(passesAllRules('NoNumberHere!')).toBe(false); // Thiếu số
      expect(passesAllRules('NoSpecialChar1')).toBe(false); // Thiếu ký tự đặc biệt
    });
  });
});
