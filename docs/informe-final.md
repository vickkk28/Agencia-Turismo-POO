# Informe final – Destino Soñado

Trabajo Práctico Obligatorio de Paradigma Orientado a Objetos (UADE) · Propuesta B: Agencia de Turismo

Integrantes: Victoria Garcia Restaino, Romina Analia Colqui y Nayla Dinara Granelli.

## 1. Cambios respecto del diseño original

Durante la codificación nos apartamos del diagrama de la Etapa 1 en estos puntos. Todos están reflejados en el diagrama de clases final actualizado (`docs/uml/clases-final`).

| Cambio | Motivo |
|---|---|
| `Tour` pasó de clase abstracta con tres subclases (`TourAventura`, `TourCultural`, `TourNaturaleza`) a clase concreta con un atributo `Categoria`. | La única diferencia de comportamiento era `requiereSeguro()`, que se resuelve con la categoría. Tres subclases que solo se diferenciaban en un valor no aportaban nada. La jerarquía de herencia con sobreescritura quedó en `Pago` (con `PagoEfectivo`, `PagoTransferencia` y `PagoTarjeta`) y en `Empleado` (con `Vendedor`, `Guia`, `Coordinador` y `Administrador`). |
| `Persona` y `Empleado` pasaron de abstractas a concretas. | Simplificó la creación de objetos en las pruebas y en los datos de ejemplo. |
| `DatosPasajero` incorporó apellido, teléfono y servicios contratados. | El formulario de reserva necesita esos datos, y el seguro obligatorio se valida al agregar el pasajero. |
| `Pago` guarda la fecha como `java.util.Date`, y `calcularTotal()` reemplazó a `importeTotal()`. | Decisión de implementación tomada al codificar la jerarquía de pagos. |
| `Reserva` implementa `Comparable` y `Salida` ofrece un `Comparator` por lugares disponibles. | Requisito de la consigna (ordenamiento natural y personalizado). Se usan en los reportes. |
| Persistencia en archivo con `AlmacenArchivo` y `RepositorioArchivo`. Se agregó `RepositorioMemoria` para las pruebas. | Las reservas, las salidas y los tours se referencian entre sí. Para que al leer el archivo sigan siendo los mismos objetos (y no copias), todas las colecciones se guardan juntas en un único archivo. |
| `ServicioReportes` devuelve listas de líneas de texto. | Permite mostrar los reportes en cualquier interfaz. |
| Se agregaron las ventanas (Swing) `VentanaPrincipal`, `FormularioReserva` y `FormularioModificar`. | Se eligió interfaz gráfica con manejo de eventos. Las ventanas solo llaman a `ControladorReservas`. |
| `ControladorReservas` recibe dos políticas de devolución (normal y por causa de la agencia). | Aplicación del principio abierto/cerrado: agregar una política nueva no obliga a modificar el controlador. |

## 2. Dificultades encontradas

- **Herramientas y librerías.** Empezamos con Maven y JUnit, pero no los vimos en la cursada y preferimos no depender de ellos. Los quitamos y armamos una clase propia (`Verificador`) para las pruebas, que se ejecutan con un `main`, y trabajamos sin librerías externas.
- **Integración del trabajo de tres personas.** Cada una programó sus clases en su rama y, al juntarlas, el proyecto no compilaba (38 errores por constructores y nombres que no coincidían). Lo resolvimos con una rama de integración y acordando los contratos entre clases (por ejemplo, la firma de `Salida` y `Reserva`).
- **Conflictos en pull requests.** Dos personas modificaron `Reserva` al mismo tiempo y hubo que resolver el conflicto a mano.
- **Mayúsculas en nombres de paquete.** Dos paquetes que solo se diferenciaban por una mayúscula (`Vista` y `vista`) hicieron fallar la compilación en Windows. Renombramos el paquete de pruebas.
- **Interfaz.** La ventana principal se diseñó con el diseñador visual de IntelliJ (archivo `.form`), por lo que solo abre desde IntelliJ. El formulario de reserva está escrito en código y funciona también desde VS Code. Aprendimos que conviene acordar la herramienta antes de empezar la interfaz.
- **Persistencia.** Guardar las colecciones por separado rompía las referencias entre objetos. Lo detectamos al diseñar la prueba de reapertura del archivo.
- **Reglas ambiguas.** Registramos supuestos (tipos de pasajero, qué cuenta como seguro, cómo se combinan las promociones). El recargo de tarjeta quedó en 10 % mientras la consigna indica 7 %; está documentado en el README como limitación.

## 3. Distribución de tareas

La distribución de tareas se reconstruyó a partir del historial de commits del repositorio (a la fecha de redacción: Romina 29 commits propios, Victoria 24 y Nayla 16, sin contar los merges).

**Victoria Garcia Restaino**
- Propuesta seleccionada, requisitos y diagramas de secuencia, SOLID y GRASP (Etapa 1).
- Estructura del proyecto, enums y excepciones.
- `DatosPasajero`, `Salida` y `Reserva`.
- `CalculadoraPrecio` con las promociones.
- `ControladorReservas` (crear reserva, registrar pago y cancelar reserva).
- Políticas de devolución (`DevolucionPorAnticipacion` y `DevolucionTotalAgencia`).
- Persistencia en archivo (`AlmacenArchivo` y `RepositorioArchivo`) y `ServicioReportes`.
- Ventana "Nueva reserva" (`FormularioReserva`).
- `Main` con persistencia, integración del proyecto y pruebas de sus clases.
- Plan de pruebas funcionales y README.

**Romina Analia Colqui**
- Diagrama de clases inicial y final.
- `Persona`, `Cliente`, `Empleado` y sus subclases, `Guia` y `Pasajero`.
- `Tour`, `Actividad`, `Tarifa`, `Proveedor`, `ServicioAdicional` y `Vehiculo`.
- `ControladorSalidas`, `LiquidadorHonorarios` y `Liquidacion`.
- Interfaz gráfica: `VentanaPrincipal` y `FormularioModificar`.
- Pruebas de `Guia` y `Pasajero`.

**Nayla Dinara Granelli**
- Descripción de los casos de uso y supuestos.
- `Pago`, `PagoEfectivo`, `PagoTransferencia` y `PagoTarjeta`.
- `Cancelacion` y `NotaAjuste`.
- `README` del proyecto (versión actual) y ajustes en `PagoTarjeta`.

## 4. Reflexión del grupo

- Separar las reglas de negocio de la interfaz nos permitió cambiar de ventana (y probar sin ventana) sin tocar la lógica. Las 50 pruebas funcionales y las pruebas unitarias corren sin abrir ninguna pantalla.
- Aplicar principios y patrones fue más útil de lo que esperábamos cuando algo cambió: agregar una política de devolución o una promoción es agregar una clase, y no hay que tocar el cálculo ni la reserva.
- Trabajar con ramas y pull requests obligó a acordar cómo se llaman y se usan las clases de los demás. Si lo hacemos de nuevo, definiríamos las firmas de las clases compartidas antes de empezar a programar, y acordaríamos entorno de desarrollo y estructura de paquetes desde el primer día.
- Dejaríamos para antes la persistencia y la interfaz, que sumaron riesgo al final.
- Lo que quedó fuera del alcance (liquidación del 5 % de las ventas, validaciones de superposición de salidas, cancelación automática por cupo mínimo y por falta de pago del saldo) está listado como limitación en el README.
