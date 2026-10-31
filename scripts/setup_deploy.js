const { execSync } = require('child_process');
const fs = require('fs');
const path = require('path');
const os = require('os');

require('dotenv').config({ path: path.resolve(__dirname, '../.env') });

const resolvePath = (p) => {
  if (!p) return '';
  if (p.startsWith('~')) {
    return path.join(os.homedir(), p.slice(1));
  }
  return path.resolve(p);
};

const runCmd = (cmd, inputData = null) => {
  try {
    const options = { stdio: inputData ? ['pipe', 'inherit', 'inherit'] : 'inherit' };
    if (inputData) options.input = inputData;
    return execSync(cmd, options);
  } catch (error) {
    console.error(`\n❌ Lỗi khi chạy lệnh: ${cmd}`);
    process.exit(1);
  }
};

const runCmdSilent = (cmd) => {
  try {
    return execSync(cmd, { stdio: 'pipe' }).toString();
  } catch (error) {
    return null;
  }
};

const REPO = process.env.REPO || 'EvoVocab/evovocab-platform';
const VPS_HOST = process.env.VPS_HOST;
const VPS_PORT = process.env.VPS_PORT || '22';
const VPS_USERNAME = process.env.VPS_USERNAME || 'evovocab';

const VPS_SSH_KEY_PATH = resolvePath(process.env.VPS_SSH_KEY_PATH);
const GH_TOKEN_PATH = resolvePath(process.env.GH_TOKEN_PATH);
const WEB_ENV_PATH = resolvePath(process.env.WEB_ENV_PATH);
const API_ENV_PATH = resolvePath(process.env.API_ENV_PATH);

if (!VPS_HOST) {
  console.error('❌ VPS_HOST không được để trống! Vui lòng cấu hình trong file .env');
  process.exit(1);
}

console.log('==========================================');
console.log('2. KIỂM TRA TOOL & ĐĂNG NHẬP GITHUB');
console.log('==========================================');

if (!runCmdSilent('gh --version')) {
  console.error('❌ Chưa tìm thấy GitHub CLI (gh). Vui lòng cài đặt công cụ này.');
  process.exit(1);
}

// Đăng nhập tự động qua TOKEN
if (fs.existsSync(GH_TOKEN_PATH)) {
  const token = fs.readFileSync(GH_TOKEN_PATH, 'utf-8');
  console.log('🔄 Đang đăng nhập GitHub CLI...');
  runCmd('gh auth login --with-token', token);
  console.log('✅ Đã đăng nhập GitHub CLI thành công.');
} else {
  console.error(`❌ Không tìm thấy file GitHub Token tại: ${GH_TOKEN_PATH}`);
  process.exit(1);
}

console.log('🔍 Kiểm tra trạng thái GitHub Pages...');
const pagesStatus = runCmdSilent(`gh api "/repos/${REPO}/pages"`);

if (pagesStatus) {
  console.log(`ℹ️ GitHub Pages đã được bật sẵn cho repo: ${REPO}`);
} else {
  console.log('🚀 GitHub Pages chưa bật. Đang tiến hành bật...');
  runCmd(`gh api -X POST "/repos/${REPO}/pages" -f "build_type=workflow"`);
  console.log('✅ Đã bật GitHub Pages thành công!');
}

console.log('==========================================');
console.log(`🚀 Bắt đầu đẩy 6 biến Secret lên repository: ${REPO}...`);
console.log('==========================================');

// ĐẨY FILE ENV (WEB & API)
if (fs.existsSync(WEB_ENV_PATH)) {
  const content = fs.readFileSync(WEB_ENV_PATH, 'utf-8');
  runCmd(`gh secret set WEB_ENV_FILE --repo "${REPO}"`, content);
  console.log(`✅ Đã cập nhật: WEB_ENV_FILE (${WEB_ENV_PATH})`);
} else {
  console.log(`⚠️ Không tìm thấy ${WEB_ENV_PATH}, bỏ qua WEB_ENV_FILE`);
}

if (fs.existsSync(API_ENV_PATH)) {
  const content = fs.readFileSync(API_ENV_PATH, 'utf-8');
  runCmd(`gh secret set API_ENV_FILE --repo "${REPO}"`, content);
  console.log(`✅ Đã cập nhật: API_ENV_FILE (${API_ENV_PATH})`);
} else {
  console.log(`⚠️ Không tìm thấy ${API_ENV_PATH}, bỏ qua API_ENV_FILE`);
}

// ĐẨY THÔNG TIN KẾT NỐI VPS
runCmd(`gh secret set VPS_HOST --repo "${REPO}" --body "${VPS_HOST}"`);
console.log('✅ Đã cập nhật: VPS_HOST (Đã ẩn giá trị)');

runCmd(`gh secret set VPS_PORT --repo "${REPO}" --body "${VPS_PORT}"`);
console.log('✅ Đã cập nhật: VPS_PORT');

runCmd(`gh secret set VPS_USERNAME --repo "${REPO}" --body "${VPS_USERNAME}"`);
console.log('✅ Đã cập nhật: VPS_USERNAME');

// ĐẨY VPS SSH KEY
if (fs.existsSync(VPS_SSH_KEY_PATH)) {
  const content = fs.readFileSync(VPS_SSH_KEY_PATH, 'utf-8');
  runCmd(`gh secret set VPS_SSH_KEY --repo "${REPO}"`, content);
  console.log(`✅ Đã cập nhật: VPS_SSH_KEY (${VPS_SSH_KEY_PATH})`);
} else {
  console.log(`⚠️ Không tìm thấy file SSH Key tại ${VPS_SSH_KEY_PATH}, bỏ qua VPS_SSH_KEY`);
}

console.log('\n🎉 Hoàn tất! Tất cả 6 biến secret đã sẵn sàng trên GitHub Actions.');
