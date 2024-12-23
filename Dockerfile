# Используем базовый образ Maven для сборки проекта
FROM maven:3.9.2-eclipse-temurin-17 AS build

# Установим клиент PostgreSQL
RUN apt-get update && apt-get install -y postgresql-client

# Устанавливаем рабочую директорию для сборки
WORKDIR /app

# Копируем только pom.xml и скачиваем зависимости для кеширования
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Копируем остальные файлы проекта
COPY src ./src

# Выполняем сборку проекта
RUN mvn clean package -DskipTests

# Проверяем наличие скомпилированного JAR-файла
RUN ls -l /app/target

# Используем минимальный образ Java для запуска приложения
FROM eclipse-temurin:17-jdk-alpine

# Устанавливаем рабочую директорию для приложения
WORKDIR /app

# Копируем собранный JAR-файл из предыдущего этапа
COPY --from=build /app/target/*.jar app.jar

# Проверяем наличие JAR-файла в образе
RUN ls -l /app

# Указываем команду запуска приложения
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
