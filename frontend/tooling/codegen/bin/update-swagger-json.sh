#!/bin/bash

rm ../swagger.json
curl http://localhost:8080/v2/api-docs > ../swagger.json

echo "done"
