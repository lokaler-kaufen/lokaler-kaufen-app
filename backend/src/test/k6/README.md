# Load Tests with K6

For easy testing install K6 locally, alternatively you can run everything containerized.
First, fire up InfluxDB and Grafana for metrics visualization.

```bash
# start the Influx DB and Grafana container
$ docker-compose up -d influxdb grafana

$ open http://localhost:3000
$ echo "Import the Grafana dashboard https://grafana.com/dashboards/2587"

# the following commands run K6 within Docker
$ docker-compose run -v $PWD/scripts:/scripts k6 run -u 50 -d 60s /scripts/angular.js
$ docker-compose run -v $PWD/scripts:/scripts k6 run -u 0 -s 10s:100 -s 60s -s 10s:0 /scripts/angular.js

$ docker-compose run -v $PWD/scripts:/scripts k6 run -u 50 -d 60s /scripts/rest-api.js
$ docker-compose run -v $PWD/scripts:/scripts k6 run -u 0 -s 10s:100 -s 60s -s 10s:0 /scripts/rest-api.js
```

Alternatively, run the load test locally and export metrics to Influx DB.
```bash
$ k6 run -u 50 -d 60s /scripts/angular.js -o influxdb=http://localhost:8086/k6
$ k6 run -u 0 -s 10s:100 -s 60s -s 10s:0 /scripts/angular.js -o influxdb=http://localhost:8086/k6

$ k6 run -u 50 -d 60s /scripts/rest-api.js influxdb=http://localhost:8086/k6
$ k6 run -u 0 -s 10s:100 -s 60s -s 10s:0 /scripts/rest-api.js -o influxdb=http://localhost:8086/k6
```