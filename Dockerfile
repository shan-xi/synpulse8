FROM openjdk:17-jdk-slim
WORKDIR /opt
ENV PORT 8080
EXPOSE 8080
COPY ./target/*.jar /opt/ebanking.jar
ENTRYPOINT exec java $JAVA_OPTS -jar ebanking.jar
