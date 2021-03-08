#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
  IDLE_PORT=$(find_idle_port)

  echo ">>> 전환할 Port: $IDLE_PORT"
  echo ">>> Port 전환"
  echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc  #echo 'set \$service_url http://127.0.0.1:${IDLE_PORT};': 하나의 문장을 만들어 파이프라인(|)으로 넘겨주기 위해 echo 사용
                                                                                                        #                                                         nginx가 변경할 프록시 주소를 생성
                                                                                                        #sudo tee /etc/nginx/conf.d/service-url.inc: 앞에서 넘겨준 문장을 service-url.inc에 덮어씀
  echo ">>> nginx Reload"
  sudo service nginx reload
}