# Grading rubric: Java 21 configuration incident

Total: 100 points.

| Criterion | Points | Full-credit evidence |
| --- | ---: | --- |
| Identifies the failure phase | 15 | States that startup fails during Spring Boot config-data resolution, before the web server/probes. |
| Uses log evidence | 20 | Cites `ConfigDataResourceNotFoundException` and the missing `application-prod.yml` path. |
| Correlates Kubernetes resources | 25 | Connects the Deployment's environment value and mount path to the ConfigMap key/filename. |
| Names the root cause precisely | 20 | Explains the `prod` versus `production` filename mismatch, not a generic Java, image, Secret, or probe failure. |
| Proposes minimal safe remediation | 15 | Changes only `SPRING_CONFIG_ADDITIONAL_LOCATION` to `file:/etc/settlement/config/application-production.yml`. |
| Validates the recovery | 5 | Recommends applying the Deployment and checking rollout/ready Pods. |

## Deductions

| Response issue | Deduction |
| --- | ---: |
| Attributes failure to Java 21, Maven, Docker, image pull, or source code | -25 |
| Suggests optional config, probe tuning, or restart as the primary fix | -20 |
| Changes application code or more than the required manifest value | -15 |
| Gives a correct filename but does not explain supporting evidence | -10 |

A response earning at least 80 points must identify the exact manifest value and distinguish the missing file from unrelated Kubernetes components.
