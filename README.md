# Destino Soñado – Sistema de gestión para una agencia de turismo

Trabajo Práctico Obligatorio de Paradigma Orientado a Objetos.
Propuesta seleccionada: Agencia de Turismo.

## Integrantes

- Victoria Garcia Restaino
- Romina Analia Colqui
- Nayla Dinara Granelli

## Qué hace el sistema

Destino Soñado es una aplicación en Java con interfaz gráfica (Swing) que modela el trabajo de una agencia de turismo. El dominio está resuelto en clases independientes de la interfaz, con sus propias pruebas. Incluye:

- Tours de tres categorías (aventura, cultural y naturaleza), con itinerario (actividades y proveedores) y tarifas por temporada y vigencia.
- Salidas de un tour, con fecha, cupo mínimo y máximo, guía y vehículo asignados. La salida controla su cupo y su estado (programada, confirmada, completa, etc.).
- Reservas de un cliente titular para una salida, con uno o más pasajeros y servicios adicionales (por ejemplo, seguro de viaje, obligatorio en los tours de aventura).
- Cálculo del precio según el tipo de pasajero (adulto, menor, infante, jubilado), las promociones y los servicios adicionales.
- Pagos en efectivo, por transferencia o con tarjeta (con recargo en cuotas). La seña del 30 % confirma la reserva.
- Cancelación de reservas con política de devolución según la anticipación (o devolución total si la causa es la agencia). La reserva no se elimina: queda anulada con su cancelación registrada.
- Lista de pasajeros de cada salida, liquidación de honorarios de guías y reportes (ocupación por salida, ventas por vendedor, ingresos por tour y cancelaciones).

### Qué se puede hacer desde la ventana

La ventana principal muestra una tabla de reservas y permite:

- **Nuevo:** abre el formulario de reserva (elegir salida, datos del cliente y de cada pasajero, tipo de pasajero y servicios). La reserva se crea a través de `ControladorReservas`, que valida cupo, seguro y calcula el total.
- **Modificar:** cambia el estado que se muestra en la tabla.
- **Eliminar:** quita la fila de la tabla.
- **Buscar:** filtra la tabla por texto.

El resto de las funciones (pagos, cancelación con devolución, lista de pasajeros, liquidación y reportes) están implementadas en las clases del dominio y cubiertas por las pruebas, pero todavía no tienen pantalla. Ver "Limitaciones conocidas".

## Requisitos

- JDK 17 o superior.
- No se usan librerías externas.
- IntelliJ IDEA (recomendado). La ventana principal se diseñó con el diseñador de formularios de IntelliJ (`VentanaPrincipal.form`), por eso conviene abrir y ejecutar el proyecto desde ese entorno.

## Cómo ejecutarlo

1. Clonar el repositorio:

   ```
   git clone https://github.com/vickkk28/Agencia-Turismo-POO.git
   ```

2. Abrir la carpeta del proyecto en IntelliJ IDEA.
3. Ejecutar la clase `destinosonado.Main` (archivo `src/destinosonado/Main.java`). Se abre la ventana principal.

Las reservas que aparecen en la tabla se guardan en el archivo `reservas.txt` (en la carpeta desde la que se ejecuta el programa) y se vuelven a cargar al abrir la ventana.

## Cómo correr las pruebas

Las pruebas están en la carpeta `test` y no usan JUnit: cada clase de prueba tiene su propio método `main`. Al ejecutarla, la consola muestra cada comprobación con el resultado esperado y el obtenido, y al final un resumen de correctas y fallidas.

| Clase de prueba | Qué verifica |
|---|---|
| `reservas/ReservaTest`, `reservas/SalidaTest` | Reservas, pagos, seña, cupo y estados de la salida |
| `servicios/CalculadoraPrecioTest` | Precio por tipo de pasajero, promociones y adicionales |
| `devolucion/DevolucionTest` | Tramos de devolución y cancelación de reservas |
| `controlador/ControladorReservasTest` | Creación, pago y cancelación de reservas |
| `archivos/RepositorioArchivoTest` | Guardado y lectura en archivo |
| `informes/ReportesYOrdenTest` | Reportes y órdenes (Comparator y Comparable) |
| `modelo/enums/TipoPasajeroTest` | Factores de tarifa |
| `excepciones/ExcepcionesTest` | Excepciones de reglas de negocio |
| `GuiaPrueba/PruebaGuia`, `PruebaPasajero/PasajeroPrueba` | Guía y pasajero |
| `ventanas/PruebaFormularioReserva` | Abre el formulario de reserva |

Las clases `TourDePrueba`, `Verificador` y `controlador/RepositorioEnMemoria` son auxiliares de las pruebas.

## Datos de prueba

Al iniciar, `Main` carga estos datos en memoria:

