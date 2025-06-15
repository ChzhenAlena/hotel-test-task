# Hotel REST API

Приложение — RESTful API для управления отелями.
Ссылка на ТЗ: https://docs.google.com/document/d/1eYN9hvjNo6Bu-j4PPK5dFJhmfVSqZkjTJVss8iYNT70/edit?usp=sharing

## Основные эндпоинты

- `GET /property-view/hotels`  
  Получение списка всех отелей с краткой информацией.

- `GET /property-view/hotels/{id}`  
  Подробная информация по конкретному отелю.

- `GET /property-view/search`  
  Поиск отелей по параметрам: `name`, `brand`, `city`, `country`, `amenities`.

- `POST /property-view/hotels`  
  Создание нового отеля.

- `POST /property-view/hotels/{id}/amenities`  
  Добавление списка удобств (amenities) к отелю.

- `GET /property-view/histogram/{param}`  
  Получение количества отелей, сгруппированных по значению параметра (`brand`, `city`, `country`, `amenities`).

## Пример запуска

```bash
mvn spring-boot:run
````
Если используете Maven Wrapper (предпочтительно для автопроверки):

```bash
./mvnw spring-boot:run  # для Linux/MacOS
````
```bash
.\mvnw.cmd spring-boot:run  # для Windows
````

Приложение стартует на порту `8092`.

## Технологии

* Java 21
* Spring Boot
* Spring Data JPA
* Liquibase
* H2 Database (встроенная)

## Дополнительно

* Тесты (unit и интеграционные)
* Swagger-документация
* Архитектура с разделением на слои и использованием паттернов проектирования
* Легкая смена базы данных (MySQL, PostgreSQL и др.)

## Примечания
* Для генерации ID используется стратегия IDENTITY для быстрой смены базы (H2, MySQL, PostgreSQL и др.). Из-за этого batch insert работает не полностью, так как полноценная поддержка требует SEQUENCE, что снижает переносимость между СУБД.
