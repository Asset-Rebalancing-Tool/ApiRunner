FROM gradle:jdk17

ARG MAVEN_USER
ARG MAVEN_PASSWORD

ENV MAVEN_USER ${MAVEN_USER}
ENV MAVEN_PASSWORD ${MAVEN_PASSWORD}

RUN echo "${MAVEN_USER}"

WORKDIR /app

COPY . ./

RUN gradle bootJar

Entrypoint ["java", "-jar", "build/libs/Runner.jar"]