/// <reference types="vite/client" />

import type { ImportMetaEnvAugmented as ImportMetaEnvAugmentedType } from '@julr/vite-plugin-validate-env';
import type env from './config/env';

type ImportMetaEnvAugmented = ImportMetaEnvAugmentedType<typeof env>;

declare global {
  interface ViteTypeOptions {
    // Avoid adding an index type to `ImportMetaDev` so
    // there's an error when accessing unknown properties.
    // ⚠️ This option requires Vite 6.3.x or higher
    strictImportMetaEnv: unknown;
  }

  // eslint-disable-next-line @typescript-eslint/no-empty-object-type, @typescript-eslint/no-empty-interface
  interface ImportMetaEnv extends Readonly<ImportMetaEnvAugmented> {}
}
