FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/*.jar app.jar

RUN mkdir -p /data/uploads

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
