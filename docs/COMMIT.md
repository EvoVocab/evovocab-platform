# Quy ước viết commit (Conventional Commits)

Dự án EvoVocab dùng **Conventional Commits**. Version (SemVer) và changelog được `nx release` tự tính từ lịch sử commit, nên commit viết đúng chuẩn là bắt buộc.

## 1. Định dạng

```
<type>(<scope>): <mô tả ngắn>

[thân commit - tùy chọn]

[footer - tùy chọn]
```

Quy tắc cho dòng đầu (header):

- Viết thường, không kết thúc bằng dấu chấm.
- Tối đa 100 ký tự.
- Dùng thể mệnh lệnh, ngắn gọn: "thêm", "sửa", "xóa", "đổi", "nâng cấp".
- **Phải có scope** (hệ thống sẽ cảnh báo nếu thiếu).

## 2. Type

| Type       | Dùng khi                              | Ảnh hưởng version |
| ---------- | ------------------------------------- | ----------------- |
| `feat`     | Thêm tính năng mới                    | MINOR             |
| `fix`      | Sửa lỗi                               | PATCH             |
| `perf`     | Cải thiện hiệu năng                   | PATCH             |
| `refactor` | Đổi cấu trúc code, không đổi hành vi  | không             |
| `docs`     | Chỉ sửa tài liệu                      | không             |
| `style`    | Format, khoảng trắng, không đổi logic | không             |
| `test`     | Thêm hoặc sửa test                    | không             |
| `build`    | Build, dependencies, Gradle, npm      | không             |
| `ci`       | Pipeline, GitHub Actions              | không             |
| `chore`    | Việc vặt khác                         | không             |
| `revert`   | Hoàn tác một commit trước đó          | không             |

Breaking change (thay đổi phá vỡ tương thích) **luôn làm tăng MAJOR**, bất kể type nào. Xem mục 4.

## 3. Scope

Scope là tên project Nx hoặc nhóm thay đổi chung:

| Scope     | Ý nghĩa                                           |
| --------- | ------------------------------------------------- |
| `web`     | Ứng dụng web                                      |
| `api`     | Backend Spring Boot                               |
| `web-e2e` | Test end-to-end của web                           |
| `ui`      | UI component dùng cho web                         |
| `deps`    | Nâng cấp hoặc thêm dependency                     |
| `release` | Việc liên quan tới phát hành                      |
| `repo`    | Cấu hình workspace (nx.json, tsconfig, eslint...) |
| `ci`      | Cấu hình CI/CD                                    |

Khi thêm project Nx mới, nhớ thêm tên nó vào `scope-enum` trong `commitlint.config.cjs`.

## 4. Breaking change

Có hai cách ghi, nên dùng cả hai để rõ ràng.

Thêm dấu `!` sau scope:

```
feat(api)!: đổi cấu trúc response của /words
```

Và/hoặc ghi footer `BREAKING CHANGE:` kèm giải thích:

```
feat(api)!: đổi tên field word thành term

BREAKING CHANGE: response của /words dùng field "term" thay cho "word".
Client cần cập nhật trước khi nâng cấp.
```

Thế nào là breaking change:

- Xóa hoặc đổi tên endpoint, field trong request/response.
- Đổi kiểu dữ liệu hoặc ý nghĩa của một field hiện có.
- Thay đổi hành vi mà client đang phụ thuộc vào.

Thêm endpoint mới hoặc thêm field tùy chọn **không** phải breaking change (đó là `feat`).

## 5. Ví dụ

Đúng:

```
feat(api): thêm endpoint lưu từ yêu thích
fix(web): sửa lỗi không hiển thị nghĩa của từ
perf(api): thêm index cho truy vấn tìm từ
refactor(web): tách component danh sách từ
docs(repo): cập nhật hướng dẫn cài đặt
test(web-e2e): thêm kịch bản đăng nhập
build(deps): nâng nx lên 22.7.12
ci(ci): thêm bước kiểm tra lint trên pull request
chore(release): 1.0.0
revert(api): hoàn tác "feat(api): thêm endpoint lưu từ yêu thích"
```

Sai (và lý do):

```
Fix bug                       -> thiếu type đúng chuẩn, viết hoa, thiếu scope
fix: sửa lỗi.                 -> thiếu scope, có dấu chấm cuối
update(api): cập nhật code    -> "update" không phải type hợp lệ
feat(backend): thêm API       -> "backend" không có trong danh sách scope
feat(api): Thêm API mới       -> mô tả viết hoa chữ đầu
```

## 6. Thói quen tốt

- **Một commit chỉ làm một việc.** Đừng trộn `feat` và `fix` trong cùng một commit.
- Mô tả nói **làm gì và vì sao**, không chỉ ghi "sửa lỗi" chung chung.
- Mỗi Pull Request dùng **squash merge**, và **tiêu đề PR phải theo đúng chuẩn này**, vì đó là commit duy nhất đi vào nhánh chính.
- Có issue liên quan thì ghi ở footer: `Closes #123`.
- Commit WIP trên nhánh cá nhân thì thoải mái, nhưng tiêu đề PR cuối cùng phải đúng chuẩn.

## 7. Kiểm tra tự động

Commit sai định dạng sẽ bị từ chối ngay trên máy bạn bởi `commitlint` (qua husky). Thử nhanh:

```bash
git commit -m "bậy bạ"                       # phải bị từ chối
git commit -m "fix(api): sửa lỗi null"        # phải qua
```

## 8. Cách version được tính

Khi chạy `nx release`, Nx đọc các commit kể từ lần release trước:

| Có commit                         | Version tăng                  |
| --------------------------------- | ----------------------------- |
| Có `BREAKING CHANGE` hoặc `!`     | MAJOR (`1.4.2` thành `2.0.0`) |
| Có `feat` (không có breaking)     | MINOR (`1.4.2` thành `1.5.0`) |
| Chỉ có `fix`, `perf`              | PATCH (`1.4.2` thành `1.4.3`) |
| Chỉ có `docs`, `test`, `chore`... | không tạo version mới         |

Lưu ý: các project trong repo cùng một nhóm `fixed`, nên một `feat` ở bất kỳ project nào cũng làm cả nhóm (`web`, `api`, `web-e2e`) tăng MINOR cùng lúc.

## 9. Tài liệu tham khảo

- Conventional Commits: https://www.conventionalcommits.org
- Semantic Versioning: https://semver.org/lang/vi/
