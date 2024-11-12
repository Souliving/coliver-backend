FROM bellsoft/liberica-openjdk-alpine:21.0.5-11-x86_64
ARG JAR_FILE=build/libs/coliver-backend-all.jar
COPY ${JAR_FILE} coliver-backend-all.jar
ENTRYPOINT ["java", "-Xms64m", "-Xmx1024m", "-XX:+UseSerialGC", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseContainerSupport", "-Djava.security.egd=file:/dev/./urandom", "-jar","coliver-backend-all.jar"]
