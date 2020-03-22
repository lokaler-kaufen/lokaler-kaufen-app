FROM openjdk:11.0.6-jre-slim

ARG JAR_FILE=JAR_FILE_MUST_BE_SPECIFIED_AS_BUILD_ARG
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=${PORT}","-jar","/app.jar"]