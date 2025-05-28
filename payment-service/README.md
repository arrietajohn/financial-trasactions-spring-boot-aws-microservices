# payment-service

## Payment Service

## Payment Service
Microservicio responsable de procesar pagos entre cuentas. Valida los montos, calcula impuestos y comisiones, y se comunica de forma síncrona con el microservicio de gestión de cuentas y transferencias (`account-service`) para ejecutar las operaciones. Además, publica mensajes en una cola para que el microservicio notificador (`notification-service`) informe al cliente sobre el resultado del pago.


## Tecnologías usadas

- Java 17
- Spring Boot 3.2.x
- Maven
- H2 Database (desarrollo) / MySQL (producción local)
- JPA
- MapStruct
- Docker (fase final)

## Endpoints disponibles

- `POST /accounts` → Registrar cuenta (próximo)
- `POST /accounts/transfer` → Realiza transferencia entre usuarios

## Arquitectura

Este microservicio sigue el patrón **Hexagonal Architecture**, dividido en:

- `domain/`: entidades, excepciones, puertos
- `application/`: casos de uso, servicios, DTOs
- `adapter/in/rest`: entrada vía REST
- `adapter/out/persistence`: salida a base de datos

## Modo de ejecución local

```bash
mvn spring-boot:run
