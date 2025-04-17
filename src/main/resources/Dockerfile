FROM eclipse-temurin:21-jdk
COPY build/libs/dash-ws.jar dash-ws.jar
ENTRYPOINT ["java", "-Dlog4j.configurationFile=log4j2.xml", "-cp", "dash-ws.jar", "org.dash.Main"]