FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache sqlite
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY data/init.sql init.sql
COPY data/start.sh start.sh
RUN chmod +x start.sh
EXPOSE 4000
ENTRYPOINT ["./start.sh"]
