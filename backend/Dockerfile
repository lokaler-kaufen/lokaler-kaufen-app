ARG VERSION=11.0.6

# Stage 1, build container
FROM openjdk:${VERSION}-jdk-slim as BUILD
COPY . .
RUN ./gradlew --no-daemon test assemble

# Stage 2, distribution container
FROM openjdk:${VERSION}-jre-slim
COPY --from=BUILD /build/libs/mercury-0.0.1-SNAPSHOT.jar /bin/run.jar
CMD ["java", "-jar", "/bin/run.jar"]

