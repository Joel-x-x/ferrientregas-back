FROM eclipse-temurin:21.0.3_9-jdk

EXPOSE 8080

WORKDIR /root

COPY ./build/libs/*.jar /root/app.jar

ENTRYPOINT ["java", "-jar", "/root/app.jar"]