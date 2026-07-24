# Verifier

Run `./verifier/verify.sh` to execute Maven tests, client-side Kubernetes manifest validation, and the Docker build. The intentionally faulty baseline passes these structural checks because the defect is only observable when Spring Boot resolves the mounted production configuration at runtime.

Run `./verifier/verify.sh --verify-fix` after a proposed repair. It validates the runtime configuration contract and rejects changes to the production ConfigMap. It does not disclose a reference filename or patch.
