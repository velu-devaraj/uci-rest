FROM openjdk:23-jdk-slim
LABEL com.yam.chess.image.authors="author@example.com"
COPY target/uci-api-0.0.1-SNAPSHOT.jar app/uci-api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","app/uci-api-0.0.1-SNAPSHOT.jar"]
CMD ["--spring.config.location=.\config\application.properties"]