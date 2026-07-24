# Expected solution

## Root cause

`deployment.yaml` sets `SPRING_CONFIG_ADDITIONAL_LOCATION` to:

```text
file:/etc/settlement/config/application-prod.yml
```

That is a required file location. The ConfigMap mounted at `/etc/settlement/config` provides the key `application-production.yml`, so Kubernetes creates `/etc/settlement/config/application-production.yml` instead. There is no `application-prod.yml` file. Spring Boot exits during config-data resolution, which accounts for the exit code 1 and `CrashLoopBackOff` before either health probe can run.

The evidence is consistent across all three required sources:

1. `kubectl-logs.txt` names the missing `application-prod.yml` location.
2. `deployment.yaml` defines that exact additional location and mounts the ConfigMap at the corresponding directory.
3. `configmap.yaml` defines the actual mounted filename as `application-production.yml`.

## Minimal correction

Change only the Deployment environment value to match the ConfigMap key:

```diff
 - name: SPRING_CONFIG_ADDITIONAL_LOCATION
-  value: file:/etc/settlement/config/application-prod.yml
+  value: file:/etc/settlement/config/application-production.yml
```

Then apply and observe the rollout:

```bash
kubectl apply -f deployment.yaml
kubectl -n payments-production rollout status deployment/settlement-api
kubectl -n payments-production get pods -l app.kubernetes.io/name=settlement-api
```

## Non-solutions

- Do not change Java code, the Maven configuration, image, or CI workflow; all are functioning.
- Do not mark the location `optional:`. That hides a required production-config contract violation and can start the service with incomplete settings.
- Do not alter liveness/readiness timings; the JVM exits before probes are relevant.
- Renaming the ConfigMap key would also work mechanically, but changes the established configuration contract. Correcting the stale Deployment reference is the smallest safe fix.
