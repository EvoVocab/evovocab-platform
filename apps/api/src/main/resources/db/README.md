# Hướng Dẫn Migration Cơ Sở Dữ Liệu Liquibase (SQL & Agile Workflow)

Chào mừng bạn đến với tài liệu hướng dẫn migration cơ sở dữ liệu! Chúng tôi sử dụng **Liquibase** với định dạng **SQL thuần** để quản lý thay đổi cấu trúc database.

Dự án này áp dụng mô hình phát triển **Agile**, do đó các thay đổi cơ sở dữ liệu sẽ được gom nhóm theo từng **Sprint** (hoặc đợt Release). Điều này giúp database luôn đồng bộ chính xác với các tính năng được phát hành trong mỗi chu kỳ.

Nếu bạn là người mới, **vui lòng đọc kỹ tài liệu này trước khi thực hiện bất kỳ thay đổi nào.**

---

## 1. Cấu Trúc Thư Mục (Theo Sprint)

Các kịch bản (script) migration được đặt tại `src/main/resources/db/changelog/`. Thay vì chia theo version (như v1, v2), chúng ta tổ chức các tệp theo từng Sprint.

Khi một Sprint kết thúc và được deploy, thư mục đó sẽ bị "đóng băng" (không được sửa đổi nữa).

```text
src/main/resources/db/changelog/
├── db.changelog-master-dev.yaml     # Tệp gốc dùng để include các file con (dành cho môi trường dev).
├── db.changelog-master-prod.yaml    # Tệp gốc dùng để include các file con (dành cho môi trường production).
├── db.changelog-master-staging.yaml # Tệp gốc dùng để include các file con (dành cho môi trường staging).
├── sprint-01/                       # Thư mục của Sprint 1 (Đã hoàn thành)
│   ├── 20260330-01-create-users.sql # File SQL thực tế
│   └── 20260410-tag-sprint-01.sql   # Đánh dấu (Tag) kết thúc Sprint 1
└── sprint-02/                       # Thư mục của Sprint 2 (Đang hoạt động)
    ├── 20260415-01-add-email.sql
    └── ...
```

---

## 2. Những Nguyên Tắc Vàng (Bắt Buộc Tuân Thủ)

Liquibase định danh một kịch bản migration bằng khóa hợp thành: **[ID] + [Tác giả] + [Đường dẫn tệp]**.

1. **KHÔNG BAO GIỜ sửa đổi một changeset đã được thực thi:** Đặc biệt là các file thuộc về các Sprint cũ đã release. Việc sửa file SQL cũ sẽ làm sai lệch mã kiểm tra (checksum) và gây lỗi khởi động app.
2. **Luôn viết Rollback (Hoàn tác):** Vì SQL thuần không tự động biết cách rollback như YAML (ví dụ `CREATE TABLE` không tự biết `DROP TABLE`), bạn **bắt buộc** phải tự viết câu lệnh rollback.
3. **Idempotent (Tính bất biến):** Hãy cố gắng viết các câu lệnh SQL an toàn (ví dụ: `CREATE TABLE IF NOT EXISTS`, `DROP TABLE IF EXISTS`) nếu database của bạn hỗ trợ.

---

## 3. Quy Ước Đặt Tên

- **Tên Tệp:** `[YYYYMMDD]-[sequence]-[mô-tả-ngắn-gọn].sql`
- _Ví dụ:_ `20260415-01-add-email-column.sql` (01 là script đầu tiên tạo trong ngày hôm đó).
- **ChangeSet ID:** Sử dụng chính tên tệp (bỏ đuôi `.sql`) để dễ tra cứu.
- **Tác giả (Author):** Sử dụng username của bạn (VD: `osamuharu`), không dùng `dev` hay `admin`.

---

## 4. Quy Trình Làm Việc Trong Sprint

1. **Xác định Sprint hiện tại:** Kiểm tra xem team đang làm việc ở Sprint nào (ví dụ `sprint-02`).
2. **Tạo tệp `.sql` mới** trong thư mục của Sprint đó.
3. **Khai báo header chuẩn của Liquibase** (Bắt buộc phải có dòng `-- liquibase formatted sql` ở đầu tệp).
4. **Viết kịch bản SQL** theo mẫu chuẩn (xem Phần 5).
5. **Include tệp của bạn** vào `db.changelog-master-dev.yaml` ở dòng cuối cùng (nếu dự án không dùng auto-include).
6. **Chạy ứng dụng** để Spring Boot tự động thực thi script ở local.

---

## 5. Mẫu File SQL (Formatted SQL) Hoàn Hảo

Bạn hãy copy mẫu này cho mọi file `.sql` mới. Các thẻ Liquibase trong SQL được khai báo thông qua **comment (`--`)**.

```sql
-- liquibase formatted sql

-- changeset ten_cua_ban:20260415-01-add-email-column
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_name = 'users' AND column_name = 'email';

/* 1. KỊCH BẢN CHÍNH (CHANGES): Thực thi thêm cột */
ALTER TABLE users
ADD COLUMN email VARCHAR(255) UNIQUE;

/* 2. ROLLBACK: Định nghĩa cách hoàn tác lại lệnh trên */
-- rollback ALTER TABLE users DROP COLUMN email;
```

**Giải thích các thành phần:**

- `-- liquibase formatted sql`: BẮT BUỘC. Báo cho Liquibase biết đây là file chứa các changeset.
- `-- changeset {author}:{id}`: Khai báo tác giả và ID (ID nên lấy theo tên file).
- `-- preconditions...` và `-- precondition-sql-check`: Dùng SQL để kiểm tra điều kiện trước khi chạy, tránh lỗi "Cột/Bảng đã tồn tại".
- `-- rollback {câu lệnh sql}`: BẮT BUỘC để có thể hạ cấp (downgrade) database khi cần.

---

## 6. Khắc Phục Các Lỗi Thường Gặp

| Thông Báo Lỗi                                             | Nguyên Nhân                                                         | Cách Khắc Phục                                                                                                                                      |
| :-------------------------------------------------------- | :------------------------------------------------------------------ | :-------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`ValidationFailedException`** <br>_(Sai lệch Checksum)_ | Bạn đã sửa đổi một tệp `.sql` SAU KHI nó đã được chạy vào database. | **Không sửa file cũ.** Khôi phục tệp về trạng thái ban đầu. Tạo file `.sql` _mới_ trong Sprint hiện tại để chạy lệnh sửa đổi (VD: `ALTER`, `DROP`). |
| **`LockException`** <br>_(Bị khóa database)_              | Ứng dụng bị lỗi hoặc dừng đột ngột lúc Liquibase đang chạy SQL.     | Chạy lệnh trong SQL client: <br>`UPDATE databasechangeloglock SET locked = false;`                                                                  |
| **`Relation/Table already exists`**                       | Chạy script tạo bảng mà bảng đã có sẵn trong DB.                    | Dùng `CREATE TABLE IF NOT EXISTS` hoặc thêm `-- preconditions` để kiểm tra trước khi chạy.                                                          |
