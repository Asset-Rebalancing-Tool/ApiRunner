FROM gradle:jdk17

WORKDIR /app

COPY . ./

RUN gradle bootJar

Entrypoint ["java", "-jar", "build/libs/Runner.jar"]