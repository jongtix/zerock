#!/usr/bin/env bash

#쉬고 있는 profile 찾기: real1이 사용 중이면 real2가 쉬고 있고, 반대면 real1이 쉬고 있음

function find_idle_profile() {
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)  #현재 nginx가 바라보고 있는 스프링 부트가 정상적으로 수행 중인지 확인 -> 응답값을 HttpStatus로 받음
  if [ ${RESPONSE_CODE} -ge 400 ] #정상일 때 200, 오류가 발생하면 400 ~ 503 사이로 발생
  then
    CURRENT_PROFILE=real2
  else
    CURRENT_PROFILE=$(curl -s http://localhost/profile)
  fi

  if [ ${CURRENT_PROFILE} == real1 ]
  then
    IDLE_PROFILE=real2
  else
    IDLE_PROFILE=real1
  fi

  echo "${IDLE_PROFILE}"  #bash script는 값을 반환하는 기능이 없기 때문에 마지막 줄에 echo로 결과를 출력한 후 find_idle_port()에서 값을 잡아서 사용(중간에 echo를 사용해서는 안 됨)
}

#쉬고 있는 profile의 port 찾기
function find_idle_port() {
    IDLE_PROFILE=$(find_idle_profile)

    if [ ${IDLE_PROFILE} == real1 ]
    then
      echo "18081"
    else
      echo "18082"
    fi
}