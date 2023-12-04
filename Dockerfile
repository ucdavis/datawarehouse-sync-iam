FROM maven:3.6-jdk-8-alpine AS maven

WORKDIR /build
COPY pom.xml .
# cache dependencies for subsequent builds as long as pom.xml doesn't change
RUN mvn dependency:go-offline

COPY src/ /build/src/
RUN mvn package

FROM openjdk:8-jdk-alpine
RUN apk --no-cache add curl

RUN curl https://truststore.pki.rds.amazonaws.com/us-west-2/us-west-2-bundle.pem -o us-west-2-bundle.pem -s
RUN keytool -import -noprompt -trustcacerts -alias aws_us_west_2_bundle -file us-west-2-bundle.pem -storepass changeit -keystore "$JAVA_HOME/jre/lib/security/cacerts"

COPY --from=maven /build/target/datawarehouse-sync-iam-0.2.0-jar-with-dependencies.jar /usr/bin/datawarehouse-sync-iam.jar

CMD java -jar /usr/bin/datawarehouse-sync-iam.jar
