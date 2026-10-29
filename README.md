<div align="center">
  <img src="./apps/web/public/brand-icon.svg" alt="EvoVocab Logo" height=96/>
  <img src="./apps/web/public/brand-name.svg" alt="EvoVocab Logo" height=80/>
  <p><em>Evocab là một nền tảng cá nhân hóa việc học từ vựng tiếng Anh, giúp người dùng nâng cao vốn từ vựng một cách hiệu quả và thú vị.</em></p>

  <p>
    <a href="https://EvoVocab.github.io/evovocab-platform">
      <img src="https://img.shields.io/badge/Demo-evovocab_platform-orange?style=flat-square" alt="Live Demo" />
    </a>
    <a href="https://github.com/EvoVocab/evovocab-platform/blob/main/LICENSE">
      <img src="https://img.shields.io/badge/License-Apache_2.0-blue.svg?style=flat-square" alt="License: Apache 2.0" />
    </a>
    <img src="https://img.shields.io/badge/version-0.1.0-brightgreen.svg?style=flat-square" alt="Version 0.1.0" />
    <a href="https://nx.dev">
      <img src="https://img.shields.io/badge/Powered_by-Nx-143055?style=flat-square&logo=nx&logoColor=white" alt="Powered by Nx" />
    </a>
  </p>
</div>

<hr />

## 📖 Mục lục

