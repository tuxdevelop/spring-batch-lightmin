#!/bin/bash
ID=$(id -u) # saves your user id in the ID variable
docker stop grafana_lightmin
docker rm grafana_lightmin
mkdir -p ./data_grafana_local
docker run \
  -d --name grafana_lightmin \
  -e "GF_INSTALL_PLUGINS=flant-statusmap-panel" \
  --user $ID \
  --volume "$(pwd)/data_grafana_local:/var/lib/grafana" \
  -p 3000:3000 grafana/grafana
