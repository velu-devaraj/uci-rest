FROM ubuntu:23.10 AS builder
FROM openjdk:23-jdk-slim
# To build the libs
# RUN apt-get update && apt-get install -y libc6 libstdc++6
LABEL com.yam.chess.image.authors="author@example.com"


# Copy required libraries
COPY --from=builder /lib/x86_64-linux-gnu/libz.so.1 /lib/x86_64-linux-gnu/
COPY --from=builder /lib/x86_64-linux-gnu/libstdc++.so.6 /lib/x86_64-linux-gnu/
COPY --from=builder /lib/x86_64-linux-gnu/libm.so.6 /lib/x86_64-linux-gnu/
COPY --from=builder /lib/x86_64-linux-gnu/libgcc_s.so.1 /lib/x86_64-linux-gnu/
COPY --from=builder /lib/x86_64-linux-gnu/libc.so.6 /lib/x86_64-linux-gnu/
COPY --from=builder /lib64/ld-linux-x86-64.so.2 /lib64/


COPY target/uci-api-0.0.1-SNAPSHOT.jar app/uci-api-0.0.1-SNAPSHOT.jar

COPY engine engine


ENTRYPOINT ["java","-jar","app/uci-api-0.0.1-SNAPSHOT.jar"]
CMD ["--spring.config.location=./config/application.properties"]

