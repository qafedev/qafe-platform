FROM java

MAINTAINER kboekhout@qafe.com

EXPOSE 8080

ENV JETTY_VERSION 9.2.2.v20140723

RUN wget http://download.eclipse.org/jetty/${JETTY_VERSION}/dist/jetty-distribution-${JETTY_VERSION}.zip -O /tmp/jetty.zip

# Unpack
RUN cd /opt && jar xf /tmp/jetty.zip
RUN ln -s /opt/jetty-distribution-${JETTY_VERSION} /opt/jetty
RUN rm /tmp/jetty.zip

ENV JETTY_HOME /opt/jetty
ENV PATH $PATH:$JETTY_HOME/bin

RUN chmod +x /opt/jetty/bin/jetty.sh

ADD qafe-webapps/qafe-gwt-generic-webapp/target/qafe-gwt-generic-webapp-3.2.0-SNAPSHOT.war /opt/jetty/webapps/

CMD [ "/opt/jetty/bin/jetty.sh", "run" ]
