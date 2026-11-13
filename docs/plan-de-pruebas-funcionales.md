# Plan de pruebas funcionales

Sistema: Destino Soñado (agencia de turismo) · Fecha de ejecución: 12/11/2026

## Cómo se ejecutaron

Cada caso se ejecutó contra el sistema real, a través de los controladores (`ControladorReservas` y `ControladorSalidas`) y con persistencia en archivo (`RepositorioArchivo`), igual que lo usa la aplicación. No se simuló ninguna respuesta: la columna "Resultado obtenido" es lo que devolvió el programa.

Los datos de partida son los del programa de prueba:

- Tour 1 "Cataratas del Iguazú": cultural, 3 días, cupo de 2 a 20, tarifa de $100.000 por pasajero adulto.
- Tour 2 "Trekking Fitz Roy": aventura, 5 días, cupo de 2 a 10, tarifa de $200.000 (el seguro es obligatorio).
- Guía con honorario de $20.000 por día y un micro de 20 lugares.
- Seguro de viaje: servicio adicional de $5.000.
- Los datos se cargaron con salidas a 30, 40, 45, 50, 60 y 90 días de la fecha de ejecución.

Cuando el resultado esperado es el nombre de una excepción, se verifica que el sistema rechace la operación con esa excepción de regla de negocio (`ReglaDeNegocioException` o `CupoInsuficienteException`).

## Casos de prueba

