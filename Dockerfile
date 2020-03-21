ARG VERSION=11.0.6

# Stage 1: build container
#FROM openjdk:${VERSION}-jdk-slim as BUILD
#COPY . .
#WORKDIR backend
#RUN ./gradlew --no-daemon test assemble

# Stage 2: runtime container
FROM openjdk:${VERSION}-jre-slim
#COPY --from=BUILD backend/build/libs/mercury-0.0.1-SNAPSHOT.jar /bin/run.jar
#CMD ["java", "-jar", "/bin/run.jar"]

ARG JAR_FILE=JAR_FILE_MUST_BE_SPECIFIED_AS_BUILD_ARG
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Djava.security.edg=file:/dev/./urandom","-jar","/app.jar"]