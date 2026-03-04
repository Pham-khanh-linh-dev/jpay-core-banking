# --- GIAI ĐOẠN 1: MƯỢN MÁY CÓ MAVEN ĐỂ ĐÓNG GÓI CODE ---
# Dùng ảnh chứa sẵn Java 21 và Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# Copy file cấu hình thư viện và source code vào
COPY pom.xml .
COPY src ./src
# Ra lệnh Maven đóng gói ra file .jar (Bỏ qua chạy test để build nhanh hơn)
RUN mvn clean package -DskipTests

# --- GIAI ĐOẠN 2: CHỈ LẤY FILE .JAR ĐỂ CHẠY (GIẢM NHẸ DUNG LƯỢNG) ---
# Lúc chạy thực tế chỉ cần JRE (Môi trường chạy), không cần Maven nữa
FROM eclipse-temurin:21-jre
WORKDIR /app
# Lấy file .jar từ Giai đoạn 1 ném sang đây
COPY --from=build /app/target/*.jar app.jar
# Mở cổng 8080 cho bên ngoài gọi vào
EXPOSE 8080
# Lệnh sẽ chạy khi khởi động Container
ENTRYPOINT ["java", "-jar", "app.jar"]