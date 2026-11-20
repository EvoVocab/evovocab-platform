const isDevelopment = (env: ImportMetaEnv) => env.DEV;
const isProduction = (env: ImportMetaEnv) => env.PROD;

function normalizeFullName(raw: string): string {
  return raw
    .normalize('NFC')
    .trim()
    .replace(/\s+/g, ' ')
    .split(' ')
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
    .join(' ');
}

export { isDevelopment, isProduction, normalizeFullName };
