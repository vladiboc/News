# Проект News<br><font size='5em'>(REST API бэкенда новостного сервиса)</font>
## Описание проекта
Проект представляет собой вэб-приложение, использующее стартеры:
- Spring Boot Web - для бэкенда
- Spring Boot Data JPA - для соединения с СУБД PostgreSQL
- Spring Doc OpenApi - для документирования API приложения

Приложение позволяет:
- создавать пользователей и управлять ими
- создавать категории новостей и управлять ими
- создавать новости и управлять ими
- создавать комментарии для новостей и управлять ими

## Стек технологий
- Java 17
- SpringFramework Boot 3.2.3
- Gradle 8.7
- Docker 24.0.7
- Docker Compose 2.20.2
- PostgreSQL 12.3

## Инструкция по локальному запуску приложения

Приложение News запускается как JAR-файл и взаимодействует с предварительно запущенной БД PostgreSQL.
Последовательность шагов для запуска приложения:

**I Запуск СУБД PostgreSQL**  
1. Создайте на локальной машине директорию для запуска СУБД PostgreSQL. Скачайте туда следующие всё содержимое папки docker_db/ из корня репозитория проекта:
    - **docker-compose.yml** - файл докер-композиции запуска СУБД PostgreSQL
    - **init.sql/** - директория с SQL-скриптами инициализации БД приложения, в том числе:
    - **init.sql/00_role_db.sql** - создание пользователя, установка пароля и прав на БД
    - **init.sql/01_schema.sql** - создание в БД схемы для приложения
2. Убедитесь, чно на локальной машине установлены Docker и Docker Compose, перейдите в директорию запуска СУБД PostgreSQL, и запустите композицию СУБД PostgreSQL командой:  
```$ docker compose up -d```  
СУБД PostgreSQL запустится на локальной машине и будет слушать запросы на порту 5432, как указано в файле композиции.  

**II Запуск приложения**  
1. Убедитесь, что на локальной машине установлена Java 17.  
Создайте на локальной машине директорию запуска приложения, и скачайте в эту директорию следующие файлы из репозитория проекта:
    - **news.jar.zip** - зазипованный JAR-файл запуска приложения, из корня репозитория
    - **application.yaml** - файл настроек приложения, из директории src/main/resources/ репозитория  
2. В той же директории запуска разархивируйте скачанный zip-файл, например так:  
   ``` unzip news.jar.zip```
3. Затем запустите приложение, в той же директории запуска выполните следующую команду:  
    ``` java -jar webcontacts-0.0.1.jar ```  
    Приложение запустится и его API будет доступен из браузера по URL http://localhost:8080/swagger  

**III Настройки приложения**  

Настройки приложения задаются в файле **application.yaml**, скачанном из корня репозитория в директорию запуска приложения. Доступны для изменения следующие настройки:  
- порт запуска приложения - по умолчанию 8080
- уровень логирования приложения - по умолчанию INFO
- параметры подключения к СУБД  

## Работа приложения
Все возможности приложения описаны в его API.  
Документация к API запуска будет открываться по адресу:  
http://localhost:8080/swagger  