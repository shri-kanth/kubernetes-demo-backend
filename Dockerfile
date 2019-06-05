FROM openjdk:10-jre-slim
RUN mkdir -p /opt/to-do-app/ 
COPY ./target/to-do-listEntity-app-0.0.1-SNAPSHOT.jar /opt/to-do-app/
WORKDIR /opt/to-do-app/
EXPOSE 8080
CMD ["java", "-jar", "to-do-listEntity-app-0.0.1-SNAPSHOT.jar"]
