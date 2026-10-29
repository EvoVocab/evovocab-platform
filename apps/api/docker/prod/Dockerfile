FROM node:24.21.0-alpine AS builder
WORKDIR /app

RUN apk add --no-cache openjdk21 bash

ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk
ENV PATH="$JAVA_HOME/bin:$PATH"

COPY package*.json ./
RUN npm ci --legacy-peer-deps

COPY . .
RUN npx nx build api -c prod

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/apps/api/build/libs/*-SNAPSHOT.jar ./app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]