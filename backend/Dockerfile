FROM openjdk:11.0.6-jre-slim

ENV PORT=8080

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://database:5432/postgres

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=${PORT}","-jar","/app.jar"]

COPY build/libs/mercury-0.0.1-SNAPSHOT.jar app.jar
