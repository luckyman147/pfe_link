#!/usr/bin/env bash
set -euo pipefail
mkdir -p "$(dirname "$0")/certs"
docker run --rm -v "$(pwd)/backend/nginx/certs:/certs" alpine/openssl req \
  -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout /certs/dev-selfsigned.key \
  -out /certs/dev-selfsigned.crt \
  -subj "/CN=localhost"
echo "Generated backend/nginx/certs/dev-selfsigned.{crt,key}"