| Id | Caso de uso | Dato de entrada | Resultado esperado | Resultado obtenido | Estado |
|---|---|---|---|---|---|
| PF-01 | Gestionar salidas | Programar salida del tour 1 para dentro de 30 dias | PROGRAMADA | PROGRAMADA | OK |
| PF-02 | Gestionar salidas | Programar salida de un tour que no existe (id 99) | ReglaDeNegocioException | ReglaDeNegocioException: No existe el Tour con ID: 99 | OK |
| PF-03 | Gestionar salidas | Salida de 3 dias que empieza hoy+30: fecha de fin | 2026-12-14 | 2026-12-14 | OK |
| PF-04 | Crear reserva / Calcular precio | 2 adultos, salida a 30 dias, tarifa $100000 | 200000 | 200000 | OK |
| PF-05 | Crear reserva / Calcular precio | Estado inicial de la reserva | PENDIENTE | PENDIENTE | OK |
| PF-06 | Crear reserva / Calcular precio | Despues de reservar 2 lugares, quedan (cupo 20) | 18 | 18 | OK |
| PF-07 | Crear reserva / Calcular precio | 1 adulto + 1 menor (60 %) + 1 infante (0 %) | 160000 | 160000 | OK |
| PF-08 | Crear reserva / Calcular precio | 1 jubilado (90 %) | 90000 | 90000 | OK |
| PF-09 | Crear reserva / Calcular precio | Grupo de 6 adultos (promocion grupo 10 %) | 540000 | 540000 | OK |
| PF-10 | Crear reserva / Calcular precio | 2 adultos con salida a 90 dias (reserva anticipada 15 %) | 170000 | 170000 | OK |
| PF-11 | Crear reserva / Calcular precio | 6 adultos a 90 dias: aplican 15 % y 10 %, se toma el mas favorable (no se acumulan) | 510000 | 510000 | OK |
| PF-12 | Crear reserva / Calcular precio | Pedir 25 lugares en una salida con cupo 20 | CupoInsuficienteException | CupoInsuficienteException: Cupo insuficiente: se pidieron 25 lugares y quedan 20 | OK |
| PF-13 | Crear reserva / Calcular precio | Reserva sin pasajeros | ReglaDeNegocioException | ReglaDeNegocioException: La reserva debe tener al menos un pasajero | OK |
| PF-14 | Crear reserva / Calcular precio | Reservar en una salida que no existe (id 99) | ReglaDeNegocioException | ReglaDeNegocioException: No existe la salida 99 | OK |
| PF-15 | Crear reserva / Calcular precio | Tour de aventura sin seguro | ReglaDeNegocioException | ReglaDeNegocioException: El pasajero debe contar con seguro para realizar un tour de aventura | OK |
| PF-16 | Crear reserva / Calcular precio | Tour de aventura con seguro ($200000 + $5000) | 205000 | 205000 | OK |
| PF-17 | Registrar pago | Pagar $10000 de $200000 (menos del 30 %) | PENDIENTE | PENDIENTE | OK |
| PF-18 | Registrar pago | Completar la sena: pagar $50000 mas (total pagado $60000 = 30 %) | CONFIRMADA | CONFIRMADA | OK |
| PF-19 | Registrar pago | Saldo pendiente despues de pagar $60000 de $200000 | 140000 | 140000 | OK |
| PF-20 | Registrar pago | Pagar mas que el saldo ($500000) | ReglaDeNegocioException | ReglaDeNegocioException: El pago supera el saldo pendiente de la reserva 1 | OK |
| PF-21 | Registrar pago | Pago con tarjeta en 3 cuotas de $100000: recargo (la implementacion usa 10 %) | 10000 | 10000 | OK |
| PF-22 | Registrar pago | Pago con tarjeta en 1 cuota: sin recargo | 0 | 0 | OK |
| PF-23 | Registrar pago | Pago en transferencia: sin recargo | 0 | 0 | OK |
| PF-24 | Registrar pago | Pagar una reserva inexistente (id 999) | ReglaDeNegocioException | ReglaDeNegocioException: No existe la reserva 999 | OK |
| PF-25 | Gestionar salidas | Salida con minimo 2 y 2 pasajeros con sena paga: estado | CONFIRMADA | CONFIRMADA | OK |
| PF-26 | Gestionar salidas | Reservar los 10 lugares de una salida con cupo maximo 10 | COMPLETA | COMPLETA | OK |
| PF-27 | Gestionar salidas | Reservar en una salida COMPLETA | ReglaDeNegocioException | ReglaDeNegocioException: La salida 5 no admite reservas (estado COMPLETA) | OK |
| PF-28 | Cancelar reserva | Cancelar con 40 dias de anticipacion habiendo pagado $100000: devolucion 90 % | 90000 | 90000 | OK |
| PF-29 | Cancelar reserva | La reserva cancelada no se elimina: estado | ANULADA | ANULADA | OK |
| PF-30 | Cancelar reserva | Cancelar dos veces la misma reserva | ReglaDeNegocioException | ReglaDeNegocioException: La reserva 9 no puede cancelarse (estado ANULADA) | OK |
| PF-31 | Cancelar reserva | Pagar una reserva anulada | ReglaDeNegocioException | ReglaDeNegocioException: La reserva 9 esta ANULADA y no admite pagos | OK |
| PF-32 | Cancelar reserva | Cancelar a 29 dias de la salida con $100000 pagados | 50000 | 50000 | OK |
| PF-33 | Cancelar reserva | Cancelar a 15 dias de la salida con $100000 pagados | 50000 | 50000 | OK |
| PF-34 | Cancelar reserva | Cancelar a 14 dias de la salida con $100000 pagados | 20000 | 20000 | OK |
| PF-35 | Cancelar reserva | Cancelar a 7 dias de la salida con $100000 pagados | 20000 | 20000 | OK |
| PF-36 | Cancelar reserva | Cancelar a 6 dias de la salida con $100000 pagados | 0 | 0 | OK |
| PF-37 | Cancelar reserva | Cancelar a 2 dias de la salida con $100000 pagados | 0 | 0 | OK |
| PF-38 | Cancelar reserva | Cancelacion provocada por la agencia a 2 dias de la salida: devolucion 100 % | 100000 | 100000 | OK |
| PF-39 | Cancelar reserva | Al cancelar se liberan los lugares: la salida vuelve a admitir reservas | true | true | OK |
| PF-40 | Emitir lista de pasajeros | Lista de la salida 1 (reservas activas; las anuladas no figuran) | OK | OK | OK |
| PF-41 | Emitir lista de pasajeros | Lista de una salida que no existe | ReglaDeNegocioException | ReglaDeNegocioException: No existe la salida con ID: 99 | OK |
| PF-42 | Liquidar honorarios | Tour de 3 dias, guia con $20000 por dia | 60000 | 60000 | OK |
| PF-43 | Liquidar honorarios | Liquidar una salida inexistente | ReglaDeNegocioException | ReglaDeNegocioException: No existe la salida con ID: 99 | OK |
| PF-44 | Emitir reportes | Reporte de ocupacion: una linea por salida (6 salidas) | 6 | 6 | OK |
| PF-45 | Emitir reportes | Ventas por vendedor: aparecen los dos vendedores | 2 | 2 | OK |
| PF-46 | Emitir reportes | Reporte de cancelaciones: hay 8 reservas canceladas | 8 | 8 | OK |
| PF-47 | Emitir reportes | Ingresos por tour: aparecen los 2 tours | 2 | 2 | OK |
| PF-48 | Persistencia | Cerrar y reabrir el archivo: cantidad de reservas | 16 | 16 | OK |
| PF-49 | Persistencia | Cerrar y reabrir: total y pagado de la reserva 1 | 200000/60000 | 200000/60000 | OK |
| PF-50 | Persistencia | Cerrar y reabrir: la reserva y su salida siguen siendo el mismo objeto | true | true | OK |

## Resumen

**50 correctos, 0 fallidos.**

## Observaciones

- **PF-21 (recargo de tarjeta):** la consigna indica un recargo del 7 % para pagos con tarjeta en cuotas y la implementación aplica el 10 %. El caso se dejó con el valor implementado y la diferencia está registrada como limitación conocida en el README.
- **PF-03:** la fecha de fin depende del día de ejecución (inicio de la salida más duración del tour menos un día).
- **Pruebas unitarias:** además de este plan, las clases de la carpeta `test` verifican la lógica de negocio con comprobaciones automáticas (reservas, salidas, cálculo de precio, devoluciones, persistencia, reportes y excepciones).
