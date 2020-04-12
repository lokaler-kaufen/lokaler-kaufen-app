#!/bin/bash

set -eo pipefail

if [[ -z "$1" ]] || [[ -z "$2" && "$1" = "-p" ]] || [[ -z "$2" && "$1" = "-s" ]]
then
    echo "Please specify your user name."
    echo "Usage: ./deploy.sh [-p] [-s] <user>"
    echo "  -p: deploy to production (mutally exclusive with -s)"
    echo "  -s: deploy to staging (mutally exclusive with -p)"
    exit 1
fi

NC='\033[0m' # No Color	echo -e "INFO: Deploying to ${RED}PRODUCTION${NC}"
if [[ "$1" = "-p" ]]
then
	RED='\033[0;31m'
	echo -e "INFO: Deploying to ${RED}PRODUCTION${NC} (https://app.lokaler.kaufen)."
	ENV="prod"
	USER="$2"
elif [[ "$1" = "-s" ]]
then
    YELLOW='\033[1;33m'
	echo -e "INFO: Deploying to ${YELLOW}STAGING${NC} (https://staging.lokaler.kaufen)."
	ENV="staging"
	USER="$2"
else
	GREEN='\033[1;32m'
	echo -e "INFO: Deploying to ${GREEN}TEST${NC} (https://test.lokaler.kaufen)."
	ENV="test"
	USER="$1"
fi

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

FRONTEND_DIR="${DIR}/frontend"
BACKEND_DIR="${DIR}/backend"

SPRING_STATIC_DIR="${BACKEND_DIR}/src/main/resources/static"
ARTIFACT="${BACKEND_DIR}/build/libs/mercury.jar"

DEPLOY_DIR="/opt/mercury/$ENV"
SERVICE_NAME="mercury-$ENV"
HOST="lokaler.kaufen"

NPM="npm"
GRADLE="./gradlew"

# Angular Build
(
    printf "Building Angular project ..."
    cd "${FRONTEND_DIR}"
    "${NPM}" ci
    "${NPM}" run build-prod
    echo "  done."
)

# Copy Angular dist files to Spring Boot static files directory
rm -rf "${SPRING_STATIC_DIR}" && mkdir -p "${SPRING_STATIC_DIR}" && echo '**' > "${SPRING_STATIC_DIR}/.gitignore"
cp -r "${FRONTEND_DIR}/dist/mercury-ui/." "${SPRING_STATIC_DIR}/"
echo "Copied Angular artifacts to ${SPRING_STATIC_DIR}"

# Build Spring Boot app
(
    printf "Building Gradle project ..."
    cd "${BACKEND_DIR}"
    "${GRADLE}" clean build
    echo " done."
)

echo "Build successful."
echo "Deploying artifact ${ARTIFACT} ..."

# Deploy app to environment
rsync -v -e ssh "${ARTIFACT}" "${USER}@${HOST}:${DEPLOY_DIR}"
ssh "${USER}@${HOST}" sudo systemctl restart "${SERVICE_NAME}".service

exit 0
