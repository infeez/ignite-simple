FROM anapsix/alpine-java:8_server-jre

RUN apk add --update bash && rm -rf /var/cache/apk/*

RUN mkdir -p /opt/ignite-simple

COPY target/ignite-simple.jar /opt/ignite-simple

COPY start.sh /opt/ignite-simple
RUN chmod +x /opt/ignite-simple/start.sh

ENV IGNITE_SERVICE_PORT=47500..47520
ENV SERVER_ADDRESS=0.0.0.0
ENV MEM_MIN="1g"
ENV MEM_MAX="5g"

CMD /opt/ignite-simple/start.sh