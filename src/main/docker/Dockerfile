# Start from a base image that provides lightweight setup
FROM amazoncorretto:21.0.7

WORKDIR /app
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-Xmx2048M", "-jar", "application.jar"]