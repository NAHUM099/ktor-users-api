# Usamos la imagen oficial de Gradle + JDK 17
FROM gradle:8.3.1-jdk17 AS build

# Directorio de trabajo en contenedor
WORKDIR /app

# Copiamos los archivos necesarios para construir la app
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle ./gradle
COPY src ./src

# Construimos la app, sin tests
RUN gradle clean build -x test -x check --no-daemon -Pproduction

# Stage final
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiamos el jar de la etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

# Puerto expuesto
EXPOSE 8080

# Comando para correr la app
CMD ["java", "-jar", "app.jar"]