- [Giới thiệu](#-giới-thiệu)
- [Kiến trúc dự án (Monorepo)](#-kiến-trúc-dự-án-monorepo)
- [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
- [Yêu cầu hệ thống](#-yêu-cầu-hệ-thống)
- [Hướng dẫn cài đặt & Chạy dự án](#-hướng-dẫn-cài-đặt--chạy-dự-án)
- [Các câu lệnh (Scripts)](#-các-câu-lệnh-scripts)
- [Thành viên đóng góp](#-thành-viên-đóng-góp)
- [Giấy phép](#-giấy-phép)

---

## 🚀 Giới thiệu

**EvoVocab** được xây dựng nhằm mang đến một trải nghiệm học từ vựng tiếng Anh hiện đại, áp dụng các phương pháp học tập được cá nhân hóa cho từng người dùng. Nền tảng bao gồm cả hệ thống API mạnh mẽ ở Backend và giao diện tương tác mượt mà ở Frontend.

---

## 🏗 Kiến trúc dự án (Monorepo)

Dự án được quản lý dưới dạng Monorepo sử dụng [Nx](https://nx.dev), giúp chia sẻ code hiệu quả giữa các ứng dụng và thư viện.

```text
evovocab-platform/
├── apps/
│   ├── api/            # Backend API (Spring Boot Application)
│   ├── web/            # Frontend Web App (React + Vite)
│   └── web-e2e/        # End-to-End Tests cho Web (Playwright)
├── libs/
│   └── ui/             # Thư viện UI Component dùng chung (Shadcn/Tailwind)
├── package.json
└── nx.json             # Cấu hình Nx Workspace
```

---

## 🛠 Công nghệ sử dụng

### Frontend (Web)

- ![React](https://img.shields.io/badge/react-19.0.0-%2320232a.svg?style=flat-square&logo=react&logoColor=%2361DAFB) - Thư viện giao diện người dùng
- ![Vite](https://img.shields.io/badge/vite-%23646CFF.svg?style=flat-square&logo=vite&logoColor=white) - Build tool siêu tốc
- ![TypeScript](https://img.shields.io/badge/typescript-%23007ACC.svg?style=flat-square&logo=typescript&logoColor=white) - Ngôn ngữ lập trình chính
- ![TailwindCSS](https://img.shields.io/badge/tailwindcss_v4-%2338B2AC.svg?style=flat-square&logo=tailwind-css&logoColor=white) - CSS Framework
- ![TanStack Router](https://img.shields.io/badge/TanStack_Router-FF4154?style=flat-square&logo=react-router&logoColor=white) - Quản lý Routing
- ![TanStack Query](https://img.shields.io/badge/TanStack_Query-FF4154?style=flat-square&logo=react-query&logoColor=white) - Data fetching & State management
- ![Zustand](https://img.shields.io/badge/Zustand-4A4A55?style=flat-square) - Quản lý State cục bộ
- **UI/UX:** [Shadcn UI](https://ui.shadcn.com/), [Base UI](https://base-ui.com/), [Nivo](https://nivo.rocks/) (Biểu đồ)

### Backend (API)

- ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=spring-boot&logoColor=white) - Framework Backend Java (quản lý qua plugin `@nxrocks/nx-spring-boot`)
- ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=flat-square&logo=docker&logoColor=white) - Containerization (Docker Compose)

### Testing & Tools

- ![Vitest](https://img.shields.io/badge/-Vitest-729B1B?style=flat-square&logo=vitest&logoColor=white) - Unit Testing
- ![Playwright](https://img.shields.io/badge/-Playwright-2EAD33?style=flat-square&logo=playwright&logoColor=white) - E2E Testing
- ![Storybook](https://img.shields.io/badge/-Storybook-FF4785?style=flat-square&logo=storybook&logoColor=white) - UI Component Explorer
- ![ESLint](https://img.shields.io/badge/ESLint-4B32C3?style=flat-square&logo=eslint&logoColor=white) & ![Prettier](https://img.shields.io/badge/Prettier-F7B93E?style=flat-square&logo=prettier&logoColor=black) - Code Quality
- ![Husky](https://img.shields.io/badge/Husky-Git_Hooks-blue?style=flat-square) - Quản lý Git hooks & Commitlint

---

## 💻 Yêu cầu hệ thống

Trước khi bắt đầu, hãy đảm bảo máy tính của bạn đã cài đặt các công cụ sau:

- [Node.js](https://nodejs.org/) (Phiên bản >= 22.19.0 khuyến nghị)
- [npm](https://www.npmjs.com/) hoặc [yarn](https://yarnpkg.com/)
- [Java JDK](https://adoptium.net/) (Khuyến nghị phiên bản 21+ cho Spring Boot)
- [Docker](https://www.docker.com/) & Docker Compose (Dành cho việc chạy API/Database nội bộ)

---

## ⚙️ Hướng dẫn cài đặt & Chạy dự án

**1. Clone kho lưu trữ**

```bash
git clone https://github.com/EvoVocab/evovocab-platform.git
cd evovocab-platform
```

**2. Khởi tạo và cài đặt Dependencies**
Chạy lệnh setup có sẵn để tự động tải các gói phụ thuộc và sao chép các file `.env.example` thành `.env` cho cả `apps/web` và `apps/api`.

```bash
npm i
npm run setup
```

**3. Khởi chạy Backend (API) bằng Docker**
Dự án cung cấp sẵn Docker Compose để dễ dàng dựng môi trường API:

```bash
npm run docker:up (Chạy đầy đủ dự án từ api tới database bằng docker)
npm run db:docker:up (Chỉ chạy database bằng docker)
nx serve api (Chỉ chạy api bằng môi trường trên hệ thống đang chạy)
```

_(Để tắt các container, hãy chạy: `npm run api:docker:down`)_

**4. Khởi chạy Frontend (Web)**
Sử dụng công cụ Nx CLI để khởi chạy ứng dụng web tại môi trường phát triển cục bộ:

```bash
nx serve web
```

Truy cập ứng dụng tại địa chỉ: `http://localhost:4200` (hoặc port được hiển thị trên console).

---

## 📜 Các câu lệnh (Scripts)

Dưới đây là một số script tiện ích được cấu hình sẵn trong `package.json`:

| Script                    | Lệnh thực thi                                        | Mô tả                                                    |
| :------------------------ | :--------------------------------------------------- | :------------------------------------------------------- |
| `npm run setup`           | `npm i & shx cp ...`                                 | Cài đặt dependencies và chuẩn bị file `.env` cơ bản      |
| `npm run setup:deploy`    | `bash ./scripts/setup_deploy.sh`                     | Chạy script setup môi trường cho việc deploy             |
| `npm run api:docker:up`   | `docker compose -f docker-compose.yml up -d --build` | Build và khởi chạy các container cho Backend bằng Docker |
| `npm run api:docker:down` | `docker compose -f docker-compose.yml down`          | Tắt và xóa các container Backend đang chạy               |
| `npm run prepare`         | `husky`                                              | Kích hoạt Git hooks tự động cho môi trường dev           |

**Các lệnh Nx hữu ích khác:**

- `npx nx build web` - Build dự án frontend để chuẩn bị deploy
- `npx nx test web` - Chạy Unit Test cho ứng dụng Web bằng Vitest
- `npx nx e2e web-e2e` - Chạy kiểm thử End-to-End bằng Playwright
- `npx nx storybook ui` - Chạy Storybook để xem trực quan các UI component

---

## 👥 Thành viên đóng góp

Cảm ơn tất cả các thành viên đã và đang đóng góp để xây dựng nền tảng **EvoVocab**:

|                                        Avatar                                        | Họ và Tên         | Vai trò   | Liên kết Github                                |
| :----------------------------------------------------------------------------------: | :---------------- | :-------- | :--------------------------------------------- |
| <img src="https://github.com/VanManhPhan.png" width="40" style="border-radius:50%"/> | **Phan Văn Mạnh** | Fullstack | [@VanManhPhan](https://github.com/VanManhPhan) |
|  <img src="https://github.com/osamuharu.png" width="40" style="border-radius:50%"/>  | **Huỳnh Nam Kha** | Fullstack | [@osamuharu](https://github.com/osamuharu)     |

_(Lưu ý: Bạn hãy thay thế nội dung trong ngoặc vuông bằng thông tin thực tế của đội ngũ)_

---

## 📄 Giấy phép

Dự án này được cấp phép theo giấy phép **Apache-2.0**. Vui lòng xem tệp [LICENSE](LICENSE) để biết thêm thông tin chi tiết.

<p align="center">
  <i>Được xây dựng với 💖 bởi đội ngũ EvoVocab</i>
</p>
