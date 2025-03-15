FROM openjdk:17-alpine AS builder

WORKDIR /app
COPY ./ .

RUN chmod +x gradlew
RUN ./gradlew clean build -PisDockerBuild=true

FROM openjdk:17-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/server.jar /app/server.jar
EXPOSE 6500

ENTRYPOINT ["java", "-jar", "server.jar"]