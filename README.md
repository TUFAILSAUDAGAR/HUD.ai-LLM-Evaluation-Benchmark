# Inventory Service

Inventory Service is the internal API used by the order-routing platform to retrieve available-to-promise stock for a SKU. The service is deployed to the production Kubernetes cluster as a stateless Spring Boot workload and is exposed only through the platform ingress.

## Project overview

The service provides a narrow, read-only inventory view for downstream checkout and fulfilment workflows. Its API contract is intentionally small: `GET /health` reports service health and `GET /inventory` returns the inventory snapshot selected by query parameters.

This repository is a production-incident benchmark. A Java 21 migration and its build pipeline completed successfully, but the production rollout does not become available. The supplied logs capture the initial incident response window. Application code is not the source of the failure.

## Architecture

```text
Order Routing -> NGINX Ingress -> ClusterIP Service -> inventory-service Pods
                                                       |-- ConfigMap volume
                                                       |-- runtime Secret
                                                       `-- Spring Boot 3 / Java 21
```

The packaged application configuration supplies local defaults. Production configuration is delivered by a ConfigMap volume. The workload uses an explicit Spring configuration location so an invalid runtime configuration fails fast rather than serving with incomplete production settings. More detail is in [docs/architecture.md](docs/architecture.md).

## Technology stack

| Area | Technology |
| --- | --- |
| Runtime | Java 21, Spring Boot 3.3 |
| Build | Maven, JUnit 5, JaCoCo, Spotless, Checkstyle |
| Container | Multi-stage Docker build, Eclipse Temurin JRE 21 |
| Delivery | GitHub Actions |
| Platform | Kubernetes, NGINX Ingress |

## Local development

Install JDK 21 and Maven 3.9 or newer, then run:

```bash
mvn -f inventory-service/pom.xml verify
mvn -f inventory-service/pom.xml spring-boot:run
curl http://localhost:8080/health
curl 'http://localhost:8080/inventory?sku=SKU-1001&warehouse=eu-west-1'
```

## Deployment

The platform bootstrap process creates the production namespace and image pull credentials. Apply these resources in order:

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml -f k8s/secret.yaml
kubectl apply -f k8s/deployment.yaml -f k8s/service.yaml -f k8s/ingress.yaml
```

## Troubleshooting

- Build and image evidence: [logs/github-actions.log](logs/github-actions.log)
- Workload status and events: [logs/kubectl-describe.txt](logs/kubectl-describe.txt)
- Previous container output: [logs/kubectl-logs.txt](logs/kubectl-logs.txt)
- Automated local checks: [verifier/README.md](verifier/README.md)

## Known production incident

Release `3.4.0` exhibits `CrashLoopBackOff` immediately after the deployment controller creates Pods. CI passed, the image was pulled successfully, and Kubernetes mounted the referenced resources. Investigate the runtime configuration contract using the manifests and captured evidence. Avoid changing application source, build tooling, or health probes unless evidence demonstrates they are involved.

## Repository structure

```text
inventory-service/   Spring Boot application, tests, and image definition
k8s/                 Production Kubernetes resources
logs/                Captured CI and Kubernetes incident evidence
docs/                Architecture, solution key, and scoring rubric
verifier/            Repeatable benchmark validation
```
