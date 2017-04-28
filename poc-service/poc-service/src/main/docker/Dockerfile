FROM azul/zulu-openjdk:8

VOLUME /tmp

# Update Ubuntu
RUN \
  bash -c 'apt-get -qq update && apt-get -y upgrade && apt-get -y autoclean && apt-get -y autoremove' && \
  bash -c 'DEBIAN_FRONTEND=noninteractive apt-get install -y curl wget tar'

ENV USER_NAME poccieloapi
ENV APP_HOME /opt/demo/$USER_NAME

RUN \
  useradd -ms /bin/bash $USER_NAME && \
  mkdir -p $APP_HOME

ADD poc-cielo-api.jar ${APP_HOME}/poc-cielo-api.jar
RUN \
  chown $USER_NAME $APP_HOME/poc-cielo-api.jar && \
  bash -c 'touch ${APP_HOME}/poc-cielo-api.jar'

ENV JAVA_TOOL_OPTIONS "-Xms128M -Xmx128M -Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom"

USER $USER_NAME
WORKDIR $APP_HOME
ENTRYPOINT ["java", "-jar", "poc-cielo-api.jar"]