| Dato | Valor |
|---|---|
| Vendedor | Ana Lopez (legajo 1, comisión 5 %) |
| Tour | T01 "Cataratas del Iguazú", categoría cultural, 3 días, cupo de 2 a 20 |
| Tarifa | Temporada media, $100.000 por pasajero adulto, vigente un año atrás y dos años adelante |
| Guía | Carlos Gomez, habilitado para tours culturales, $20.000 por día |
| Vehículo | Micro, patente AB123CD, 20 lugares |
| Salida 1 | Dentro de 30 días |
| Salida 2 | Dentro de 60 días |

Ejemplos para probar desde el formulario de reserva:

- 2 adultos en la salida 1: total de $200.000, sin descuento.
- 6 adultos en la salida 1: 10 % de descuento por grupo, total de $540.000.
- Un infante (menor de 3 años) se registra pero no paga tarifa.

## Cómo está organizado el repositorio

- `src/destinosonado/`: código fuente.
- `test/`: pruebas.
- `docs/`: documento de diseño (PDF del TPO).
- `reservas.txt`: archivo donde la ventana guarda la tabla de reservas.

| Paquete | Contenido |
|---|---|
| `destinosonado` | Clases base del dominio: `Persona`, `Cliente`, `Actividad`, `Proveedor`, `ServicioAdicional`, `Vehiculo`, `DatosPasajero`, `LiquidadorHonorarios`, `Liquidacion` y `Main` |
| `destinosonado.model` | `Tour`, `Tarifa`, `Salida`, `Reserva`, `Pasajero`, `Guia` |
| `destinosonado.modelo.enums` | `Categoria`, `Temporada`, `TipoPasajero`, `TipoVehiculo`, `EstadoReserva`, `EstadoSalida` |
| `destinosonado.Empleados` | `Empleado`, `Vendedor`, `Coordinador`, `Administrador` |
| `destinosonado.Pagos` | `Pago` (abstracta), `PagoEfectivo`, `PagoTransferencia`, `PagoTarjeta` |
| `destinosonado.Pagos.Cancelacion` | `Cancelacion`, `NotaAjuste`, `PoliticaDevolucion`, `DevolucionPorAnticipacion`, `DevolucionTotalAgencia` |
| `destinosonado.servicios` | `CalculadoraPrecio`, `Promocion`, `PromocionReservaAnticipada`, `PromocionGrupo`, `ServicioReportes` |
| `destinosonado.controlador` | `ControladorReservas`, `ControladorSalidas` |
| `destinosonado.Persistencia` | `Repositorio<T>`, `RepositorioArchivo<T>`, `RepositorioMemoria<T>`, `AlmacenArchivo`, `ArchivosReservastxt` |
| `destinosonado.Vista` | `VentanaPrincipal`, `FormularioReserva`, `FormularioModificar` |
| `destinosonado.excepciones` | `ReglaDeNegocioException`, `CupoInsuficienteException` |

## Decisiones de diseño

- **SRP:** el cálculo de precios (`CalculadoraPrecio`), las devoluciones (`PoliticaDevolucion`), los honorarios (`LiquidadorHonorarios`) y los reportes (`ServicioReportes`) están fuera de `Reserva`. No existe una clase "sistema" que haga todo: la coordinación está en `ControladorReservas` y `ControladorSalidas`.
- **OCP:** las promociones (`Promocion`) y las políticas de devolución (`PoliticaDevolucion`) son interfaces con una clase por variante. Agregar una nueva no obliga a modificar la calculadora ni la reserva.
- **DIP:** los controladores y los reportes dependen de `Repositorio<T>`. Hay dos implementaciones: `RepositorioMemoria` (la que usa `Main`) y `RepositorioArchivo` (guarda en un archivo mediante `AlmacenArchivo`, y se prueba en `RepositorioArchivoTest`).
- **Experto:** `Salida` sabe su cupo y su estado; `Reserva` sabe lo pagado y el saldo; `TipoPasajero` conoce su factor de tarifa.
- **Creador:** `Reserva` crea sus `Pasajero` y su `Cancelacion`.
- **Controlador:** la interfaz no habla con el dominio directamente, sino a través de `ControladorReservas`.
- **Polimorfismo:** `Pago` define `calcularRecargo()` y lo sobrescriben `PagoEfectivo`, `PagoTransferencia` y `PagoTarjeta`.
- **Orden de colecciones:** `Reserva` implementa `Comparable` (por fecha de salida y número) y `Salida` ofrece un `Comparator` por lugares disponibles, usados en los reportes.

## Reglas y supuestos considerados

Además de las reglas de la consigna, se tomaron estos supuestos:

