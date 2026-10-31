const { google } = require('googleapis');
const fs = require('fs');
const { rootDir } = require('../utils');

require('dotenv').config({ path: rootDir + '/.env' });

const { downloadFile } = require('../drive/download-file');
const { uploadFile } = require('../drive/upload-file');
const { getAuthClient } = require('../helpers/auth-helpers');

async function main() {
  try {
    const auth = await getAuthClient();
    const drive = google.drive({ version: 'v3', auth });
    const folderId = '1WbUMSd29N6pFgMQT-ZDb-sZhEEK6wj4q';

    const webEnvPath = rootDir + '/' + process.env.WEB_ENV_PATH;
    const apiEnvPath = rootDir + '/' + process.env.API_ENV_PATH;

    const outputWebEnvPath = rootDir + '/' + 'apps/web/.env.prod';
    const outputApiEnvPath = rootDir + '/' + 'apps/api/.env.prod';

    if (!fs.existsSync(webEnvPath) || !fs.existsSync(apiEnvPath)) {
      console.log(
        '❌ Không tìm thấy file .env.prod trong thư mục apps/web hoặc apps/api. Hãy chắc chắn rằng bạn đã tạo file này trước khi chạy script.',
      );
      console.log('📁 Đường dẫn file .env.prod trên máy tính:');
      console.log('   - Web:', webEnvPath);
      console.log('   - API:', apiEnvPath);
      return;
    }

    await Promise.all([
      uploadFile({
        auth,
        drive,
        filePath: webEnvPath,
        fileName: '.env.web.prod',
        folderId: folderId,
      }),
      uploadFile({
        auth,
        drive,
        filePath: apiEnvPath,
        fileName: '.env.api.prod',
        folderId: folderId,
      }),
    ]);

    console.log('🎉 Hoàn tất việc upload tất cả file lên Google Drive!');

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

    require('./prepare');
  } catch (error) {
    console.error('❌ Lỗi:', error.message);
  }
}

main();
