FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/micromorph-1.0.0.jar /app/micromorph-1.0.0.jar

CMD ["java", "-jar", "micromorph-1.0.0.jar"]