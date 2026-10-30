## Tổng quan workspace

README và workflow deploy đều dùng tên rút gọn; nếu Nx báo không tìm thấy project thì dùng tên đầy đủ.

| Tên rút gọn | Tên đầy đủ          | Loại     | Công nghệ                         | Cổng                      |
| ----------- | ------------------- | -------- | --------------------------------- | ------------------------- |
| `web`       | `@evovocab/web`     | App      | React 19, Vite, TanStack Router   | dev 4200, preview 4300    |
| `api`       | `@evovocab/api`     | App      | Spring Boot, Gradle               | 5000 (Docker Compose)     |
| `web-e2e`   | `@evovocab/web-e2e` | E2E      | Playwright                        | dùng lại server của `web` |
| `ui`        | `@evovocab/ui`      | Thư viện | Shadcn, Tailwind, Storybook       | —                         |
| `store`     | `@evovocab/store`   | Thư viện | Thư viện quản lý state dùng chung | —                         |

Các target (`build`, `test`, `lint`, `serve`...) hầu hết do plugin trong `nx.json` suy ra từ file cấu hình có sẵn (`vite.config.mts`, `build.gradle`, `playwright.config.mts`, `.storybook`). Riêng `api` khai báo thêm trong `apps/api/project.json`.

## Cài đặt và chạy hằng ngày

Ngày làm việc thường bắt đầu bằng ba bước: bật MySQL, chạy API, chạy web. Lần đầu cần thêm `npm i` và `npm run setup`.

| Lệnh                                               | Tác dụng                                                                                                                      |
| -------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------- |
| `npm i`                                            | Cài dependency. CI dùng `npm ci --legacy-peer-deps`; nếu máy bạn dính lỗi peer dependency thì thêm cờ này                     |
| `npm run setup`                                    | Sao chép `.env.example` thành `.env` cho `apps/web` và `apps/api`, và `docker-compose.example.yml` thành `docker-compose.yml` |
| `npm run db:docker:up`                             | Chỉ bật MySQL (`mysql-db`, cổng 3306) bằng Docker                                                                             |
| `npx nx serve api`                                 | Chạy API trên máy với profile `dev` (cần Java 21+ và MySQL đang chạy)                                                         |
| `npx nx serve web`                                 | Chạy web tại `http://localhost:4200`. Target `dev` cũng có và cho kết quả tương tự: `npx nx dev web`                          |
| `npx nx run-many -t serve -p web api`              | Chạy web và API cùng lúc trong một terminal                                                                                   |
| `npm run docker:up`                                | Build và chạy cả API lẫn MySQL trong Docker (API ở cổng 5000)                                                                 |
| `npm run docker:down`                              | Dừng và xóa các container của Docker Compose                                                                                  |
| `docker compose -f docker-compose.yml logs -f api` | Xem log API khi chạy bằng Docker                                                                                              |
| `npx nx preview web`                               | Chạy bản build của web tại cổng 4300 để kiểm tra giống production                                                             |

## Build, test, lint, typecheck

Mỗi lệnh có dạng `npx nx <target> <project>`, hoặc `npx nx run-many -t <target>` để chạy cho mọi project. Vì `nx.json` đặt `test` phụ thuộc `^build`, chạy test sẽ build trước các thư viện mà project đó dùng (`ui`, `store`).

| Lệnh                                 | Tác dụng                                                                               |
| ------------------------------------ | -------------------------------------------------------------------------------------- |
| `npx nx build web`                   | Build web ra `apps/web/dist` (thư mục mà workflow deploy đưa lên GitHub Pages)         |
| `npx nx build ui`                    | Build thư viện `ui`. Thư viện `store` làm tương tự                                     |
| `npx nx build api`                   | Build Spring Boot bằng Gradle                                                          |
| `npx nx test web`                    | Chạy unit test bằng Vitest (jsdom). Cấu hình `watch: false` nên chạy một lần rồi thoát |
| `npx nx test web --watch`            | Chạy Vitest ở chế độ watch khi đang viết test                                          |
| `npx nx test web --coverage`         | Báo cáo coverage (v8), ghi vào `apps/web/test-output/vitest/coverage`                  |
| `npx nx lint web`                    | ESLint cho `web`. Thêm `--fix` để tự sửa                                               |
| `npx nx typecheck web`               | Kiểm tra kiểu TypeScript cho một project                                               |
| `npm run typecheck`                  | Kiểm tra kiểu cho mọi project (`nx run-many --target=typecheck --all`)                 |
| `npx nx format:check`                | Kiểm tra định dạng Prettier toàn repo (có Java và sắp xếp class Tailwind)              |
| `npx nx format:write`                | Tự định dạng lại toàn repo. Thêm `--uncommitted` để chỉ định dạng file đang thay đổi   |
| `npm run analyst`                    | Chạy Knip để tìm file, export và dependency không dùng đến                             |
| `npx nx run-many -t lint test build` | Chạy ba target cho mọi project. Thêm `-p web ui` để giới hạn project                   |

