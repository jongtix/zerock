#!/usr/bin/env bash

PROJECT_HOME=/home/ec2-user
REPOSITORY=$PROJECT_HOME/zerock
PROJECT_NAME=Zerock
DOCKER_COMPOSE_NAME=docker-compose.yml

TIME_STAMP=$(date +%Y%m%d%H%M%s)

echo ">>> 기존 Build 파일 백업"
echo ">>> cp -rp $REPOSITORY/jar/$PROJECT_NAME.jar $REPOSITORY/jar/backup/$PROJECT_NAME.$TIME_STAMP.jar"

if [ ! -d $REPOSITORY/jar/backup ]; then
  mkdir $REPOSITORY/jar/backup
fi

cp -rp $REPOSITORY/jar/$PROJECT_NAME.jar $REPOSITORY/jar/backup/$PROJECT_NAME.$TIME_STAMP.jar

echo ">>> Build 파일 복사"
echo ">>> cp $REPOSITORY/zip/*.jar $REPOSITORY/jar/$PROJECT_NAME.jar"

cp -rp $REPOSITORY/zip/*.jar $REPOSITORY/jar/$PROJECT_NAME.jar

echo ">>> Directory 이동"
echo ">>> cd $REPOSITORY"

cd $REPOSITORY

echo ">>> docker-compose 파일 복사"
echo ">>> cp $REPOSITORY/zip/$DOCKER_COMPOSE_NAME $REPOSITORY/$DOCKER_COMPOSE_NAME"

cp $REPOSITORY/zip/$DOCKER_COMPOSE_NAME $REPOSITORY/$DOCKER_COMPOSE_NAME

echo ">>> 도커 컴포즈 다운"
echo ">>> docker-compose down"

docker-compose down

echo ">>> 도커 컴포즈 업"
echo ">>> docker-compose up -d"

docker-compose up -d
