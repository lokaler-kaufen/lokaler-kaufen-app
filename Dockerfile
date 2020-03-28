#############################
# STAGE 1: Frontend build   #
#############################
FROM node:12.16.1-alpine3.11 AS frontend-build

COPY . /workspace
WORKDIR /workspace/frontend

# Install tools & libs
RUN npm install

# Build app
RUN npm run build


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
FROM openjdk:11.0.6-jre-slim

COPY --from=backend-build /workspace/backend/build/libs/mercury-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=${PORT}","-jar","/app.jar"]