## Chỉ chạy phần bị ảnh hưởng (affected)

`affected` chỉ chạy các project bị thay đổi so với nhánh gốc, và cả CI lẫn git hooks của repo đều dùng nó. `nx.json` không đặt `defaultBase`, nên Nx mặc định so với `main`; khi làm việc trên nhánh `dev` hãy thêm `--base=origin/dev`.

| Lệnh                                                                 | Tác dụng                                                                                            |
| -------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------- |
| `npx nx affected -t lint`                                            | Lệnh mà hook pre-commit chạy                                                                        |
| `npx nx affected -t test`                                            | Lệnh mà hook pre-push chạy                                                                          |
| `npx nx affected -t lint test build`                                 | Chạy lại ba bước của CI (lint, test, build) trên máy trước khi mở PR                                |
| `npx nx affected -t build --base=origin/main --head=HEAD`            | Chỉ định rõ mốc so sánh                                                                             |
| `npx nx affected -t test --uncommitted`                              | Chỉ tính các thay đổi chưa commit                                                                   |
| `npx nx show projects --affected`                                    | Liệt kê project bị ảnh hưởng. Workflow deploy dùng lệnh này để quyết định có deploy `web` hay `api` |
| `NX_DAEMON=false NX_NO_CLOUD=true npx nx affected -t lint --verbose` | Chạy bước lint giống hệt cách CI chạy                                                               |

Lưu ý: `sharedGlobals` trong `nx.json` trỏ tới `.github/workflows/ci.yml`. Sửa file này sẽ làm mọi project bị coi là bị ảnh hưởng, nên `affected` sẽ chạy lại toàn bộ.

## Backend Spring Boot (api)

`api` chạy bằng Gradle qua plugin `@nxrocks/nx-spring-boot`. Ba target `serve`, `build` và `test` được khai báo trong `apps/api/project.json`, mỗi target gắn với một profile Spring; chọn profile bằng cờ `-c` (configuration).

| Lệnh                          | Profile Spring                                           |
| ----------------------------- | -------------------------------------------------------- |
| `npx nx serve api`            | `dev` (mặc định)                                         |
| `npx nx serve api -c staging` | `staging`                                                |
| `npx nx serve api -c prod`    | `prod`                                                   |
| `npx nx build api`            | Không ép profile                                         |
| `npx nx build api -c prod`    | `prod`. Các configuration khác: `dev`, `staging`, `test` |
| `npx nx test api`             | `test`                                                   |

Plugin còn khai báo thêm các target `install`, `clean`, `run`, `build-image`, `build-info`, `format`, `apply-format` và `check-format` trong `nx.json`. Ví dụ:

- `npx nx run api:clean` xóa thư mục build của Gradle.
- `npx nx run api:build-image` tạo Docker image bằng Spring Boot.
- `npx nx run api:check-format` và `npx nx run api:apply-format` kiểm tra và áp dụng định dạng Java.

Dùng dạng `nx run api:format`, không dùng `nx format api`, vì `nx format` là lệnh có sẵn của Nx (Prettier) chứ không phải target của `api`.

## E2E (Playwright) và Storybook

E2E chạy trên 8 cấu hình thiết bị: Google Chrome, Firefox, Safari, iPad Pro 11 (dọc và ngang), iPhone 14, iPhone 13 Mini và Galaxy S22. Lần đầu cần tải trình duyệt bằng `npx playwright install`.

