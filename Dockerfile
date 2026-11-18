FROM maven:3.9-amazoncorretto-21-al2023 AS builder

WORKDIR /build
COPY pom.xml .
# cache dependencies for subsequent builds as long as pom.xml doesn't change
RUN mvn dependency:go-offline

COPY src/ /build/src/
RUN mvn package

FROM amazoncorretto:21-al2023-headless

RUN dnf install -y openssl wget && \
    dnf clean all

COPY import-rds-certs.sh .
RUN ./import-rds-certs.sh

COPY --from=builder /build/target/datawarehouse-sync-iam-0.2.0-jar-with-dependencies.jar /usr/bin/datawarehouse-sync-iam.jar

CMD ["java", "-jar", "/usr/bin/datawarehouse-sync-iam.jar"]
