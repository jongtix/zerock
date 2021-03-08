#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile_docker.sh

IDLE_PORT=48081

echo ">>> Health Check Start!"
echo ">>> IDLE_PORT: $IDLE_PORT"
echo ">>> curl -s http://localhost:$IDLE_PORT/profile"
curl -s http://localhost:$IDLE_PORT/profile
sleep 10

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl -s http://localhost:${IDLE_PORT}/profile)
  UP_COUNT=$(echo ${RESPONSE} | grep 'real' | wc -l)

  if [ ${UP_COUNT} -ge 1 ]
  then  #UP_COUNT >= 1 ("real" 문자열이 있는지 검증)
    echo ">>> Health Check 성공"
    break
  else
    echo ">>> Health Check의 응답을 알 수 없거나 실행 상태가 아닙니다."
    echo ">>> Health Check: ${RESPONSE}"
  fi

  if [ ${RETRY_COUNT} -eq 10 ]
  then
    echo ">>> Health Check 실패"
    echo ">>> 엔진엑스에 연결하지 않고 배포를 종료합니다."
    exit 1
  fi

  echo ">>> Health Check 연결 실패. 재시도..."
  sleep 5
done

IDLE_PORT=48082

echo ">>> Health Check Start!"
echo ">>> IDLE_PORT: $IDLE_PORT"
echo ">>> curl -s http://localhost:$IDLE_PORT/profile"
curl -s http://localhost:$IDLE_PORT/profile
sleep 10

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl -s http://localhost:${IDLE_PORT}/profile)
  UP_COUNT=$(echo ${RESPONSE} | grep 'real' | wc -l)

  if [ ${UP_COUNT} -ge 1 ]
  then  #UP_COUNT >= 1 ("real" 문자열이 있는지 검증)
    echo ">>> Health Check 성공"
    break
  else
    echo ">>> Health Check의 응답을 알 수 없거나 실행 상태가 아닙니다."
    echo ">>> Health Check: ${RESPONSE}"
  fi

  if [ ${RETRY_COUNT} -eq 10 ]
  then
    echo ">>> Health Check 실패"
    echo ">>> 엔진엑스에 연결하지 않고 배포를 종료합니다."
    exit 1
  fi

  echo ">>> Health Check 연결 실패. 재시도..."
  sleep 5
done