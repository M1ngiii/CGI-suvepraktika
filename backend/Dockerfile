FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

CMD sh -c "java -jar $(find target -type f -name '*.jar' | head -n 1)"

EXPOSE 8080