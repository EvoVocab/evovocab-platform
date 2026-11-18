import { describe, expect, it } from 'vitest';
import { normalizeFullName } from '@evovocab/shared-utils';

describe('normalizeFullName', () => {
  describe('Basic Title Case formatting', () => {
    it('should convert all lowercase to Title Case', () => {
      expect(normalizeFullName('nguyễn văn a')).toBe('Nguyễn Văn A');
    });

    it('should convert all uppercase to Title Case', () => {
      expect(normalizeFullName('NGUYỄN VĂN A')).toBe('Nguyễn Văn A');
    });

    it('should normalize mixed casing in each word', () => {
      expect(normalizeFullName('nGUyễn vĂn A')).toBe('Nguyễn Văn A');
    });

    it('should handle single-character words and single-word names', () => {
      expect(normalizeFullName('a')).toBe('A');
      expect(normalizeFullName('đ')).toBe('Đ');
      expect(normalizeFullName('nguyễn văn a')).toBe('Nguyễn Văn A');
    });
  });

  describe('Whitespace handling', () => {
    it('should trim leading and trailing spaces', () => {
      expect(normalizeFullName('   Nguyễn Văn A   ')).toBe('Nguyễn Văn A');
    });

    it('should collapse multiple spaces, tabs, and newlines between words into a single space', () => {
      expect(normalizeFullName('Nguyễn    Văn     A')).toBe('Nguyễn Văn A');
      expect(normalizeFullName('  Nguyễn \t Văn \n A  ')).toBe('Nguyễn Văn A');
    });
  });

  describe('Unicode normalization (NFC vs NFD)', () => {
    it('should normalize NFD decomposed Vietnamese characters into standard NFC form', () => {
      // Chuỗi NFD (thường gặp khi gõ trên macOS/iOS hoặc copy từ PDF):
      // 'Nguyễn' ở dạng NFD = 'Nguye\u0302\u0303n'
      const nfdInput = 'Nguye\u0302\u0303n';
      const expectedNfc = 'Nguyễn';

      const result = normalizeFullName(nfdInput);

      expect(result).toBe(expectedNfc);
      expect(result.length).toBe(expectedNfc.length); // Đảm bảo byte/code-unit length khớp chuẩn NFC
    });

    it('should properly uppercase Vietnamese special characters like đ/Đ, ă/Ă, â/Â, ê/Ê, ô/Ô, ơ/Ơ, ư/Ư', () => {
      expect(normalizeFullName('đặng ănh ân êm ôn ơn ưng')).toBe('Đặng Ănh Ân Êm Ôn Ơn Ưng');
      expect(normalizeFullName('ĐẶNG ĂN ÂN ÊM ÔN ƠN ƯNG')).toBe('Đặng Ăn Ân Êm Ôn Ơn Ưng');
    });
  });

  describe('Edge cases', () => {
    it('should return an empty string when input is empty or whitespace-only', () => {
      expect(normalizeFullName('')).toBe('');
      expect(normalizeFullName('     ')).toBe('');
      expect(normalizeFullName('\t\n  ')).toBe('');
    });

    it('should preserve hyphens or apostrophes inside compound names without breaking', () => {
      // Lưu ý hành vi hiện tại: split(' ') chỉ tách theo dấu cách
      expect(normalizeFullName('jean-luc picard')).toBe('Jean-luc Picard');
      expect(normalizeFullName("o'connor")).toBe("O'connor");
    });
  });
});
