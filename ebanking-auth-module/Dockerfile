FROM openjdk:17-jdk-slim
WORKDIR /opt
ENV PORT 8081
EXPOSE 8081
COPY ./target/*.jar /opt/ebanking-auth-module.jar
ENTRYPOINT exec java $JAVA_OPTS -jar ebanking-auth-module.jar
