#!/bin/bash

# Dừng script nếu có lỗi
set -e

# ==========================================
# 1. CẤU HÌNH THÔNG TIN DỰ ÁN & VPS
# ==========================================
REPO="EvoVocab/evovocab-platform"

# Đã xóa IP cứng VPS_HOST ở đây để bảo mật
VPS_PORT="22"
VPS_USERNAME="evovocab"

# Đường dẫn mặc định (nếu bấm Enter bỏ qua)
DEFAULT_SSH_KEY="$HOME/.ssh/evovocab/vps-ssh-key.pem"
DEFAULT_GH_TOKEN="$HOME/.config/secrets/evovocab_platform/gh_access_token"
DEFAULT_WEB_ENV="apps/web/.env.prod"
DEFAULT_API_ENV="apps/api/.env.prod"

echo "----------------------------------------------------"
echo "📥 CẤU HÌNH ĐƯỜNG DẪN KẾT NỐI (Bấm TAB để Auto-complete)"
echo "----------------------------------------------------"

# 1.1 Nhập VPS_HOST (Yêu cầu nhập tay, ẩn khi gõ)
read -s -p "🌐 Nhập địa chỉ VPS_HOST (IP hoặc Domain) [Ẩn khi gõ]: " VPS_HOST
echo "" # Xuống dòng sau khi nhập ẩn
if [ -z "$VPS_HOST" ]; then
  echo "❌ VPS_HOST không được để trống!"
  exit 1
fi

# 1.2 Nhập VPS_SSH_KEY_PATH (Tham số $1 hoặc nhập tay)
VPS_SSH_KEY_PATH="${1:-}"
if [ -z "$VPS_SSH_KEY_PATH" ]; then
  read -e -p "🔑 Đường dẫn VPS_SSH_KEY_PATH [$DEFAULT_SSH_KEY]: " INPUT_SSH_KEY
  VPS_SSH_KEY_PATH="${INPUT_SSH_KEY:-$DEFAULT_SSH_KEY}"
fi
VPS_SSH_KEY_PATH="${VPS_SSH_KEY_PATH/#\~/$HOME}"

# 1.3 Nhập GH_TOKEN_PATH (Tham số $2 hoặc nhập tay)
GH_TOKEN_PATH="${2:-}"
if [ -z "$GH_TOKEN_PATH" ]; then
  read -e -p "🔑 Đường dẫn GH_TOKEN_PATH [$DEFAULT_GH_TOKEN]: " INPUT_GH_TOKEN
  GH_TOKEN_PATH="${INPUT_GH_TOKEN:-$DEFAULT_GH_TOKEN}"
fi
GH_TOKEN_PATH="${GH_TOKEN_PATH/#\~/$HOME}"

# 1.4 Nhập WEB_ENV_PATH (Tham số $3 hoặc nhập tay)
WEB_ENV_PATH="${3:-}"
if [ -z "$WEB_ENV_PATH" ]; then
  read -e -p "📄 Đường dẫn WEB_ENV_PATH [$DEFAULT_WEB_ENV]: " INPUT_WEB_ENV
  WEB_ENV_PATH="${INPUT_WEB_ENV:-$DEFAULT_WEB_ENV}"
fi
WEB_ENV_PATH="${WEB_ENV_PATH/#\~/$HOME}"

# 1.5 Nhập API_ENV_PATH (Tham số $4 hoặc nhập tay)
API_ENV_PATH="${4:-}"
if [ -z "$API_ENV_PATH" ]; then
  read -e -p "📄 Đường dẫn API_ENV_PATH [$DEFAULT_API_ENV]: " INPUT_API_ENV
  API_ENV_PATH="${INPUT_API_ENV:-$DEFAULT_API_ENV}"
fi
API_ENV_PATH="${API_ENV_PATH/#\~/$HOME}"

echo "----------------------------------------------------"

# ==========================================
# 2. KIỂM TRA TOOL & ĐĂNG NHẬP GITHUB
# ==========================================
if ! command -v gh &> /dev/null; then
  echo "❌ Chưa tìm thấy GitHub CLI (gh). Vui lòng cài đặt: sudo apt install gh"
  exit 1
fi

# Đăng nhập tự động qua TOKEN
if [ -f "$GH_TOKEN_PATH" ]; then
  cat "$GH_TOKEN_PATH" | gh auth login --with-token
  echo "✅ Đã đăng nhập GitHub CLI thành công."
else
  echo "❌ Không tìm thấy file GitHub Token tại: $GH_TOKEN_PATH"
  exit 1
fi

echo "🔍 Kiểm tra trạng thái GitHub Pages..."

# Kiểm tra nếu GitHub Pages đã được bật chưa
if gh api "/repos/$REPO/pages" &> /dev/null; then
  echo "ℹ️ GitHub Pages đã được bật sẵn cho repo: $REPO"
else
  echo "🚀 GitHub Pages chưa bật. Đang tiến hành bật..."
  gh api -X POST "/repos/$REPO/pages" -f "build_type=workflow"
  echo "✅ Đã bật GitHub Pages thành công!"
fi

echo "🚀 Bắt đầu đẩy 6 biến Secret lên repository: $REPO..."

# ==========================================
# 3. ĐẨY FILE ENV (WEB & API)
# ==========================================
if [ -f "$WEB_ENV_PATH" ]; then
  gh secret set WEB_ENV_FILE --repo "$REPO" < "$WEB_ENV_PATH"
  echo "✅ Đã tạo/cập nhật: WEB_ENV_FILE ($WEB_ENV_PATH)"
else
  echo "⚠️  Không tìm thấy $WEB_ENV_PATH, bỏ qua WEB_ENV_FILE"
fi

if [ -f "$API_ENV_PATH" ]; then
  gh secret set API_ENV_FILE --repo "$REPO" < "$API_ENV_PATH"
  echo "✅ Đã tạo/cập nhật: API_ENV_FILE ($API_ENV_PATH)"
else
  echo "⚠️  Không tìm thấy $API_ENV_PATH, bỏ qua API_ENV_FILE"
fi

# ==========================================
# 4. ĐẨY THÔNG TIN KẾT NỐI VPS
# ==========================================
gh secret set VPS_HOST --repo "$REPO" --body "$VPS_HOST"
echo "✅ Đã tạo/cập nhật: VPS_HOST (Đã ẩn giá trị)"

gh secret set VPS_PORT --repo "$REPO" --body "$VPS_PORT"
echo "✅ Đã tạo/cập nhật: VPS_PORT"

gh secret set VPS_USERNAME --repo "$REPO" --body "$VPS_USERNAME"
echo "✅ Đã tạo/cập nhật: VPS_USERNAME"

if [ -f "$VPS_SSH_KEY_PATH" ]; then
  gh secret set VPS_SSH_KEY --repo "$REPO" < "$VPS_SSH_KEY_PATH"
  echo "✅ Đã tạo/cập nhật: VPS_SSH_KEY ($VPS_SSH_KEY_PATH)"
else
  echo "⚠️  Không tìm thấy file SSH Key tại $VPS_SSH_KEY_PATH, bỏ qua VPS_SSH_KEY"
fi

echo "🎉 Hoàn tất! Tất cả 6 biến secret đã sẵn sàng trên GitHub Actions."