# Hướng dẫn đóng góp (Contributing Guidelines) cho EvoVocab Platform

Chào mừng bạn đến với dự án **EvoVocab Platform**! Chúng tôi rất trân trọng và hoan nghênh mọi đóng góp của bạn, từ việc báo cáo lỗi, cải thiện tài liệu, đến việc phát triển các tính năng mới.

Để đảm bảo quá trình làm việc nhóm diễn ra trơn tru, code base luôn sạch sẽ và dễ dàng bảo trì, vui lòng đọc kỹ các quy định và hướng dẫn dưới đây trước khi bắt đầu.

---

## 1. Quy trình đóng góp (Workflow)

Để đóng góp vào dự án, vui lòng tuân thủ quy trình sau:

1. **Cập nhật mã nguồn (Pull/Fetch):** Đảm bảo bạn đang ở nhánh `develop` và đã cập nhật code mới nhất từ server.
2. **Tạo nhánh mới (Branch):** Luôn tạo một nhánh mới từ nhánh `develop` để bắt đầu công việc. Tuân thủ nghiêm ngặt **Quy tắc đặt tên Branch** ở mục 2.
3. **Phát triển & Kiểm thử:** Viết code, thêm các bài test cần thiết và đảm bảo tất cả các test hiện tại đều chạy thành công (pass).
4. **Commit thay đổi:** Thực hiện commit thường xuyên với thông điệp (message) tuân thủ quy định của dự án.
5. **Tạo Pull Request (PR):** Đẩy nhánh của bạn lên GitHub và tạo một Pull Request. Điền đầy đủ thông tin theo mẫu (template) có sẵn.
6. **Code Review:** Đợi các thành viên khác trong team review code. Bạn có thể sẽ cần chỉnh sửa lại code theo các góp ý (nếu có) trước khi được merge.

---

## 2. Quy tắc đặt tên Nhánh (Branch Naming Conventions)

Việc đặt tên branch thống nhất là **bắt buộc** nhằm giúp mọi người dễ dàng theo dõi tiến độ và nắm bắt mục đích của từng nhánh.

Cấu trúc chung: `<loại-thay-đổi>/<mô-tả-ngắn-gọn>` (Sử dụng chữ thường và dấu gạch ngang `-` để nối các từ).

Dưới đây là các loại nhánh và cách sử dụng:

| Loại (Prefix) | Mục đích sử dụng                                                                                                          | Ví dụ                                            |
| :------------ | :------------------------------------------------------------------------------------------------------------------------ | :----------------------------------------------- |
| `feature/`    | Phát triển hoặc thêm một tính năng mới.                                                                                   | `feature/user-login`, `feature/vocabulary-list`  |
| `bugfix/`     | Sửa một lỗi (bug) đang tồn tại trên hệ thống.                                                                             | `bugfix/payment-crash`, `bugfix/typo-in-header`  |
| `perf/`       | Cải thiện hiệu suất (performance) mà không đổi logic.                                                                     | `perf/optimize-image-loading`                    |
| `refactor/`   | Tái cấu trúc mã nguồn (không thêm tính năng, không sửa lỗi).                                                              | `refactor/extract-api-service`                   |
| `test/`       | Viết thêm hoặc chỉnh sửa các bài kiểm thử (unit, e2e...).                                                                 | `test/add-login-test`, `test/edit-user-mock`     |
| `build/`      | Thay đổi liên quan đến cấu hình build, thêm/bớt thư viện.                                                                 | `build/update-webpack`, `build/axios`            |
| `ci/`         | Thiết lập, cập nhật, sửa lỗi hoặc xóa luồng CI/CD. <br>_(Bắt buộc theo sau bởi: `setup`, `update`, `fix`, hoặc `remove`)_ | `ci/setup-github-actions`, `ci/fix-docker-build` |
| `chore/`      | Các công việc bảo trì nhỏ, không làm thay đổi code production.                                                            | `chore/update-readme`, `chore/bump-dependencies` |

---

## 3. Quy tắc viết thông điệp Commit (Commit Message)

Dự án của chúng tôi áp dụng các tiêu chuẩn cụ thể đối với thông điệp Commit để đảm bảo lịch sử Git luôn rõ ràng và dễ dàng tạo Changelog.

👉 **Vui lòng đọc kỹ và tuân thủ [Quy tắc Commit của EvoVocab](./COMMIT.md) trước khi thực hiện bất kỳ commit nào.**

_(Lưu ý: Bạn có thể click vào link trên để xem chi tiết các quy định về loại commit, cấu trúc và ví dụ cụ thể)._

---

## 4. Báo cáo Lỗi (Issues) & Yêu cầu tính năng (Feature Requests)

- **Báo cáo lỗi (Bug Report):** Trước khi mở một Issue mới, vui lòng kiểm tra lại danh sách Issues hiện tại để tránh bị trùng lặp. Nếu chưa có, hãy tạo Issue và mô tả rõ: các bước để tái hiện lỗi, hệ điều hành/trình duyệt đang dùng, và đính kèm hình ảnh/log lỗi nếu có.
- **Yêu cầu tính năng (Feature Request):** Mô tả chi tiết vấn đề bạn muốn giải quyết hoặc lý do tại sao tính năng mới này lại mang lại lợi ích cho nền tảng EvoVocab.

---

## 5. Tiêu chuẩn Code (Code Standards)

- Tuân thủ cấu hình Linting và Formatting (VD: ESLint, Prettier) đã được cài đặt sẵn trong dự án. Bạn nên chạy công cụ auto-format trước khi push code.
- Xóa bỏ các đoạn `console.log()` hoặc code thừa không sử dụng trước khi tạo Pull Request.
- Cập nhật tài liệu kỹ thuật hoặc `README.md` nếu nhánh của bạn có thay đổi về mặt kiến trúc, cấu hình môi trường hoặc thêm tính năng phức tạp.

Cảm ơn bạn đã đóng góp công sức giúp **EvoVocab Platform** ngày càng phát triển! 🚀