| Lệnh                                              | Tác dụng                                                                                                    |
| ------------------------------------------------- | ----------------------------------------------------------------------------------------------------------- |
| `npx nx e2e web-e2e`                              | Chạy toàn bộ E2E. Playwright tự bật `nx run @evovocab/web:dev` ở cổng 4200, hoặc dùng luôn server đang chạy |
| `npx nx e2e web-e2e -- --project="Google Chrome"` | Chỉ chạy trên một cấu hình, nhanh hơn khi đang viết test                                                    |
| `npx nx e2e web-e2e -- --ui`                      | Mở giao diện Playwright để chạy và gỡ lỗi từng test. Thêm `--headed` để thấy trình duyệt                    |
| `npx nx e2e-ci web-e2e`                           | Target dành cho CI. Khi có biến `CI`, Playwright dùng `preview` ở cổng 4300 nên cần có bản build của `web`  |
| `npx nx storybook ui`                             | Chạy Storybook xem các component trong `libs/ui`                                                            |
| `npx nx build-storybook ui`                       | Build Storybook thành bản tĩnh                                                                              |
| `npx nx static-storybook ui`                      | Chạy bản Storybook tĩnh đã build                                                                            |
| `npx nx test-storybook ui`                        | Chạy test của Storybook                                                                                     |

CI hiện chỉ chạy lint, test và build, chưa chạy E2E; hãy tự chạy `e2e` trước khi merge các thay đổi lớn ở giao diện.

## Khám phá workspace, cache và xử lý sự cố

Khi không chắc một project có target nào, hãy hỏi Nx thay vì đoán: `npx nx show project web` in ra danh sách target thật, input, output và `dependsOn`.

| Lệnh                               | Tác dụng                                                             |
| ---------------------------------- | -------------------------------------------------------------------- |
| `npx nx show projects`             | Liệt kê mọi project trong workspace                                  |
| `npx nx show project api`          | Xem target và cấu hình thật của một project                          |
| `npx nx graph`                     | Mở đồ thị phụ thuộc giữa các project trên trình duyệt                |
| `npx nx graph --affected`          | Đồ thị chỉ gồm phần bị ảnh hưởng bởi thay đổi hiện tại               |
| `npx nx list`                      | Liệt kê plugin Nx đã cài                                             |
| `npx nx build web --skip-nx-cache` | Chạy lại mà bỏ qua cache                                             |
| `npx nx build web --verbose`       | In log chi tiết khi lệnh lỗi khó hiểu                                |
| `npx nx reset`                     | Xóa cache và dừng daemon. Dùng khi Nx cho kết quả cũ hoặc hành vi lạ |
| `NX_DAEMON=false npx nx build web` | Chạy không qua daemon (CI cũng đặt như vậy ở bước lint)              |

## Commit, git hooks và release

Mỗi lần commit hoặc push đều có hook chạy Nx, và `nx release` tính version trực tiếp từ nội dung commit, nên commit phải đúng chuẩn Conventional Commits.

| Hook         | Chạy gì                                                    |
| ------------ | ---------------------------------------------------------- |
| `pre-commit` | `nx format:write --uncommitted`, rồi `nx affected -t lint` |
| `pre-push`   | `nx affected -t test`                                      |
| `commit-msg` | `commitlint`: kiểm tra định dạng message                   |

Định dạng message là `<type>(<scope>): <mô tả>`, ví dụ `feat(web): thêm trang ôn tập từ vựng`. Header viết thường, không có dấu chấm cuối, tối đa 100 ký tự, và bắt buộc có scope.

- Type hợp lệ: `feat`, `fix`, `perf`, `refactor`, `docs`, `style`, `test`, `build`, `ci`, `chore`, `revert`.
- Scope hợp lệ: `web`, `api`, `web-e2e`, `ui`, `deps`, `release`, `repo`, `ci`.
- Quy ước đầy đủ nằm trong `docs/COMMIT.md`.

