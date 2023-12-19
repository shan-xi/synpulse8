FROM openjdk:17-jdk-slim
WORKDIR /opt
ENV PORT 8082
EXPOSE 8082
COPY ./target/*.jar /opt/ebanking-transaction-module.jar
ENTRYPOINT exec java $JAVA_OPTS -Dspring.profiles.active=stage -jar ebanking-transaction-module.jar
