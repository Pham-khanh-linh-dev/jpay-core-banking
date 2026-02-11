JPay Core Banking - E-Wallet Backend System

Hệ thống Backend Ví điện tử mô phỏng nghiệp vụ Core Banking, được xây dựng với kiến trúc chuẩn Enterprise, tập trung vào Bảo mật và Toàn vẹn dữ liệu giao dịch.

## Công nghệ sử dụng
* **Ngôn ngữ & Framework:** Java 21, Spring Boot 3
* **Database & ORM:** MySQL 8, Spring Data JPA, Hibernate
* **Security:** Spring Security 6, JWT (Nimbus JOSE), BCrypt
* **Công cụ khác:** MapStruct, Lombok, Validation, Docker & Docker Compose

## Tính năng nổi bật
1. **Identity & Security:**
   - Đăng ký / Đăng nhập sinh mã JWT (HMAC-SHA256).
   - Mã hóa mật khẩu an toàn với BCrypt.
   - Phân quyền và bảo vệ API với OAuth2 Resource Server.
   - Xử lý lỗi tập trung (`GlobalExceptionHandler`) và chuẩn hóa API Response.
2. **Core Transaction:**
   - Tự động cấp phát Ví (Wallet) khi khởi tạo User (Quan hệ One-to-One).
   - Xử lý nghiệp vụ Nạp tiền (Deposit), Rút tiền (Withdraw).
   - **Đặc biệt:** Nghiệp vụ Chuyển tiền (Transfer) áp dụng **ACID Transaction** (`@Transactional`) và **Pessimistic Row Locking** để ngăn chặn triệt để lỗi Deadlock và Double-spending trong môi trường đa luồng (Concurrency).

## 🛠️ Hướng dẫn cài đặt & Chạy dự án
1. Clone dự án về máy.
2. Chạy Database MySQL qua Docker: `docker-compose up -d`
3. Chạy ứng dụng Spring Boot. Hệ thống tự động tạo bảng (Hibernate ddl-auto).
