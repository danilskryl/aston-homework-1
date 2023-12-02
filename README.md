# REST сервис на основе Java Servlets и JDBC

Проект представляет собой простой REST сервис, реализованный с использованием Java Servlets и JDBC для выполнения основных CRUD операций. Проект соблюдает принципы ООП и SOLID, использует Lombok для упрощения кода и обеспечивает покрытие тестами более чем на 80%.

## Технологии

- Java 17
- [Jakarta Servlet API](https://eclipse-ee4j.github.io/servlet/) (6.0.0)
- MySQL
- [Lombok](https://projectlombok.org/) (1.18.30)
- [JUnit Jupiter](https://junit.org/junit5/docs/current/user-guide/) (5.10.0)
- [Jackson Databind](https://github.com/FasterXML/jackson) (2.16.0)
- [Jackson Datatype JSR310](https://github.com/FasterXML/jackson-modules-java8) (2.16.0)
- [HikariCP](https://github.com/brettwooldridge/HikariCP) (5.1.0)
- [Mockito](https://site.mockito.org/) (5.3.1)
- [JUnit Jupiter Params](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests) (5.9.3)
- [Mockito JUnit Jupiter](https://site.mockito.org/) (5.3.1)
- [Typesafe Config](https://github.com/lightbend/config) (1.4.3)

## Структура проекта

Проект разделен на следующие слои:

- **Servlets**: Обрабатывает HTTP запросы и возвращает DTO.
- **Service**: Содержит бизнес-логику, взаимодействует с репозиторием и маппером.
- **DAO**: Отвечает за взаимодействие с базой данных через JDBC.
- **Mapper**: Преобразует Entity в DTO и наоборот.
- **DTO**: Объекты передачи данных для взаимодействия с клиентом.
- **Entity**: Сущности, представляющие объекты в базе данных.

## Зависимости

- Java 17
- MySQL
- Lombok
- [JUnit Jupiter](https://junit.org/junit5/docs/current/user-guide/) (5.10.0)
- [Jakarta Servlet API](https://eclipse-ee4j.github.io/servlet/) (6.0.0)
- [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) (8.0.33)
- [Jackson Databind](https://github.com/FasterXML/jackson) (2.16.0)
- [Jackson Datatype JSR310](https://github.com/FasterXML/jackson-modules-java8) (2.16.0)
- [HikariCP](https://github.com/brettwooldridge/HikariCP) (5.1.0)
- [Mockito](https://site.mockito.org/) (5.3.1)
- [JUnit Jupiter Params](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests) (5.9.3)
- [Mockito JUnit Jupiter](https://site.mockito.org/) (5.3.1)
- [Typesafe Config](https://github.com/lightbend/config) (1.4.3)

## Запуск проекта

1. Склонировать репозиторий:

    ```bash
    git clone https://github.com/danilskryl/aston-homework-1.git
    ```

2. Настроить подключение к базе данных в файле `src/main/resources/application.conf`.

3. Собрать проект:

    ```bash
    ./mvnw clean package
    ```

4. Запустить проект:

    ```bash
    java -jar target/aston-homework-1-1.0.jar
    ```

## API Endpoints

### Markets

#### Получить все рынки

```http
GET /api/v1/markets
```

#### Получить информацию о конкретном рынке:
```http
GET /api/v1/markets/{id}
```

#### Создать новый рынок
```http
POST /api/v1/markets
```

#### Обновить информацию о рынке

```http
PUT /api/v1/markets
```

#### Удалить рынок

```http
DELETE /api/v1/markets/{id}
```

### Product

#### Получить все продукты

```http
GET /api/v1/products
```

#### Получить информацию о конкретном продукте:
```http
GET /api/v1/products/{id}
```

#### Создать новый продукт
```http
POST /api/v1/products
```

#### Обновить информацию о продукте

```http
PUT /api/v1/products
```

#### Удалить продукт

```http
DELETE /api/v1/products/{id}
```

### Order
#### Получить все заказы

```http
GET /api/v1/products
```

#### Получить информацию о конкретном заказе:
```http
GET /api/v1/products/{id}
```

#### Создать новый заказ
```http
POST /api/v1/products
```

#### Обновить информацию о заказе

```http
PUT /api/v1/products
```

#### Удалить заказ

```http
DELETE /api/v1/products/{id}
```
