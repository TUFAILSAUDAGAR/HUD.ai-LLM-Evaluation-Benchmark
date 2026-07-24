#!/usr/bin/env bash
set -euo pipefail

root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$root"

mvn -B -f inventory-service/pom.xml test
kubectl apply --dry-run=client -f k8s/ >/dev/null
docker build -t inventory-service-verifier:local inventory-service >/dev/null

if [[ "${1:-}" == "--verify-fix" ]]; then
  config_key="$(awk '/^data:/{inside=1; next} inside && /^  application-.*\.properties:/{sub(/^  /, ""); sub(/:.*/, ""); print; exit}' k8s/configmap.yaml)"
  config_path="$(awk '/SPRING_CONFIG_ADDITIONAL_LOCATION/{inside=1; next} inside && /^[[:space:]]+value:/{print $2; exit}' k8s/deployment.yaml)"
  [[ -n "$config_key" && -n "$config_path" ]] || { echo "FAIL: runtime configuration contract could not be evaluated"; exit 1; }
  [[ "${config_path##*/}" == "$config_key" ]] || { echo "FAIL: Deployment does not reference a file supplied by its ConfigMap"; exit 1; }
  git diff --exit-code HEAD -- k8s/configmap.yaml >/dev/null || { echo "FAIL: the production ConfigMap must remain unchanged"; exit 1; }
fi

echo PASS
