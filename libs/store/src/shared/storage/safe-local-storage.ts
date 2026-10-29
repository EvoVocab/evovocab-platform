import type { StateStorage } from 'zustand/middleware';

export const safeLocalStorage: StateStorage = {
  getItem: (name: string): string | null => {
    if (typeof window === 'undefined' || !window.localStorage) return null;
    try {
      return window.localStorage.getItem(name);
    } catch {
      return null;
    }
  },

  setItem: (name: string, value: string): void => {
    if (typeof window === 'undefined' || !window.localStorage) return;
    try {
      window.localStorage.setItem(name, value);
    } catch (error) {
      const message = error instanceof Error ? error.message : String(error);
      console.error(`[Storage Error] Cannot set item ${name}:`, message);
    }
  },

  removeItem: (name: string): void => {
    if (typeof window === 'undefined' || !window.localStorage) return;
    try {
      window.localStorage.removeItem(name);
    } catch (error) {
      const message = error instanceof Error ? error.message : String(error);
      console.error(`[Storage Error] Cannot remove item ${name}:`, message);
    }
  },
};
