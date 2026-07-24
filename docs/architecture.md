# Architecture

Inventory Service is a stateless read API in the commerce platform. NGINX terminates external traffic and routes `/inventory` to a ClusterIP Service. Three Pods run a Java 21 Spring Boot process, with readiness and liveness endpoints managed through Actuator.

Production settings are delivered through `inventory-service-config`. Kubernetes materializes each ConfigMap key as a filename beneath `/etc/inventory/config`. The Deployment tells Spring Boot to load a required, explicit configuration file at that mount. This makes the configuration filename a contract between the Deployment and ConfigMap. The Secret is injected separately as an environment variable and does not affect file resolution. The container uses a read-only root filesystem and receives an in-memory `/tmp` volume for embedded-server runtime files.

The delivery pipeline compiles and tests the service on Temurin 21, emits JaCoCo coverage, builds the container, and validates manifest schemas client-side. A successful image build proves the artifact is runnable with its packaged configuration; it does not validate an external configuration volume at runtime.
