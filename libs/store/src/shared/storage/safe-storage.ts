import type { StateStorage } from 'zustand/middleware';

type StorageType = 'local' | 'session';

export const createSafeStorage = (type: StorageType, onError?: () => void): StateStorage => {
  const getBrowserStorage = (): Storage | null => {
    if (typeof window === 'undefined') return null;
    return type === 'session' ? window.sessionStorage : window.localStorage;
  };

  return {
    getItem: (name: string): string | null => {
      const storage = getBrowserStorage();
      if (!storage) return null;
      try {
        return storage.getItem(name);
      } catch {
        return null;
      }
    },

    setItem: (name: string, value: string): void => {
      const storage = getBrowserStorage();
      if (!storage) return;
      try {
        storage.setItem(name, value);
      } catch (error) {
        if (onError) onError();
        console.error(`[${type.toUpperCase()} Error] Cannot set ${name}:`, error);
      }
    },

    removeItem: (name: string): void => {
      const storage = getBrowserStorage();
      if (!storage) return;
      try {
        storage.removeItem(name);
      } catch (error) {
        if (onError) onError();
        console.error(`[${type.toUpperCase()} Error] Cannot remove ${name}:`, error);
      }
    },
  };
};
