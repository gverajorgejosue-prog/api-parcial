# ============================================================
# Etapa 1 — BUILD: compila el proyecto y genera el JAR ejecutable
# ============================================================
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# El mvnw generado en Windows tiene fin de línea CRLF: se normaliza a LF
COPY mvnw .
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw
COPY .mvn .mvn
COPY pom.xml .

# Descarga de dependencias primero (aprovecha el caché de capas de Docker)
RUN ./mvnw dependency:go-offline -B

COPY src src
RUN ./mvnw package -DskipTests -B

# ============================================================
# Etapa 2 — RUNTIME: imagen final mínima con el JRE
# ============================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Ejecuta la aplicación como un usuario sin privilegios
RUN addgroup -S app && adduser -S app -G app && chown app:app app.jar
USER app

EXPOSE 8080

# La configuración (URL de BD, usuario, contraseña) llega por variables de entorno
ENTRYPOINT ["java", "-jar", "app.jar"]
