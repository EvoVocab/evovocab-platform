import '@testing-library/jest-dom/vitest';
import { vi } from 'vitest';

vi.mock('./src/contexts/theme-provider', () => ({
  useTheme: vi.fn(),
}));
