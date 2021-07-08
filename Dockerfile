FROM maven:3.6.3-jdk-11 AS MAVEN_BUILD

# copy the pom and src code to the container
COPY ./ ./

# package application code
RUN mvn clean package -DskipTests

# the second stage of our build will use open jdk 8 on alpine 3.9
FROM openjdk:8-jre-alpine3.9

# copy only the artifacts we need from the first stage and discard the rest
COPY --from=MAVEN_BUILD ./target/demo-0.0.1-SNAPSHOT.jar /stanbic.jar

# set the startup command to execute the jar
CMD ["java", "-jar", "/stanbic.jar"]