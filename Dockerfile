# ========================
# Stage 1: Build
# ========================
FROM gradle:8.14-jdk17-ubi AS builder

# Directorio de trabajo en el contenedor
WORKDIR /home/gradle/project

# Copiar archivos de Gradle y descargar dependencias
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle ./gradle

# Pre-cache de dependencias
RUN gradle build -x test -x check --no-daemon || true

# Copiar el resto del proyecto
COPY . .

# Construir el JAR de producción
RUN gradle clean build -x test -x check --no-daemon

# ========================
# Stage 2: Runtime
# ========================
FROM eclipse-temurin:17-jdk-focal AS runtime

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copiar JAR desde el stage de build
COPY --from=builder /home/gradle/project/build/libs/ktorDB-all.jar app.jar

# Puerto expuesto
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]