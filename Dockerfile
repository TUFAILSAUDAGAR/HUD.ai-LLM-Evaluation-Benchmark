FROM eclipse-temurin:21-jre-jammy

RUN groupadd --system --gid 10001 northstar && useradd --system --uid 10001 --gid northstar --no-create-home northstar
WORKDIR /opt/settlement-api
COPY target/settlement-api-2.8.0.jar app.jar
USER 10001
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/opt/settlement-api/app.jar"]
