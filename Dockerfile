FROM maven:3.6-jdk-8-alpine AS maven

WORKDIR /build
COPY pom.xml .
# cache dependencies for subsequent builds as long as pom.xml doesn't change
RUN mvn dependency:go-offline

COPY src/ /build/src/
RUN mvn package

FROM openjdk:8-jdk-alpine

COPY import-rds-certs.sh .
RUN ./import-rds-certs.sh

COPY --from=maven /build/target/datawarehouse-sync-iam-0.2.0-jar-with-dependencies.jar /usr/bin/datawarehouse-sync-iam.jar

CMD java -jar /usr/bin/datawarehouse-sync-iam.jar
