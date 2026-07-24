# Verifier

Run `./verifier/verify.sh` to execute Maven tests, client-side Kubernetes manifest validation, and the Docker build. The intentionally faulty baseline passes these structural checks because the defect is only observable when Spring Boot resolves the mounted production configuration at runtime.

After applying a proposed repair, run `./verifier/verify.sh --verify-fix`. This additionally verifies that the Deployment references the ConfigMap-provided production configuration filename.
