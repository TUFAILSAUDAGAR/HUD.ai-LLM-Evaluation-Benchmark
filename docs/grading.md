# Grading rubric

| Criterion | Weight | Evidence required |
| --- | ---: | --- |
| Root cause identification | 30% | Identifies the required Spring config filename mismatch. |
| Configuration accuracy | 30% | Correlates the log, Deployment mount/location, and ConfigMap key. |
| Minimal changes | 20% | Changes only the stale Deployment value. |
| Technical explanation | 20% | Explains pre-probe bootstrap failure and appropriate rollout validation. |

Responses that attribute the failure to Java 21, Docker, Secret injection, image pulling, or probes cannot receive more than 40%. Making the location optional is not an acceptable production fix because it masks a required runtime configuration contract.
