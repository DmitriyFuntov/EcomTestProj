FROM apache/spark:3.5.1

WORKDIR /app

COPY target/testproj-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["/opt/spark/bin/spark-submit"]
CMD ["--master", "local[*]", "/app/app.jar"]
