# account-service

Microservicio encargado de registrar cuentas y realizar transferencias entre usuarios.

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
