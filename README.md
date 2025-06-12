# Simulador Básico de Micropagos

Este proyecto es un **simulador básico de sistemas distribuidos** para la gestión de micropagos, ideal para ambientes de prueba, entrevistas técnicas o prototipos de arquitectura de pagos. La solución está compuesta por microservicios independientes que permiten probar flujos completos de pago, transferencia entre cuentas y notificaciones de resultados.

---

## Arquitectura de Microservicios

- **Microservicio de Cuentas y Transferencias**  
  Gestiona cuentas y realiza transferencias entre ellas.
- **Microservicio de Procesamiento de Pago**  
  Procesa pagos y orquesta la lógica del sistema.
- **Microservicio de Notificaciones**  
  Informa el resultado de los pagos y transferencias al usuario o a otros sistemas.

---

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.5**
- **H2 Database** (persistencia en memoria para pruebas)
- **AWS SQS** (simulado con LocalStack)
- **AWS DynamoDB** (simulado con LocalStack)
- **Docker Compose** (para orquestar el entorno local)

---

## Ejecución del Proyecto

1. **Ubíquese en la carpeta raíz del proyecto**
2. **Comandos principales:**
   - Para detener servicios y limpiar el entorno:  
     ```sh
     docker compose down
     ```
   - Para levantar todos los servicios y dependencias:  
     ```sh
     docker compose up
     ```
3. **Uso de Postman**  
   Importe la colección de endpoints o cree sus propias pruebas para interactuar con los microservicios expuestos.
4. **Monitorear colas y base de datos**
   - Instale el AWS CLI o AWS SDK (Python, Node, etc.) para inspeccionar las colas SQS y registros de DynamoDB.
   - Ejemplos de comandos útiles en LocalStack:
     ```sh
     aws --endpoint-url=http://localhost:4566 sqs list-queues
     aws --endpoint-url=http://localhost:4566 dynamodb list-tables
     aws --endpoint-url=http://localhost:4566 dynamodb scan --table-name <nombre_tabla>
     ```

---

## Nota para Usuarios de Windows

Si ejecutas el sistema sobre Windows y la tabla de DynamoDB no se carga correctamente, ejecuta este comando para convertir los saltos de línea del script de inicialización:

```sh
wsl dos2unix scripts/init_localstack.sh


## Presentada por; John Carlos Arrieta Arrieta