FROM openjdk:17-jdk

WORKDIR /app

ARG JAR_FILE=onetake/build/libs/onetake-0.0.1.jar

COPY ${JAR_FILE} .

EXPOSE 8080

ENTRYPOINT [ "java","-jar","onetake-0.0.1.jar" ]
