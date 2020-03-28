#############################
# STAGE 1: Frontend build   #
#############################
FROM node:12.16.1-stretch AS frontend-build

# First: Copy only npm stuff so that we don't download the whole internet on every build
COPY frontend/package.json /workspace/frontend/package.json
COPY frontend/package-lock.json /workspace/frontend/package-lock.json
WORKDIR /workspace/frontend

# Install dependencies / dev dependencies
RUN npm ci

# Copy the remaining stuff
COPY . /workspace

# Build app
RUN npm run build-prod

#############################
# STAGE 2: Backend  build   #
#############################
FROM gradle:jdk11 AS backend-build

# prepare workspace
COPY . /workspace
COPY --from=frontend-build /workspace/frontend/dist/mercury-ui/. /workspace/backend/src/main/resources/static
WORKDIR /workspace/backend

# run backend build
RUN gradle build

##############################
# STAGE 3: App package build #
##############################
FROM openjdk:11-jre-slim

COPY --from=backend-build /workspace/backend/build/libs/mercury-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=${PORT}","-jar","/app.jar"]
