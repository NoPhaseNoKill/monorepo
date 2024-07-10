#!/bin/bash

# Launches build remote cache server on port 5071
# Usage:
#   - Requires the absolute path to the root dir (ie: /home/tomga/projects/monorepo/toolset/jvm )
#
# Note: It puts the build cache data to: /opt/build-cache-node

exec remote-build-cache/scripts/setup-remote-build-cache.sh $1