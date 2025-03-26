
# syntax=docker/dockerfile:1

FROM xrpllabsofficial/xrpld:2.4.0

CMD ["-a", "--start"]

# RPC Public
EXPOSE 7001:51234
# RPC Admin Local
EXPOSE 5006:5005
 # WS Local
EXPOSE 8001:80
# WS Admin Local
EXPOSE 4001:6006
 # Peer Port (Not Used for Standalone)
EXPOSE 9001:51235

COPY ./config /config/config

#services:
#  standalone:
#    platform: linux/amd64
#    container_name: standalone
#    image: "xrpllabsofficial/xrpld:2.4.0"
#    command: '-a --start '
#    ports:
#
#
#    volumes:
#      - ./config:/config/
