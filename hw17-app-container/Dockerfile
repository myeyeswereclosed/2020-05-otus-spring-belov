FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:11-jdk-slim
COPY --from=build /home/app/target/book-info-app.jar /app/book_info.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/book_info.jar"]
