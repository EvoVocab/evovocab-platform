const fs = require('fs');
const path = require('path');

/**
 * Upload file lên Google Drive (Hỗ trợ thay thế nếu file đã tồn tại cùng tên trong thư mục)
 *
 * @param {Object} options - Các tham số đầu vào
 * @param {Object} options.auth - Đối tượng xác thực OAuth2 client
 * @param {Object} options.drive - Đối tượng google.drive({version: 'v3', auth})
 * @param {string} options.filePath - Đường dẫn file trên máy tính cần upload
 * @param {string} [options.fileName] - Tên hiển thị file trên Drive
 * @param {string} options.folderId - ID của thư mục trên Google Drive để chứa file upload
 * @param {string} [options.mimeType='text/plain'] - Kiểu định dạng của file
 * @returns {Promise<Object>} Trả về thông tin chi tiết file vừa upload/cập nhật (bao gồm file.id)
 */
async function uploadFile({ auth, drive, filePath, fileName, folderId, mimeType = 'text/plain' }) {
  try {
    if (!auth || !drive) {
      throw new Error(
        '❌ Thiếu thông tin xác thực hoặc đối tượng drive. Hãy đảm bảo đã gọi getAuthClient() trước.',
      );
    }

    if (!filePath) {
      throw new Error('❌ Bạn phải cung cấp đường dẫn filePath của file cần upload!');
    }

    if (!folderId) {
      throw new Error('❌ Bạn phải cung cấp folderId để biết vị trí lưu file trên Drive!');
    }

    if (!fs.existsSync(filePath)) {
      throw new Error(`❌ File không tồn tại tại đường dẫn: ${filePath}`);
    }

    const targetFileName = fileName || path.basename(filePath);

    const searchRes = await drive.files.list({
      q: `'${folderId}' in parents and name = '${targetFileName}' and trashed = false`,
      fields: 'files(id, name)',
    });

    const existingFiles = searchRes.data.files;
    let response;

    if (existingFiles && existingFiles.length > 0) {
      const fileId = existingFiles[0].id;
      console.log(
        `🔄 File "${targetFileName}" đã tồn tại trên Drive (ID: ${fileId}). Đang tiến hành thay thế...`,
      );

      response = await drive.files.update({
        fileId: fileId,
        media: {
          mimeType: mimeType,
          body: fs.createReadStream(filePath),
        },
      });

      console.log(`✅ Thay thế (Update) thành công! File ID: ${response.data.id}`);
    } else {
      console.log(`📤 Đang upload file mới "${targetFileName}" lên Google Drive...`);

      response = await drive.files.create({
        requestBody: {
          name: targetFileName,
          mimeType: mimeType,
          parents: [folderId],
        },
        media: {
          mimeType: mimeType,
          body: fs.createReadStream(filePath),
        },
      });

      console.log(`✅ Upload mới thành công! File ID: ${response.data.id}`);
    }

    return response.data;
  } catch (error) {
    console.error(`❌ Lỗi uploadFile:`, error.message);
    throw error;
  }
}

module.exports = { uploadFile };
