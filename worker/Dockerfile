#
# Set a variable that can be used in all stages.
#
ARG BUILD_HOME=/gradle-docker-example

#
# Gradle image for the build stage.
#
FROM gradle:8.5.0-jdk21 as build-image

#
# Set the working directory.
#
ARG BUILD_HOME
ENV APP_HOME=$BUILD_HOME
WORKDIR $APP_HOME

#
# Copy the Gradle config, source code, and static analysis config
# into the build container.
#
COPY build.gradle.kts settings.gradle.kts $APP_HOME/
COPY src $APP_HOME/src

#
# Build the application.
#
RUN gradle build --parallel -x test

#
# Java image for the application to run in.
#
FROM eclipse-temurin:21-jdk-alpine

#
# Copy the jar file in and name it app.jar.
#
ARG BUILD_HOME
ENV APP_HOME=$BUILD_HOME
WORKDIR /opt/app
COPY --from=build-image $APP_HOME/build/libs/*.jar app.jar

#
# The command to run when the container starts.
#
EXPOSE 80
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=80"]