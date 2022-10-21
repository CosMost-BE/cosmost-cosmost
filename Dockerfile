FROM openjdk:17-ea-11-jdk-slim
COPY build/libs/cosmost-1.0.jar CosmostService.jar
ENTRYPOINT ["java","-jar","app.jar"]