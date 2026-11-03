# Destino Soñado – Sistema de gestión para una agencia de turismo

Trabajo Práctico Obligatorio de Paradigma Orientado a Objetos.
Propuesta B: Agencia de Turismo.

## Integrantes

- Victoria Garcia Restaino
- Romina Analia Colqui
- Nayla Dinara Granelli

## Qué hace el sistema

Destino Soñado es una aplicación en Java, de uso por consola, que ordena el trabajo de una agencia de turismo (los datos se guardan en archivos, así que se conservan entre una ejecución y otra). Permite:

- Administrar el catálogo de tours (aventura, culturales y de naturaleza) con su itinerario y sus tarifas por temporada.
- Programar salidas, asignándoles guía y vehículo, y controlar el cupo.
- Registrar clientes y crear reservas con uno o más pasajeros.
- Calcular el precio de cada reserva según el tipo de pasajero, las promociones y los servicios adicionales.
- Registrar la seña y los pagos (efectivo, transferencia o tarjeta, con recargo en cuotas).
- Cancelar o modificar reservas aplicando la política de devolución.
- Emitir la lista de pasajeros de cada salida, liquidar los honorarios de los guías y generar reportes.

## Requisitos

- JDK 17 o superior.
- No se usan librerías externas.
- Un editor como VS Code o IntelliJ IDEA (o la terminal).

## Cómo ejecutarlo

1. Clonar el repositorio:

   ```
   git clone https://github.com/vickkk28/Agencia-Turismo-POO.git
   ```

2. Abrir la carpeta del proyecto en VS Code o IntelliJ IDEA.
3. Ejecutar la clase `destinosonado.Main` (archivo `src/destinosonado/Main.java`).
4. Seguir las opciones del menú que aparece en la consola.

## Cómo correr las pruebas

Las pruebas están en la carpeta `test`. Cada clase de prueba tiene su propio método `main`. Al ejecutarla, la consola muestra cada comprobación con el resultado esperado y el obtenido, y al final un resumen de correctas y fallidas.

## Datos de prueba

## Cómo está organizado el repositorio

- `src/`: código fuente, en paquetes (`modelo`, `servicios`, `controlador`, `persistencia` y `excepciones`).
- `test/`: pruebas unitarias.
- `docs/`: documento de diseño, plan de pruebas e informe.
- `docs/uml/`: diagramas UML (imagen y archivo fuente de cada uno).

## Documentación

El análisis y el diseño del sistema (requisitos, casos de uso, diagramas de clases y de secuencia, y la aplicación de principios SOLID y patrones GRASP) están en la carpeta `docs`.

## Aporte de cada integrante

Victoria Garcia Restaino: Propuesta seleccionada e identificacion de integrantes, diagrama de secuencia, SOLID y GRASP. Tambien realizó:
- Estructura inicial del proyecto, enums y excepciones.
- DatosPasajero, Salida y Reserva.
- CalculadoraPrecio con Promocion, PromocionReservaAnticipada y PromocionGrupo.
- ControladorReservas: crear reserva.
- Pruebas: las de sus clases, y las del cálculo de precio con promociones.
- Documentación: README y la sección "cambios respecto del diseño original" del informe final.

Romina Analia Colqui: Diagrama de clases inicial y final. Tambien realizó:
- Persona, Cliente, Empleado, Vendedor, Coordinador y Administrador.
- Guia y Pasajero.
- Tour, TourAventura, TourCultural, TourNaturaleza, Actividad, Tarifa, Proveedor, ServicioAdicional y Vehiculo.
- ControladorSalidas: programar salida, emitir lista de pasajeros y verificar salidas.
- LiquidadorHonorarios y la clase Liquidacion.
- Pruebas: las de sus clases (sobre todo las validaciones del guía y de TourAventura).
- Documentación: el plan de pruebas funcionales (dato de entrada, resultado esperado y obtenido).

Nayla Granelli: Requisitos y supuestos considerados y descripcion de los casos de uso. Tambien realizó:
- Pago, PagoEfectivo, PagoTransferencia y PagoTarjeta.
- Cancelacion, NotaAjuste, PoliticaDevolucion, DevolucionPorAnticipacion y DevolucionTotalAgencia.
- Repositorio y RepositorioArchivo.
- ServicioReportes.
- El menú de consola en Main.
- ControladorReservas: registrar pagos y cancelar reserva.
- Pruebas: recargos de pago, tramos de devolución y guardado en archivo.
- Documentación: la sección «dificultades encontradas» del informe final, y los datos de prueba del README.
