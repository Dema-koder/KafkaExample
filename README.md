# Система мониторинга с использованием Spring Kafka

## Описание проекта

Этот проект представляет собой систему мониторинга, которая отслеживает работу различных компонентов приложения с помощью Spring Kafka. Система состоит из двух микросервисов: Producer (отправитель метрик) и Consumer (приемник и анализатор метрик). Микросервисы взаимодействуют через Kafka, где метрики передаются через топик `metrics-topic`.

### Цель проекта

Цель проекта — разработать систему, которая:
- Собирает метрики работы приложения (производительность, использование ресурсов, ошибки и т.д.) с помощью Producer и отправляет их в Kafka.
- Получает и анализирует метрики через Consumer.
- Позволяет пользователю просматривать собранные метрики через REST API.

## Архитектура системы

### Producer Service

**Микросервис "Metrics Producer"** собирает метрики работы приложения и отправляет их в Kafka топик `metrics-topic`. Метрики могут включать информацию о производительности, использовании ресурсов и ошибках.

API, предоставляемый Metrics Producer:

- **POST /metrics**: Отправляет метрики в формате JSON в Kafka. Пример метрик:
  ```json
  {
      "cpuUsage": "75%",
      "memoryUsage": "1.5GB",
      "errorCount": 5,
      "timestamp": "2024-09-01T12:00:00Z"
  }
  ```

### Consumer Service

**Микросервис "Metrics Consumer"** получает метрики из топика Kafka `metrics-topic`, анализирует их и сохраняет в базе данных MongoDB для последующего анализа и визуализации.

API, предоставляемый Metrics Consumer:

- **GET /metrics**: Возвращает список всех метрик.
- **GET /metrics/{id}**: Возвращает конкретную метрику по её ID.

## Структура проекта

```
.
├── consumer                    # Микросервис "Metrics Consumer"
│   ├── pom.xml                 # Maven зависимости для Consumer
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com.example.KafkaExample
│   │   │   │       ├── KafkaConsumerApplication.java     # Точка входа Consumer
│   │   │   │       ├── api
│   │   │   │       │   └── MetricsController.java        # REST API для просмотра метрик
│   │   │   │       ├── config
│   │   │   │       │   └── KafkaConsumerConfig.java      # Конфигурация Kafka Consumer
│   │   │   │       └── service
│   │   │   │           ├── KafkaConsumerListener.java    # Логика получения сообщений из Kafka
│   │   │   │           └── MongoDBService.java           # Логика сохранения данных в MongoDB
├── producer                    # Микросервис "Metrics Producer"
│   ├── pom.xml                 # Maven зависимости для Producer
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com.example.KafkaExample
│   │   │   │       ├── KafkaProducerApplication.java     # Точка входа Producer
│   │   │   │       ├── config
│   │   │   │       │   └── KafkaProducerConfig.java      # Конфигурация Kafka Producer
│   │   │   │       └── controller
│   │   │   │           └── MetricsController.java        # API для отправки метрик
├── docker-compose.yml           # Docker-compose для запуска Kafka, Zookeeper и MongoDB
├── pom.xml                      # Общий POM-файл
```

## Конфигурация и запуск проекта

### Требования

- Docker и Docker Compose для запуска сервисов Kafka, Zookeeper и MongoDB.
- JDK 11 или выше для сборки и запуска микросервисов.
- Maven для управления зависимостями и сборки.

### Шаги для запуска

1. **Запуск Kafka и MongoDB с помощью Docker Compose:**
   Выполните следующую команду для поднятия необходимых сервисов:

   ```bash
   docker-compose up
   ```

   Это запустит Kafka, Zookeeper и MongoDB.

2. **Запуск микросервисов Producer и Consumer:**

   Откройте два терминала, чтобы запустить оба микросервиса параллельно.

   Для Producer:
   ```bash
   cd producer
   mvn spring-boot:run
   ```

   Для Consumer:
   ```bash
   cd consumer
   mvn spring-boot:run
   ```

### Пример использования

1. **Отправка метрики через Producer:**
   Выполните следующий запрос:

   ```bash
   curl -X POST http://localhost:8080/metrics -H "Content-Type: application/json" -d '{
       "cpuUsage": "85%",
       "memoryUsage": "1.8GB",
       "errorCount": 3,
       "timestamp": "2024-09-02T12:30:00Z"
   }'
   ```

2. **Получение метрик через Consumer API:**

   Все метрики:
   ```bash
   curl http://localhost:8079/metrics
   ```

   Конкретная метрика:
   ```bash
   curl http://localhost:8079/metrics/{id}
   ```

## Конфигурация Kafka и MongoDB

### Kafka

В файле `docker-compose.yml` настроены сервисы Kafka и Zookeeper для межсервисного взаимодействия через топик `metrics-topic`. В Producer и Consumer используются следующие настройки:

- **Топик**: `metrics-topic`
- **Порты Kafka**: `9092`
- **Порт Zookeeper**: `2181`

### MongoDB

MongoDB используется для хранения метрик, полученных Consumer-ом. Конфигурация базы данных указана в файле `application.properties`:

```properties
mongodb://root:example@localhost:27017/kafka?authSource=admin
```

## Заключение

Этот проект демонстрирует базовую архитектуру системы мониторинга на основе Kafka. С помощью двух микросервисов (Producer и Consumer) метрики собираются, отправляются и сохраняются для последующего анализа.