FROM openjdk:latest
COPY ./target/group10sem-0.1.0.3-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "group10sem-0.1.0.3-jar-with-dependencies.jar"]