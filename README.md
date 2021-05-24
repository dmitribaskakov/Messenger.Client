# Messenger.Client



##Команды для докера:
Сборка docker image
```
docker build -t messenger-client:1.0 .
```
Запуск docker image
```
docker run --rm --name client --publish 19000:19000 --env TZ=Asia/Novosibirsk messenger-client:1.0
```