| Lệnh                                              | Tác dụng                                                                          |
| ------------------------------------------------- | --------------------------------------------------------------------------------- |
| `npx nx format:check && npx nx affected -t lint`  | Tự chạy các bước của pre-commit trước khi commit                                  |
| `echo "feat(web): thêm từ mới" \| npx commitlint` | Thử một commit message có hợp lệ không                                            |
| `git commit --no-verify`                          | Bỏ qua hook trong trường hợp khẩn cấp. CI vẫn kiểm tra lại                        |
| `npx nx release --dry-run`                        | Xem trước version, changelog, commit và tag sẽ được tạo. Luôn chạy bước này trước |
| `npx nx release`                                  | Tính version từ lịch sử commit, cập nhật `CHANGELOG.md`, tạo commit và tag git    |
| `npx nx release --first-release`                  | Dùng cho lần release đầu tiên, khi repo chưa có tag phát hành                     |

Cấu hình `release` dùng `projectsRelationship: fixed` và `projects: ["*"]`, nên mọi project chung một version: một `feat` ở đâu cũng làm cả nhóm tăng MINOR. `nx.json` chỉ bật `commit` và `tag`, không có `push`; sau khi release hãy kiểm tra `git log` rồi mới `git push --follow-tags`.

## Generators và nâng cấp Nx

Generator tạo code theo mẫu của Nx; hãy luôn thêm `--dry-run` để xem trước các file sẽ được tạo. `nx.json` đã đặt sẵn mặc định cho `@nx/react`: ứng dụng dùng Vite, ESLint và CSS; thư viện mới dùng ESLint, CSS và không có test runner (`unitTestRunner: none`).

| Lệnh                                                | Tác dụng                                                                            |
| --------------------------------------------------- | ----------------------------------------------------------------------------------- |
| `npx nx list @nx/react`                             | Liệt kê các generator của `@nx/react`                                               |
| `npx nx g @nx/react:library libs/ten-lib --dry-run` | Xem trước việc tạo thư viện React mới                                               |
| `npx nx g @nx/react:component --help`               | Xem các tùy chọn của generator component                                            |
| `npx nx migrate latest`                             | Bước 1 khi nâng cấp: cập nhật version trong `package.json` và tạo `migrations.json` |
| `npm install`                                       | Bước 2: cài các version mới                                                         |
| `npx nx migrate --run-migrations`                   | Bước 3: chạy các migration. Xong thì xóa `migrations.json`                          |

Gói `nx` và các gói `@nx/*` đang cùng dùng version 22.7.12, nên hãy nâng đồng bộ bằng `nx migrate` thay vì sửa tay từng gói. Repo đã có sẵn `migrations.json` ở thư mục gốc; kiểm tra các migration trong đó đã chạy xong chưa trước khi bắt đầu đợt nâng cấp mới.

Khi thêm project Nx mới, thêm tên project vào `scope-enum` trong `commitlint.config.cjs` để commit có scope đó không bị từ chối.

## Điểm cần lưu ý trong cấu hình

Bốn chỗ trong repo đáng xử lý hoặc cần biết khi dùng các lệnh ở trên.

- **Thiếu scope `store`.** `libs/store` là project thứ năm, nhưng `commitlint.config.cjs` và `docs/COMMIT.md` chưa có scope `store`, nên commit dạng `feat(store): ...` sẽ bị commitlint từ chối. README cũng chưa liệt kê thư viện này.
- **README sai tên script.** README hướng dẫn `npm run api:docker:down`, nhưng `package.json` chỉ có `docker:down`.
- **Hai khóa lạ trong `nx.json`.** `"name": "ui"` và `"sourceRoot": "libs/ui/src"` nằm ở cấp gốc. Hai khóa này thuộc về cấu hình project chứ không phải `nx.json`, có vẻ do generator ghi nhầm. Có thể xóa, rồi chạy `npx nx show projects` để chắc chắn vẫn đủ 5 project.
- **Hai phiên bản Node khác nhau.** README khuyến nghị Node từ 22.19.0, còn CI dùng 24.21.0. Khi gặp lỗi chỉ xảy ra trên máy hoặc chỉ trên CI, thử dùng cùng phiên bản với CI.

Các lệnh trong tài liệu này được suy ra từ `package.json`, `nx.json`, `apps/api/project.json`, các file cấu hình và workflow; chưa chạy `nx` trên repo. Với các target do plugin tự suy ra (ví dụ các target Gradle của `api`), hãy đối chiếu bằng `npx nx show project api`.
