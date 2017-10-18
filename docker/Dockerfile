FROM java:8u91-jdk
MAINTAINER Alfonso Marin Lopez <info@alfonsomarin.es>
VOLUME /tmp
# Important becasuse the app use relative paths and it has to be start inside this folder
WORKDIR /home/finances/
ADD target/finances-release.tar .
ENV JAVA_OPTS="-Dspring.config.location=./config/"
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar backend.jar
CMD ["/bin/bash"]
EXPOSE 8090:8090