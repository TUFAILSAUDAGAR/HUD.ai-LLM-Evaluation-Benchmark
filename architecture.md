# Architecture: Settlement API

## Runtime context

```text
Merchant -> NGINX Ingress -> settlement-api Service -> settlement-api Pods
                                                    |-- ConfigMap volume: /etc/settlement/config
                                                    |-- Secret env: PARTNER_API_TOKEN
                                                    `-- Spring Boot 3.3 / Java 21
```

The public endpoint accepts a validated settlement request and returns HTTP 202. Actuator liveness and readiness probes run on the same HTTP port. The API has no database dependency in this exercise; the focus is the application bootstrap path.

## Configuration contract

Spring Boot always loads the packaged `application.yml`. Production overrides are delivered in the ConfigMap named `settlement-api-config`. Kubernetes mounts each ConfigMap key as a file under `/etc/settlement/config`; therefore the ConfigMap key is also the expected filename. The Deployment's `SPRING_CONFIG_ADDITIONAL_LOCATION` is an explicit, required Spring configuration location. An absent resource at that path terminates Spring Boot before its web server starts.

The Secret is injected independently as an environment variable and is not involved in Spring's config-file resolution. Service and Ingress only route to a ready Pod, so they cannot cause a JVM bootstrap failure.

## Deployment sequence

1. CI compiles/tests with JDK 21 and builds the image.
2. The release controller applies ConfigMap, Secret, Deployment, Service, and Ingress.
3. Kubelet creates the ConfigMap volume, injects the Secret, and starts the container.
4. Spring resolves its packaged configuration and the explicit additional runtime location.
5. On successful startup, probes mark Pods ready and the Service receives endpoints.

Failure before step 5 is observable in container logs rather than probe failures.
