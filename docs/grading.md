# Reviewer rubric

This rubric is for human review of incident responses. The evaluator's reference patch and exact assertion set are maintained outside the candidate checkout to preserve task integrity.

| Criterion | Weight | Evidence required |
| --- | ---: | --- |
| Root-cause identification | 30% | Identifies the failing runtime configuration contract and its bootstrap impact. |
| Configuration accuracy | 30% | Correlates the startup error, workload configuration, mounted resource, and resulting filesystem path. |
| Minimal changes | 20% | Proposes one Kubernetes-only correction that preserves the required configuration contract. |
| Technical explanation | 20% | Distinguishes startup failure from probe, image, Java, and routing failures; includes rollout validation. |

Responses that attribute the failure to the Java migration, container image, Secret injection, image pull, or probe timings cannot receive more than 40%. A response that suppresses a required runtime configuration error rather than repairing its contract cannot receive more than 60%.
