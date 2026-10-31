const fs = require('fs');
const path = require('path');
const { google } = require('googleapis');
const readline = require('readline');

const TOKEN_PATH = path.join(__dirname, '../../token.json');
const CREDENTIALS_PATH = path.join(__dirname, '../../client_secret.json');
const SCOPES = ['https://www.googleapis.com/auth/drive'];

/**
 * Hàm lấy OAuth2 Client (Tự động xác thực hoặc yêu cầu cấp quyền nếu chưa có token)
 * @returns {Promise<google.auth.OAuth2>} Trả về đối tượng auth đã sẵn sàng sử dụng
 */
async function getAuthClient() {
  if (!fs.existsSync(CREDENTIALS_PATH)) {
    throw new Error(
      '❌ Không tìm thấy file client_secret.json! Hãy tải nó từ Google Cloud Console về.',
    );
  }

  const content = fs.readFileSync(CREDENTIALS_PATH, 'utf8');
  const credentials = JSON.parse(content);
  const { client_secret, client_id } = credentials.installed || credentials.web;

  const oAuth2Client = new google.auth.OAuth2(client_id, client_secret, 'http://localhost');

  // 1. Nếu đã có token.json, dùng luôn không cần hỏi lại
  if (fs.existsSync(TOKEN_PATH)) {
    const tokenData = JSON.parse(fs.readFileSync(TOKEN_PATH, 'utf8'));
    oAuth2Client.setCredentials(tokenData);
    return oAuth2Client;
  }

  // 2. Nếu chưa có token.json, tiến hành hướng dẫn người dùng cấp quyền 1 lần duy nhất
  console.log('\n========================================');
  console.log('🔒 CHƯA CÓ TOKEN: Yêu cầu cấp quyền Google Drive');
  console.log('1. Mở đường link này trên trình duyệt web:\n');

  const authUrl = oAuth2Client.generateAuthUrl({
    access_type: 'offline',
    scope: SCOPES,
  });
  console.log(authUrl);

  console.log('\n2. Đăng nhập Gmail và bấm "Tiếp tục/Allow".');
  console.log('3. Copy TOÀN BỘ đường link của trang lỗi (localhost) dán vào đây.');
  console.log('========================================\n');

  const inputUrl = await askQuestion('Dán link trên thanh địa chỉ vào đây: ');

  let code = inputUrl.trim();
  if (code.includes('code=')) {
    code = new URL(code).searchParams.get('code');
  }

  console.log('⏳ Đang xử lý mã xác thực...');
  const { tokens } = await oAuth2Client.getToken(code);
  oAuth2Client.setCredentials(tokens);

  // Lưu lại token để dùng cho các lần sau
  fs.writeFileSync(TOKEN_PATH, JSON.stringify(tokens, null, 2));
  console.log('✅ Đã lưu token thành công vào:', TOKEN_PATH);

  return oAuth2Client;
}

function askQuestion(query) {
  const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout,
  });

  return new Promise((resolve) => {
    rl.question(query, (answer) => {
      rl.close();
      resolve(answer);
    });
  });
}

module.exports = { getAuthClient };
