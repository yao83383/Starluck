# ✨ Starluck Server · 运行时 Dockerfile
# 构建前先用 Maven 本地打 JAR：mvn clean package -DskipTests
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/starluck-server-1.0.0.jar app.jar
RUN mkdir -p /data/uploads
EXPOSE 8080
ENV JAVA_OPTS="-Xms256m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8"
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
