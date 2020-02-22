FROM maven:3.5.2-jdk-8-alpine AS MAVEN_BUILD
MAINTAINER Subbareddy Nallamachu
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn package
FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=MAVEN_BUILD /target/java-dependency-hierarchy-1.0.0.jar /app/
ENTRYPOINT ["java", "-jar", "java-dependency-hierarchy-1.0.0.jar"]
