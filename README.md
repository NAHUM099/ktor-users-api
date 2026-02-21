# Ktor REST API

REST API built with **Kotlin** and **Ktor**, implementing JWT authentication, role-based authorization, PostgreSQL integration.

---

## Overview

This project is a backend service developed with Ktor. It includes secure authentication, database integration, and is structured for development.

---

## Tech Stack

- **Kotlin**
- **Ktor**
- **PostgreSQL**
- **Exposed ORM**
- **HikariCP**
- **JWT Authentication**
- **Koin (Dependency Injection)**
- **Gradle (KTS)**

---

##  Features

- User registration & login
- JWT-based authentication
- Role-based authorization
- Products & Categories management
- Secure password hashing (BCrypt)
- Environment-based configuration (DEV / PROD)
- Fat JAR build for deployment


---

##  Project Structure

src/main/kotlin

│

├── config/ # Database & environment configuration

├── plugins/ # Ktor configuration plugins

├── routes/ # API endpoints

├── tables/ # Database table definitions

├── models/ # Data models

└── Application.kt # Entry point