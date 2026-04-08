FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY daily-ritual-lib/pom.xml daily-ritual-lib/pom.xml
COPY daily-ritual-lib/src daily-ritual-lib/src
COPY daily-ritual-scheduler/pom.xml daily-ritual-scheduler/pom.xml
COPY daily-ritual-scheduler/src daily-ritual-scheduler/src

RUN mvn -q -f daily-ritual-lib/pom.xml install -DskipTests
RUN mvn -q -f daily-ritual-scheduler/pom.xml package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /workspace/daily-ritual-scheduler/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
