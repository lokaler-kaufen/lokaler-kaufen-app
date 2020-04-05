###########################
# STAGE 1: Backend  build #
###########################
FROM gradle:jdk11 AS backend-build

# prepare workspace
COPY . /workspace
WORKDIR /workspace

# run backend build
RUN gradle --no-daemon build -x test

##############################
# STAGE 2: App package build #
##############################
FROM openjdk:11-jre-slim
ENV PORT=8080
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://database:5432/postgres
# Install graphicsmagick
RUN apt-get update && apt-get install -y --no-install-recommends graphicsmagick && apt-get clean

COPY --from=backend-build /workspace/build/libs/mercury.jar app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-XX:+ExitOnOutOfMemoryError", "-Dserver.port=${PORT}","-jar","/app.jar", "--spring.profiles.active=dev"]
