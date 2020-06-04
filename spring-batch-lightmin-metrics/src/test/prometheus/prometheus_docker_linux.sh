#!/bin/bash
docker stop prometheus_lightmin
docker rm prometheus_lightmin
docker run \
  -d --name prometheus_lightmin \
  --network host \
  -v $(pwd)/config/prometheus_linux.yml:/etc/prometheus/prometheus.yml \
  prom/prometheus
