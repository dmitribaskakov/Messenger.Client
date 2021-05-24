FROM openjdk:15

RUN mkdir /app

COPY target/Messenger.Client-1.0.jar /app

WORKDIR /app

CMD java -jar Messenger.Client-1.0.jar