#!/bin/bash

set -e

if [[ -z "$1" ]] || [[ -z "$2" && "$1" = "-p" ]]
then
    echo "Please specify your user name."
    echo "Usage: ./deploy.sh [-p] <user>"
    exit 1
fi

NC='\033[0m' # No Color	echo -e "INFO: Deploying to ${RED}PRODUCTION${NC}"
if [[ "$1" = "-p" ]]
then
	RED='\033[0;31m'
	echo -e "INFO: Deploying to ${RED}PRODUCTION${NC} (https://demo.lokaler.kaufen)."
	PROD=true
	USER="$2"
else
	YELLOW='\033[1;33m'
	echo -e "INFO: Deploying to ${YELLOW}TEST${NC} (https://test.lokaler.kaufen)."
	PROD=false
	USER="$1"
fi

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

FRONTEND_DIR="${DIR}/frontend"
BACKEND_DIR="${DIR}/backend"

SPRING_STATIC_DIR="${BACKEND_DIR}/src/main/resources/static"
ARTIFACT="${BACKEND_DIR}/build/libs/mercury-0.0.1-SNAPSHOT.jar"

DEPLOY_DIR_PROD="/opt/mercury/prod"
DEPLOY_DIR_TEST="/opt/mercury/test"
HOST="lokaler.kaufen"

NPM="npm"
ANGULAR="ng"
GRADLE="./gradlew"

# Angular Build
(
    printf "Building Angular project ..."
    cd "${FRONTEND_DIR}"
    "${NPM}" install
    "${ANGULAR}" --prod build
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
    "${GRADLE}" build
    echo " done."
)

echo "Build successful."
echo "Deploying artifact ${ARTIFACT} ..."

# Deploy app to environment
if [ "$PROD" = true ]
then
	rsync -v -e ssh "${ARTIFACT}" "${USER}@${HOST}:${DEPLOY_DIR_PROD}"
	ssh "${USER}@${HOST}" sudo systemctl restart mercury-prod.service
else
	rsync -v -e ssh "${ARTIFACT}" "${USER}@${HOST}:${DEPLOY_DIR_TEST}"
	ssh "${USER}@${HOST}" sudo systemctl restart mercury-test.service
fi

exit 0
