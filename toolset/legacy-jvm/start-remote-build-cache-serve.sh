#!/bin/bash

# Launches build remote cache server on port you specify
# Example usage: ./start-remote-build-cache-serve.sh $HOME/projects/monorepo/toolset/jvm 5071
#
# Would launch it on port 5071
#
# Note: It puts the build cache data to: ~/.cache/build-cache-node

#exec remote-build-cache/scripts/setup-remote-build-cache.sh $1 $2

# Launches build remote cache server on port you specify
# Example usage: ./start-remote-build-cache-serve.sh $1
# Important note: It also adds this to your computer's startup tasks, so you don't need to manually start it each time

#!/bin/bash

if [ -z "$1" ]; then
  echo "Usage: ./start-remote-build-cache-serve.sh <port>"
  echo "Example: ./start-remote-build-cache-serve.sh 8080 which starts build cache server on port 8080"
  exit 1
fi



CONTAINER_NAME="remote-build-cache"
CONTAINER_DATA_LOCATION="$HOME/.cache/build-cache-data"
PORT=$1
MAX_ATTEMPTS=10
SLEEP_INTERVAL=3

# Log function for easier debug output
log() {
  echo "[INFO] $1"
}

# Create necessary directories and config.yaml file
log "Creating directories and config.yaml file..."
mkdir -p $CONTAINER_DATA_LOCATION/conf
touch $CONTAINER_DATA_LOCATION/conf/config.yaml

# Add config content to config.yaml
log "Writing config.yaml contents..."
cat <<EOF > $CONTAINER_DATA_LOCATION/conf/config.yaml
version: 5
cache:
  accessControl:
    users:
      DEFAULT_CACHE_USER:
        level: readwrite
        password: QapBR+C6/z9kdhQGytuQS2ojIIqryMIeo2fB/9lQ1l4=:bvg/PU9PC6MKTiSHcJ45+Cq+udFyE9efZE+BEZNaZIM=
        note: ""
      user2:
        level: readwrite
        password: FrpDSYG+wLLZBD0G4ckdGEN/I7wve5tNQzPpLdfCWFE=:aw8ar1GqenwyjfFf/Mei/LJknj8bRrrTce2H9+ZSvzo=
        note: ""
    anonymousLevel: none
  freeSpaceBufferSize: 1024
  maxEntryAgeInHours: 168
  targetSize:
    type: fixed
    size: 10000
  maxArtifactSize: 100
registration: null
uiAccess:
  type: secure
  username: DEFAULT_USERNAME
  password: +VRUzrvG0cMObN7L+n773wI7INZ4CyAjSVWBpL9+Hls=:2vItqZITjaZXV6HZ2by7r/W6rzkUGp6UriEPsiL+K/Q=
EOF

# Start the Docker container
log "Starting the Docker container..."
docker run --detach \
      --user $UID \
      --name $CONTAINER_NAME \
      --volume $CONTAINER_DATA_LOCATION:/data \
      --publish $PORT:5071 \
      --publish 6011:6011 \
      gradle/build-cache-node:20.0 \
      start --data-dir=/data

# Step 1: Wait for the container to be in the "running" state
log "Waiting for the container to be in the 'running' state..."
attempt=1
while [ "$(docker container inspect -f '{{.State.Status}}' $CONTAINER_NAME)" != "running" ] && [ $attempt -le $MAX_ATTEMPTS ]; do
  log "Attempt $attempt/$MAX_ATTEMPTS: Container status is not 'running'. Retrying in $SLEEP_INTERVAL seconds..."
  sleep $SLEEP_INTERVAL
  attempt=$((attempt+1))
done

# Check if the container is running
container_status=$(docker container inspect -f '{{.State.Status}}' $CONTAINER_NAME)
log "Container status: $container_status"
if [ "$container_status" != "running" ]; then
  log "Container failed to start after $MAX_ATTEMPTS attempts."
  exit 1
fi

# Step 2: Wait for the /status endpoint to return {"error":false} using Basic Authentication
AUTH_HEADER="Authorization: Basic $(echo -n 'DEFAULT_CACHE_USER:DEFAULT_CACHE_USER_PASSWORD1!' | base64)"

#check_service_status() {
#  log "Checking service status at http://localhost:$PORT/status with authentication..."
#  response=$(curl -s -H "$AUTH_HEADER" http://localhost:$PORT/status)
#  log "Received response from /status: $response"
#  echo "$response" | grep '"error":false' > /dev/null 2>&1
#}

AUTH_TOKEN="DEFAULT_USERNAME:DEFAULT_PASSWORD1!"


check_service_status() {
  log "Checking service status at http://localhost:$PORT/status with authentication..."

  echo "Encoding..."
#  ENCODED_AUTH_TOKEN=$(echo DEFAULT_USERNAME:DEFAULT_PASSWORD1! | base64 --encode)
  ENCODED_AUTH_TOKEN=$(echo -n $AUTH_TOKEN | base64)
  echo "Encoded token: $ENCODED_AUTH_TOKEN"
  echo "Encoded token should be: REVGQVVMVF9VU0VSTkFNRTpERUZBVUxUX1BBU1NXT1JEMSE="

  response=$(curl 'http://localhost:8080/status' \
               -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7' \
               -H "Authorization: Basic $ENCODED_AUTH_TOKEN" \
               )
  log "Received response from /status: $response"
  echo "$response" | grep '"error":false' > /dev/null 2>&1
}

attempt=1
while ! check_service_status && [ $attempt -le $MAX_ATTEMPTS ]; do
  log "Attempt $attempt/$MAX_ATTEMPTS: Waiting for service to respond with {'error': false}..."
  sleep $SLEEP_INTERVAL
  attempt=$((attempt+1))
done

# Check if the service is ready
if check_service_status; then
  log "Service is ready. Opening http://localhost:$PORT"
  # Open the page in the default browser (on Linux or macOS)
  xdg-open "http://localhost:$PORT" || open "http://localhost:$PORT"
else
  log "Service failed to start after $MAX_ATTEMPTS attempts."
fi

