# Use Maven to build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Use JDK to run
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar ./ecommerce.jar
EXPOSE 8090
ENV SPRING_PROFILES_ACTIVE=prod
CMD ["java", "-jar", "./ecommerce.jar"]
