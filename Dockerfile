# Start from a base image that provides lightweight setup
FROM amazoncorretto:21.0.7

WORKDIR /app
COPY ./build/libs/CustomerBookingService-*SNAPSHOT.jar ./application.jar
ENTRYPOINT ["java", "-Xmx2048M", "-jar", "application.jar"]