const { google } = require('googleapis');
const { rootDir, resolvePath } = require('../utils');

require('dotenv').config({ path: resolvePath(rootDir + '/.env') });

const { downloadFile } = require('../drive/download-file');
const { getAuthClient } = require('../helpers/auth-helpers');

async function main() {
  const auth = await getAuthClient();
  const drive = google.drive({ version: 'v3', auth });

  const outputWebEnvPath = resolvePath(process.env.WEB_ENV_PATH);
  const outputApiEnvPath = resolvePath(process.env.API_ENV_PATH);

  await Promise.all([
    downloadFile({
      auth,
      drive,
      fileName: '.env.web.prod',
      outputPath: outputWebEnvPath,
    }),
    downloadFile({
      auth,
      drive,
      fileName: '.env.api.prod',
      outputPath: outputApiEnvPath,
    }),
  ]);

  console.log('🎉 Hoàn tất việc tải tất cả file về máy!');
}

main();
