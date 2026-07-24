# Settlement API

Settlement API is the payments-platform edge service that accepts merchant settlement instructions and returns a durable request identifier. It is deployed to the `payments-production` namespace behind the shared NGINX ingress tier.

## Incident exercise

Release `2.8.0` completed the Java 17 to Java 21 runtime migration. The GitHub Actions build and container build are green, but the production Deployment never becomes available and its Pods enter `CrashLoopBackOff`.

Your task is to identify the single defective Kubernetes configuration value and propose the smallest safe correction. The service source, Maven build, Docker image, Secret, Service, and Ingress are deliberately valid. Treat the supplied `kubectl-describe.txt`, `kubectl-logs.txt`, and `github-actions.log` as incident evidence captured shortly after rollout.

## Local development

Prerequisites: JDK 21 and Maven 3.9+.

```bash
mvn verify
mvn spring-boot:run
curl -i -X POST http://localhost:8080/v1/settlements \
  -H 'Content-Type: application/json' \
  -d '{"merchantId":"mrc_1029","amountMinor":1250,"currency":"USD"}'
```

The health endpoints are available at `/actuator/health/liveness` and `/actuator/health/readiness`.

## Delivery model

CI runs `mvn verify` on Temurin 21 and then builds the runtime image. Cluster deployment is performed by the release controller after image publication. Kubernetes runtime settings are mounted from `settlement-api-config`; the application is intentionally configured to read that mounted file explicitly.

## Repository layout

| Path | Purpose |
| --- | --- |
| `src/` | Spring Boot service and tests |
| `.github/workflows/build.yml` | Java 21 build and container verification |
| `deployment.yaml` | Production workload definition |
| `configmap.yaml` | Non-secret runtime configuration |
| `kubectl-*.txt` | Incident evidence |
| `expected_solution.md` | Maintainer answer key |
| `grading.md` | Evaluation criteria |

Apply manifests in dependency order after the `payments-production` namespace is provisioned by platform bootstrap:

```bash
kubectl apply -f configmap.yaml -f secret.yaml
kubectl apply -f deployment.yaml -f service.yaml -f ingress.yaml
```

`secret.yaml` models an External Secrets-rendered object for this exercise; production credentials are not stored in source control.
