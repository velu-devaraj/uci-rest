# use i option for interactive mode
#docker run -i -d --name uci-api-server-container
docker run -d --name uci-api-server-container \
-v $CERTS_DIR_PATH:/certs \
 -v $CONFIG_DIR_PATH:/config \
 -v $LOGS_DIR_PATH:/logs \
 -p 1443:1443 \
 uci-api-server-image:latest "$@"
