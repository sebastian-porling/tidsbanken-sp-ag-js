FROM openjdk:14
ADD target/server-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 3400 4121
ENTRYPOINT ["java", "-jar", "/app.jar"]