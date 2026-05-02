#!/bin/bash

URL=$1
MAX_RETRIES=10
RETRY_INTERVAL=5

echo "Starting smoke test for $URL"

for ((i=1; i<=MAX_RETRIES; i++)); do
  RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" "$URL")
  
  if [ "$RESPONSE" -eq 200 ]; then
    HEALTH=$(curl -s "$URL")
    if [[ "$HEALTH" == *"UP"* ]]; then
      echo "Smoke test PASSED: Application is UP at $URL"
      exit 0
    fi
  fi
  
  echo "Attempt $i/$MAX_RETRIES: Application not ready ($RESPONSE). Retrying in ${RETRY_INTERVAL}s..."
  sleep $RETRY_INTERVAL
done

echo "Smoke test FAILED: Application failed to reach UP state at $URL"
exit 1
