# src/components/customs

Thư mục `custom` nên chứa các thành phần đã được thiết kế lại có thể được sử dụng trên toàn bộ ứng dụng.

Theo nguyên tắc chung, mỗi thành phần (component) nên được đặt trong thư mục riêng, kèm theo tệp mô tả (story), kiểm thử đơn vị (unit test) và tệp chỉ mục (index file) để quản lý các thành phần được xuất ra (exports).

Ví dụ:

- src/components/ui/exmaple/
  - exmaple.tsx
  - exmaple.stories.tsx
  - exmaple.spec.tsx
  - index.ts
