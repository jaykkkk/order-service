FROM eclipse-temurin:25-jdk-alpine

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw package -DskipTests

EXPOSE 8082

CMD ["java", "-jar", "target/order-service-0.0.1-SNAPSHOT.jar"]
