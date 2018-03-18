#!/usr/bin/env bash

java -Xms${MEM_MIN} -Xmx${MEM_MAX} \
                -agentlib:jdwp=transport=dt_socket,address=8787,server=y,suspend=n \
                -server -XX:+AggressiveOpts \
                -jar /opt/ignite-simple/ignite-simple.jar \
                > /opt/ignite-simple/log.log 2>&1