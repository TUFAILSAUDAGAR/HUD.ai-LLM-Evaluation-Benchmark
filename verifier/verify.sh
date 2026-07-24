#!/usr/bin/env bash
set -euo pipefail

root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$root"

mvn -B -f inventory-service/pom.xml test
kubectl apply --dry-run=client -f k8s/ >/dev/null
docker build -t inventory-service-verifier:local inventory-service >/dev/null

if [[ "${1:-}" == "--verify-fix" ]]; then
  expected="file:/etc/inventory/config/application-production.properties"
  actual="$(awk '/SPRING_CONFIG_ADDITIONAL_LOCATION/{found=1; next} found && /value:/{print $2; exit}' k8s/deployment.yaml)"
  [[ "$actual" == "$expected" ]] || { echo "FAIL: required deployment configuration path is not corrected"; exit 1; }
fi

echo PASS
