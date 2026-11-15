import { Configuration } from '@evovocab/shared-api';

export const config = new Configuration({
  basePath: import.meta.env.VITE_API_BASE_URL,
});
