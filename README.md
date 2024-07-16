# Система Анализа Метрик

## Описание архитектуры проекта

Проект представляет собой систему для сбора, обработки и анализа метрик с использованием Apache Kafka, Spring Boot и PostgreSQL. В нем используется микросервисная архитектура для обеспечения масштабируемости и гибкости системы.

### Компоненты системы
#### 1. Producer Service
- Описание: Сервис, который периодически собирает метрики с различных источников (например, через Spring Actuator), сериализует их в формат JSON и отправляет в Kafka.
- Технологии: Java, Spring Boot, Kafka, REST API.
- Функции:
  - Сбор метрик с использованием Spring Actuator.
  - Отправка метрик в Kafka для дальнейшей обработки и анализа.

#### 2. Consumer Service
- Описание: Сервис, который потребляет метрики из Kafka, обрабатывает их и сохраняет в базу данных PostgreSQL для последующего анализа.
- Технологии: Java, Spring Boot, Kafka, PostgreSQL, JPA.
- Функции:
  - Потребление метрик из Kafka.
  - Сохранение метрик в базу данных PostgreSQL.
  - Реализация функциональности для анализа метрик (например, расчет среднего значения и предсказание значений).

## Требования к запуску проекта
- [Java 21]
- [Maven]
- [Docker]

## Инструкция по сборке
### Запуск инфраструктуры
Создает контейнеры с postgreSQL и Apache Kafka.
Для запуска требуются порты 5432, 9091, 2181
```bash
docker-compose -f ./infrastructure/docker-compose.yml up -d
```

### Запуск Consumer service

Для запуска требуется порт 8080
```bash
mvn clean package -f ./consumer-service
mvn spring-boot:run -f ./consumer-service
```
### Запуск Producer service

Для запуска требуется порт 8081
```bash
mvn clean package -f ./producer-service
mvn spring-boot:run -f ./producer-service
```


## Конфигурация Kafka

#### Топики:

- metrics-topic: Основной топик, в который продюсер отправляет метрики и из которого консьюмер их читает. 
  - Количество разделов (partitions): 3 
  - Фактор репликации (replication factor): 1

#### Параметры продюсера
- Bootstrap Servers: localhost:9092. 
- Key Serializer: Сериализатор для ключей сообщений. Используется StringSerializer. 
- Value Serializer: Сериализатор для значений сообщений. Используется JsonSerializer.

#### Параметры консьюмера
- Bootstrap Servers: localhost:9092.
- Key Deserializer: Десериализатор для ключей сообщений. Используется StringDeserializer.
- Value Deserializer: Десериализатор для значений сообщений. Используется JsonDeserializer.
- Trusted Packages: Пакеты, которым доверяет десериализатор. В данном случае настроено на все пакеты (openschool.java.kafka.dto).

## Swagger документация

### Consumer-service
```
http://localhost:8080/swagger-ui/index.html
```

### Producer-service
```
http://localhost:8081/swagger-ui/index.html
```

[Java 21]: https://www.oracle.com/java/technologies/downloads/?er=221886
[Maven]: https://maven.apache.org/install.html
[Docker]: https://www.docker.com/products/docker-desktop/