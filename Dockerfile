FROM openjdk:24

ADD build/libs/*.jar app.jar

ADD data/*.xlsx data/product_table.xlsx

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

