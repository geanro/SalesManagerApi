FROM openjdk:25-jdk-slim
ARG JAR_FILE=target/sales_manager_api-0.0.1.jar
COPY ${JAR_FILE} app_saleManagerApi.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app_saleManagerApi.jar"]