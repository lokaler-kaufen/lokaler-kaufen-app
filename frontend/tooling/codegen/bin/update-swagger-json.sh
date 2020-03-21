#!/bin/bash

rm ../swagger.json
curl http://localhost:8080/v2/api-docs > ../swagger_original.json

# Set the actual local frontend host
sed 's/localhost:8080/localhost:4200/' ../swagger_original.json > ../swagger.json

echo "done"
