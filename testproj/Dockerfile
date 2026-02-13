# Этап 1: Сборка JAR-файла
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

# Этап 2: Запуск приложения
FROM apache/spark:3.5.1

WORKDIR /app

COPY --from=builder /app/target/testproj-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["/opt/spark/bin/spark-submit"]
CMD ["--master", "local[*]", "/app/app.jar"]