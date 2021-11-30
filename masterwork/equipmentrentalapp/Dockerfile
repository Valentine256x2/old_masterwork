FROM openjdk:8-jdk-alpine
EXPOSE 8080
ARG JAR_FILE=build/libs/equipmentrentalapp-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} equipmentrentalapp.jar
ENTRYPOINT ["java","-jar","/equipmentrentalapp.jar"]
