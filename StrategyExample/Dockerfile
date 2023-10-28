FROM openjdk:11-jre-slim-buster
EXPOSE 8095
RUN mkdir -p /app/
ADD build/libs/StrategyExample-0.0.1-SNAPSHOT.jar /app/StrategyExample-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app/StrategyExample-0.0.1-SNAPSHOT.jar"]
