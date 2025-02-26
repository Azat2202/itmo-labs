FROM eclipse-temurin:17

WORKDIR /app

COPY . .

RUN chmod +x ./mvnw

RUN ./mvnw package -DskipTests

ENTRYPOINT java -jar ./target/server.jar