FROM openjdk:11
ADD target/qr-code-generator-rest.jar qr-code-generator-rest.jar
EXPOSE 8080
Entrypoint ["java", "-jar", "qr-code-generator-rest.jar"]