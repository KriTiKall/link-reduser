# link-reduser
Проект написан на:
  - java v17;
  - PostgreSQL v12;
  - Maven v3.6.3;

Получилось так, что у меня не вышло накатить swaager 2 с spring boot 2 (может быть среда не всё отображала). По итогу получилась две ветки:
1. Spring boot 2 без swagger, но с basic auth через NameAndPasswordToken(ветка: **master**);
2. Spring boot 3 c swwager 3, но без авторизации(везде придётся передовать пользователя)(ветка: **non_sec**)

Последовательность сборки проекта:
  1. Скачиваем проект на локальный компьютер через команду  git clone: `git clonehttps://github.com/KriTiKall/link-reduser.git`
     1.1. По необходимости меняем ветку(с **master** на **non_sec**);
  3. Создаём базу данный с помощью db.sql(содержит команды для создания бд и таблиц).
  4. Собираем проект через Maven cледующией командой: `mvn clean install`
  5. В проекте сгенерировалась папка **target** в ней находится файл по имени **link.reduce-0.0.1-SNAPSHOT.jar** его необходимо выполнить через команду: `java -jar /target/link.reduce-0.0.1-SNAPSHOT.jar`
  6. Если ветка с swagger переходим на `http://localhost:8080/swagger-ui/index.html`

  
