FROM maven:3.6-jdk-8-alpine AS maven

WORKDIR /build
COPY pom.xml .
# cache dependencies for subsequent builds as long as pom.xml doesn't change
RUN mvn dependency:go-offline

COPY src/ /build/src/
RUN mvn package

FROM openjdk:8
# Had issues with combinded cert bundle
RUN curl https://s3.amazonaws.com/rds-downloads/rds-ca-2019-root.pem -o rds-ca-2019-root.pem -s
RUN curl https://s3.amazonaws.com/rds-downloads/rds-ca-2019-us-west-2.pem -o rds-ca-2019-us-west-2.pem -s
RUN keytool -import -noprompt -trustcacerts -alias aws_rds_ca_2019_root -file rds-ca-2019-root.pem -storepass changeit -keystore "$JAVA_HOME/jre/lib/security/cacerts"
RUN keytool -import -noprompt -trustcacerts -alias aws_rds_ca_us_west_2 -file rds-ca-2019-us-west-2.pem -storepass changeit -keystore "$JAVA_HOME/jre/lib/security/cacerts"

COPY --from=maven /build/target/datawarehouse-sync-iam-0.2.0-jar-with-dependencies.jar /usr/bin/datawarehouse-sync-iam.jar

CMD java -jar /usr/bin/datawarehouse-sync-iam.jar
