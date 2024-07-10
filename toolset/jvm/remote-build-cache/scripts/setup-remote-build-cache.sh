#!/bin/bash

# Launches build remote cache server on port 5071
# Usage:
#   - Requires the absolute path to the root dir (ie: /home/tomga/projects/monorepo/toolset/jvm )
if [ -z "$1" ]; then
  echo "Usage: $0 /home/tomga/projects/monorepo/toolset/jvm"
  exit 1
fi

ROOT_DIR="$1"
ABSOLUTE_PATH_TO_REMOTE_BUILD_SERVER_JAR="$ROOT_DIR/remote-build-cache/build-cache-node-19.1.jar"
ABSOLUTE_PATH_TO_THIS_SCRIPT="$ROOT_DIR/remote-build-cache/build-cache-node.sh"
SERVICE_FILE="/etc/systemd/system/gradle-remote-build-cache.service"

# Create gradle-remote-build-cache.service for automatic starting
if [ ! -f "$SERVICE_FILE" ]; then
  sudo bash -c "cat << EOF > $SERVICE_FILE
[Unit]
Description=Gradle Remote Build Cache
After=network.target
StartLimitIntervalSec=0

[Service]
# To improve security you should create a separate user to run the Build Cache Node
#User=gradle
#Group=gradle
# Allow using ports below 1024, required if not run as the superuser
AmbientCapabilities=CAP_NET_BIND_SERVICE
Restart=always
RestartSec=1
ExecStart=$ABSOLUTE_PATH_TO_THIS_SCRIPT

[Install]
WantedBy=multi-user.target
EOF"
else
  echo "Service file $SERVICE_FILE already exists. Skipping creation."
fi

# Start the build cache node for the first time
sudo systemctl start gradle-remote-build-cache

# Enable systemd to start the build cache node on system boot
sudo systemctl enable gradle-remote-build-cache

echo "Starting service at http://localhost:5071 ....."

# Start the server for verification
java -jar $ABSOLUTE_PATH_TO_REMOTE_BUILD_SERVER_JAR start --data-dir=/opt/build-cache-node