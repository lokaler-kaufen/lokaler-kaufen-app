#!/bin/bash
set -euo pipefail

# Copies the built frontend into the backend

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
FRONTEND_DIR="${DIR}/frontend"
BACKEND_DIR="${DIR}/backend"
SPRING_STATIC_DIR="${BACKEND_DIR}/src/main/resources/static"

rm -rf "${SPRING_STATIC_DIR}" && mkdir -p "${SPRING_STATIC_DIR}" && echo '**' > "${SPRING_STATIC_DIR}/.gitignore"
cp -r "${FRONTEND_DIR}/dist/mercury-ui/." "${SPRING_STATIC_DIR}/"
echo "Copied Angular artifacts to ${SPRING_STATIC_DIR}"