- **Tarifa:** se toma la tarifa vigente en la fecha de inicio de la salida. La fecha de fin es la de inicio más la duración del tour menos un día.
- **Tipos de pasajero:** adulto paga 100 %, menor 60 %, infante 0 % y jubilado 90 %. El vendedor indica el tipo al cargar cada pasajero.
- **Promociones:** reserva anticipada (15 % si faltan más de 60 días) y grupo (10 % si son 6 o más pasajeros). No se acumulan: se aplica la más favorable. El descuento afecta solo a las tarifas, y los servicios adicionales se suman después.
- **Seguro:** es obligatorio en los tours de categoría aventura. Se reconoce como seguro un servicio adicional cuyo nombre contiene "seguro" o "asistencia".
- **Seña y confirmación:** la reserva pasa a confirmada con el 30 % del total pagado. La salida se confirma cuando las reservas confirmadas alcanzan el cupo mínimo, y pasa a completa al llegar al cupo máximo.
- **Devolución:** se calcula sobre lo pagado. 90 % con 30 días o más de anticipación, 50 % de 29 a 15, 20 % de 14 a 7, y sin devolución con menos de 7 días. Si la cancelación es responsabilidad de la agencia, 100 %.
- **Pagos con tarjeta:** el recargo se aplica cuando el pago es en más de una cuota (10 % en la implementación actual).
- **Honorarios del guía:** monto fijo por cada día del tour.

## Limitaciones conocidas

Lo siguiente no está implementado o difiere de lo pedido en la consigna:

- **Recargo de tarjeta:** la consigna indica 7 % y el código aplica 10 %.
- **Honorarios:** se calcula solo el monto fijo por día; no se suma el 5 % de las ventas de servicios adicionales y no se verifica que la salida esté finalizada.
- **Programación de salidas:** no se valida que el guía esté habilitado para la categoría, que no tenga salidas superpuestas ni que el vehículo tenga capacidad suficiente.
- **Ciclo de vida de la salida:** no hay cancelación automática por no alcanzar el cupo mínimo, ni forma de iniciar o finalizar una salida (no se exige guía y lista de pasajeros para iniciarla).
- **Saldo:** no se anula automáticamente la reserva si el saldo no se paga un mes antes de la salida.
- **Notas de ajuste:** existe la clase `NotaAjuste`, pero las reservas todavía no las registran.
- **Tipo de pasajero:** no se valida contra la fecha de nacimiento.
- **Tours:** `Tour` es una clase concreta y la categoría se guarda como atributo; no hay subclases por categoría. No hay control de edad mínima.
- **Persistencia:** `Main` trabaja con repositorios en memoria. Solo la tabla de la ventana se guarda en `reservas.txt` (como texto) y no se relaciona con el dominio al reiniciar.
- **Ventana:** solo permite crear reservas. "Modificar" y "Eliminar" afectan únicamente la tabla y no aplican las reglas de devolución ni de conservación de reservas. Pagos, cancelación, lista de pasajeros, liquidación y reportes no tienen pantalla.
- **ABM:** no hay pantallas para administrar tours ni registrar clientes (el tour, el guía y el vehículo se cargan en `Main`).

## Documentación

El análisis y el diseño del sistema (requisitos, casos de uso, diagramas de clases y de secuencia, y la aplicación de principios SOLID y patrones GRASP) están en el documento de la carpeta `docs` (`TPO- Destino Soñado.pdf`).

## Aporte de cada integrante

**Victoria Garcia Restaino:** propuesta seleccionada e identificación de integrantes, diagramas de secuencia, SOLID y GRASP. También realizó:
- Estructura inicial del proyecto, enums y excepciones.
- `DatosPasajero`, `Salida` y `Reserva`.
- `CalculadoraPrecio` con `Promocion`, `PromocionReservaAnticipada` y `PromocionGrupo`.
- `ControladorReservas`: crear reserva.
- Pruebas: las de sus clases y las del cálculo de precio con promociones.
- Documentación: README.

**Romina Analia Colqui:** diagrama de clases inicial y final. También realizó:
- `Persona`, `Cliente`, `Empleado`, `Vendedor`, `Coordinador` y `Administrador`.
- `Guia` y `Pasajero`.
- `Tour`, `Actividad`, `Tarifa`, `Proveedor`, `ServicioAdicional` y `Vehiculo`.
- `ControladorSalidas`: programar salida, emitir lista de pasajeros y verificar salidas.
- `LiquidadorHonorarios` y la clase `Liquidacion`.
- Interfaz gráfica.
- Pruebas: las de sus clases (sobre todo las validaciones del guía).

**Nayla Dinara Granelli:** requisitos y supuestos considerados, y descripción de los casos de uso. También realizó:
- `Pago`, `PagoEfectivo`, `PagoTransferencia` y `PagoTarjeta`.
- `Cancelacion`, `NotaAjuste`, `PoliticaDevolucion`, `DevolucionPorAnticipacion` y `DevolucionTotalAgencia`.
- `Repositorio` y `RepositorioArchivo`.
- `ServicioReportes`.
- `ControladorReservas`: registrar pagos y cancelar reserva.
- Pruebas: recargos de pago, tramos de devolución y guardado en archivo.
