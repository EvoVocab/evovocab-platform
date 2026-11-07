import { create } from 'zustand';
import { toast } from 'sonner';
import { persist, createJSONStorage, devtools } from 'zustand/middleware';
import { createSafeStorage, loggerMiddleware } from '../shared';
import { isDevelopment } from '@evovocab/shared-utils';

interface OAuthState {
  oauthState: string | null;
  setOAuthState: (state: string) => void;
  resetOAuthState: () => void;
  generateOAuthState: () => string;
}

const useOAuthStore = create<OAuthState>()(
  devtools(
    loggerMiddleware(
      persist(
        (set) => ({
          oauthState: null,
          setOAuthState: (oauthState) => set({ oauthState }),
          resetOAuthState: () => set({ oauthState: null }),
          generateOAuthState: () => {
            const newState = crypto.randomUUID();
            set({ oauthState: newState });
            return newState;
          },
        }),
        {
          name: 'oauth-storage',
          partialize: (state) => ({
            oauthState: state.oauthState,
          }),
          storage: createJSONStorage(() =>
            createSafeStorage('local', () => {
              toast.error(`Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau`);
            }),
          ),
        },
      ),
      'OAuthStore',
    ),
    {
      name: 'OAuthStore',
      enabled: isDevelopment(import.meta.env),
    },
  ),
);

export { useOAuthStore };
