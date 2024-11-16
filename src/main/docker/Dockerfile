FROM openjdk:21
LABEL authors="egormalov"

VOLUME /tmp
WORKDIR /app
ADD maven/user-service-0.0.1-SNAPSHOT.jar /app/userservice.jar
EXPOSE 7070
ENTRYPOINT ["java", "-jar", "userservice.jar"]