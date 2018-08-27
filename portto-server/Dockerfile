FROM busybox

ADD target/ /tmp/

WORKDIR /tmp/
RUN mkdir /tmp/app/

RUN unzip *-exec.jar -d /tmp/app/
#move libs to external folder for caching
RUN mv /tmp/app/BOOT-INF/lib /tmp/lib




###############################################################
FROM openjdk:8-jdk-slim

RUN mkdir /deployments/


#cache libs layer
COPY --from=0  /tmp/lib /deployments/BOOT-INF/lib
COPY --from=0  /tmp/app /deployments


WORKDIR /deployments/


CMD ["java", \
 "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap","-XshowSettings:vm", \
 "-XX:+ExitOnOutOfMemoryError","-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=/tmp","-XX:ErrorFile=/tmp/jdk.error", \
 "-XX:MaxHeapFreeRatio=40","-XX:MinHeapFreeRatio=20", \
 "-Djava.security.egd=file:/dev/./urandom", \
 "-Dspring.profiles.active=production", \
 "-classpath", ".",\
 "org.springframework.boot.loader.JarLauncher"]