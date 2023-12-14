# syntax=docker/dockerfile:1

FROM openjdk:11
WORKDIR /app

COPY . .

CMD ./gradlew run

EXPOSE 8088

