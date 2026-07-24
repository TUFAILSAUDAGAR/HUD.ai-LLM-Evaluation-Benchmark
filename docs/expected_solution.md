# Expected solution

## Root cause

The Deployment requires `file:/etc/inventory/config/application-prod.properties`, but the mounted ConfigMap key creates `application-production.properties`. Spring Boot treats `SPRING_CONFIG_ADDITIONAL_LOCATION` as mandatory and exits before the web server and probes start because the requested file does not exist.

`logs/kubectl-logs.txt` identifies the missing path. `k8s/deployment.yaml` declares that path and mount directory; `k8s/configmap.yaml` provides the different filename. CI and image pull evidence rule out the Java migration and image delivery.

## Correct YAML

```yaml
- name: SPRING_CONFIG_ADDITIONAL_LOCATION
  value: file:/etc/inventory/config/application-production.properties
```

## Verification

Apply the corrected Deployment, then run `kubectl -n commerce-production rollout status deployment/inventory-service` and confirm all three Pods are Ready. `verifier/verify.sh --verify-fix` checks that the Deployment path and ConfigMap filename agree.
