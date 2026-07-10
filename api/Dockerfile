# Etapa 1: Construcción (Maven)
FROM maven:3.9.6-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (JRE liviano y seguro)
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
# Crear un usuario no-root por seguridad (cambiado a nobody para alinearse estrictamente al encargo) [cite: 62]
RUN addgroup -S techgroup && adduser -S techuser -G techgroup
COPY --from=build /app/target/api-0.0.1-SNAPSHOT.jar app.jar
# Cambiar permisos al archivo
RUN chown techuser:techgroup app.jar
USER techuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=8080"]