const path = require('path');
const fs = require('fs');

/**
 * Tải file từ Google Drive về máy (Hỗ trợ tái sử dụng)
 * @param {Object} options - Các tham số đầu vào
 * @param {string} [options.fileId] - ID của file trên Drive (Ưu tiên dùng cách này nếu biết ID)
 * @param {string} [options.fileName] - Tên file trên Drive (Dùng nếu không có ID, code sẽ tự tìm)
 * @param {string} options.outputPath - Đường dẫn tuyệt đối hoặc tương đối để lưu file về máy
 */
async function downloadFile({ auth, drive, fileId, fileName, outputPath }) {
  try {
    if (!auth || !drive) {
      throw new Error(
        '❌ Thiếu thông tin xác thực hoặc đối tượng drive. Hãy đảm bảo đã gọi getAuthClient() trước.',
      );
    }

    if (!outputPath) {
      throw new Error('❌ Bạn phải cung cấp đường dẫn outputPath để lưu file về máy!');
    }

    let targetFileId = fileId;

    if (!targetFileId && fileName) {
      console.log(`🔍 Đang tìm file có tên: "${fileName}" trên Drive...`);
      const res = await drive.files.list({
        q: `name = '${fileName}' and trashed = false`,
        fields: 'files(id, name)',
      });

      const files = res.data.files;
      if (!files || files.length === 0) {
        throw new Error(`Không tìm thấy file có tên là: ${fileName}`);
      }
      targetFileId = files[0].id;
    }

    if (!targetFileId) {
      throw new Error('❌ Bạn phải cung cấp ít nhất `fileId` hoặc `fileName`!');
    }

    const dir = path.dirname(outputPath);
    if (!fs.existsSync(dir)) {
      fs.mkdirSync(dir, { recursive: true });
    }

    console.log(`📥 Đang tải file (ID: ${targetFileId}) về: ${outputPath}...`);

    const dest = fs.createWriteStream(outputPath);
    const fileStream = await drive.files.get(
      { fileId: targetFileId, alt: 'media' },
      { responseType: 'stream' },
    );

    return new Promise((resolve, reject) => {
      fileStream.data
        .on('end', () => {
          console.log(`✅ Tải thành công: ${path.basename(outputPath)}`);
          resolve(outputPath);
        })
        .on('error', (err) => {
          console.error(`❌ Lỗi stream khi tải file:`, err.message);
          reject(err);
        })
        .pipe(dest);
    });
  } catch (error) {
    console.error(`❌ Lỗi downloadFile:`, error.message);
    throw error;
  }
}

module.exports = { downloadFile };
