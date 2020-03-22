#!/bin/bash

set -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

FRONTEND_DIR="${DIR}/frontend"
BACKEND_DIR="${DIR}/backend"

SPRING_STATIC_DIR="${BACKEND_DIR}/src/main/resources/static"

NPM="npm"
GRADLE="./gradlew"

(
    printf "Building Angular project ..."
    cd "${FRONTEND_DIR}"
    ${NPM} install
    ${NPM} run build
    echo "  done."

)

rm -r "${SPRING_STATIC_DIR}/dist" && echo '**' > "${SPRING_STATIC_DIR}/.gitignore"
cp -r "${FRONTEND_DIR}/dist/mercury-ui/." "${SPRING_STATIC_DIR}/"
echo "Copied Angular artifacts to ${SPRING_STATIC_DIR}"

(
    printf "Building Gradle project ..."
    cd "${BACKEND_DIR}"
    "${GRADLE}" build
    echo " done."
)

echo "Build successful."

exit 0
