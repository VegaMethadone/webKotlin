FROM ubuntu:20.04 as build

RUN apt-get clean && \
    rm -rf /var/lib/apt/lists/* && \
    apt-get update
RUN apt-get install -y openjdk-8-jdk
RUN apt-get install -y axel
RUN apt-get install -y unzip

ENV JUDDI_VERSION="3.3.10"

WORKDIR /juddi/

RUN axel -n 150 https://archive.apache.org/dist/juddi/juddi/${JUDDI_VERSION}/juddi-distro-${JUDDI_VERSION}.zip
RUN unzip juddi-distro-${JUDDI_VERSION}.zip


FROM build as target

WORKDIR /juddi/

ARG USERNAME=${USERNAME:-juddi}
ARG PASSWORD=${PASSWORD:-juddi}

ENV PRG_PATH="/juddi/juddi-distro-${JUDDI_VERSION}/juddi-tomcat-${JUDDI_VERSION}"

ENV USERS_CONF_NAME="tomcat-users.xml"
ENV USERS_CONF_PATH="${PRG_PATH}/conf/${USERS_CONF_NAME}"
COPY ./juddi/${USERS_CONF_NAME} ${USERS_CONF_PATH}

ENV SERVER_CONF_NAME="server.xml"
ENV SERVER_CONF_PATH="${PRG_PATH}/conf/${SERVER_CONF_NAME}"
COPY ./juddi/${SERVER_CONF_NAME} ${SERVER_CONF_PATH}

ENV USERNAME=$USERNAME
ENV PASSWORD=$PASSWORD
RUN sed -i "s/{{ USERNAME }}/${USERNAME}/" ${USERS_CONF_PATH}
RUN sed -i "s/{{ PASSWORD }}/${PASSWORD}/" ${USERS_CONF_PATH}

# https://stackoverflow.com/questions/23011547/webservice-client-generation-error-with-jdk8
RUN echo "javax.xml.accessExternalDTD = all" > /usr/lib/jvm/java-8-openjdk-amd64/jre/lib/jaxp.properties

EXPOSE 8080

CMD ${PRG_PATH}/bin/catalina.sh run