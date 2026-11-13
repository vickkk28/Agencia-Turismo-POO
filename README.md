# Destino Soñado – Sistema de gestión para una agencia de turismo

Trabajo Práctico Obligatorio de Paradigma Orientado a Objetos (UADE). Propuesta B: Agencia de Turismo.

## Integrantes

- Victoria Garcia Restaino
- Romina Analia Colqui
- Nayla Dinara Granelli

## Qué hace el sistema

Aplicación en Java (17) con interfaz gráfica Swing, sin librerías externas. Cubre:

- Catálogo de tours (aventura, cultural, naturaleza) con tarifas por temporada y alta de tours nuevos.
- Programación de salidas con guía, vehículo y control de cupo. Se valida que la fecha no sea pasada, que el guía esté habilitado en la categoría del tour, que el vehículo tenga capacidad para el cupo máximo y que el guía no tenga otra salida en las mismas fechas.
- Reservas con uno o más pasajeros y cálculo del precio según tipo de pasajero (adulto, menor, infante, jubilado), promociones (reserva anticipada 15 % con más de 60 días, grupo 10 % desde 6 pasajeros; no se acumulan, se aplica la más favorable) y servicios adicionales. El seguro es obligatorio en los tours de aventura.
- Pagos en efectivo, transferencia o tarjeta (con recargo por cuotas). La seña del 30 % confirma la reserva.
- Cancelaciones con política de devolución: 90 % con 30 días o más de anticipación, 50 % entre 29 y 15 días, 20 % entre 14 y 7 días, 0 % con menos de 7 días, y 100 % si la causa es de la agencia. Una reserva pagada no se elimina: pasa a ANULADA y genera su cancelación.
- Lista de pasajeros por salida, liquidación de honorarios de guías y verificación de salidas (confirma las que alcanzaron el cupo mínimo).
- Reportes: ocupación por salida, ventas por vendedor, ingresos por tour y cancelaciones.
- Persistencia en archivo: los datos se guardan en `datos/agencia.dat` y se recuperan al volver a abrir el programa.

## Requisitos

- JDK 17 o superior. Funciona en IntelliJ IDEA y en VS Code.

## Cómo ejecutarlo

1. Clonar el repositorio:
   ```
   git clone https://github.com/vickkk28/Agencia-Turismo-POO.git
   ```
2. Abrir la carpeta en IntelliJ o VS Code (carpetas de código: `src` y `test`).
3. Ejecutar el `main` de `src/destinosonado/Main.java`. Se abre la ventana de gestión con cinco pestañas:
   - **Reservas (gestión):** nueva reserva, registrar pago, cancelar reserva y ver detalle.
   - **Salidas:** programar salida, lista de pasajeros, liquidar honorarios, verificar salidas y ordenar por lugares libres.
   - **Tours:** ver el catálogo y crear tours.
   - **Reportes:** los cuatro reportes.
   - **Tabla de reservas:** tabla con búsqueda (ventana principal).

La primera vez, con el archivo de datos vacío, se cargan datos de prueba. Para volver a empezar de cero se borra la carpeta `datos/`.

## Cómo correr las pruebas

Las pruebas están en `test`. Cada clase tiene su propio `main` (ejecutar con Run). Usan una herramienta propia, `Verificador`, que muestra cada comprobación con el resultado esperado y el obtenido, y al final un resumen de correctas y fallidas. Clases de prueba: `SalidaTest`, `ReservaTest`, `CalculadoraPrecioTest`, `ControladorReservasTest`, `ControladorSalidasToursTest`, `RepositorioArchivoTest`, `ReportesYOrdenTest`, `DevolucionTest`, `TipoPasajeroTest`, `ExcepcionesTest`, `PruebaGuia` y `PasajeroPrueba`.

El plan de pruebas funcionales (datos de entrada, resultado esperado y obtenido) está en `docs/plan-de-pruebas-funcionales.md`.

## Datos de prueba

| Tour | Categoría | Precio base | Salidas |
|---|---|---|---|
| Cataratas del Iguazú | Cultural | $100.000 | +30 y +60 días (guía Carlos Gomez, micro AB123CD) |
| Trekking Fitz Roy | Aventura (exige seguro) | $150.000 | +75 días (guía Laura Perez, combi CD456EF) |
| Esteros del Iberá | Naturaleza | $80.000 | +20 días (guía Laura Perez, combi CD456EF) |

Vendedor de prueba: Ana Lopez. En el formulario de pasajeros la fecha de nacimiento se escribe `AAAA-MM-DD` (por ejemplo `1990-01-31`).

## Organización del repositorio

- `src/destinosonado/`
  - `model/`: Reserva, Salida, Tour, Pasajero, Guia, Tarifa.
  - `modelo/enums/`: categorías, temporadas, estados, tipos de pasajero y de vehículo.
  - `Empleados/`, `Pagos/` (con `Cancelacion/` y las políticas de devolución).
  - `servicios/`: cálculo de precio, promociones y reportes.
  - `controlador/`: ControladorReservas, ControladorSalidas, ControladorTours.
  - `Persistencia/`: interfaz `Repositorio` e implementaciones en memoria y en archivo.
  - `Vista/`: ventanas y pestañas.
  - `excepciones/`: excepciones de reglas de negocio.
- `test/`: pruebas unitarias.
- `docs/`: requisitos, diseño, diagramas UML (`docs/uml`), plan de pruebas funcionales e informe final.

## Diseño

- **SOLID:** responsabilidad única (cálculo de precio, promociones y reportes separados); abierto/cerrado (nuevas promociones o políticas de devolución sin tocar el resto); inversión de dependencias (los controladores usan la interfaz `Repositorio`, con implementación en memoria o en archivo).
- **GRASP:** Experto, Creador, Controlador y Polimorfismo (jerarquía de `Pago`).
- `Reserva` implementa `Comparable` (orden por fecha de salida) y `Salida` ofrece un `Comparator` por cupo disponible.

## Limitaciones conocidas

- El recargo por cuotas con tarjeta es del 10 % en el código (la consigna menciona 7 %).
- Los guías y vehículos disponibles al programar una salida son los que ya figuran en salidas existentes; no hay pantalla de alta de guías ni de vehículos.
- La persistencia usa serialización de Java en un único archivo; no es legible a mano.

## Documentación

Requisitos, casos de uso, diagramas de clases y de secuencia, SOLID/GRASP, plan de pruebas e informe final en `docs/`.

## Aporte de cada integrante

*(Confirmado según el historial de commits de git.)*

**Victoria Garcia Restaino:** propuesta e identificación de integrantes, diagrama de secuencia, SOLID y GRASP. Además:
- Estructura inicial del proyecto, enums y excepciones.
- DatosPasajero, Salida y Reserva (cupo, pagos, cancelación).
- CalculadoraPrecio y promociones.
- ControladorReservas (crear reserva, registrar pago, cancelar reserva) y política de devolución.
- Persistencia en archivo (`AlmacenArchivo`, `RepositorioArchivo`, `RepositorioMemoria`) y `ServicioReportes`.
- `Comparable` en Reserva y `Comparator` en Salida.
- ControladorTours, validaciones de programación de salidas y la ventana de gestión con sus pestañas (reservas, salidas, tours, reportes).
- Ventana "Nueva reserva" (FormularioReserva).
- Verificador y la mayoría de las pruebas unitarias.
- Plan de pruebas funcionales, diagramas UML finales, informe final y README.

**Romina Analia Colqui:** diagrama de clases inicial y final. Además:
- Persona, Cliente, Empleado, Vendedor, Coordinador, Administrador, Guia y Pasajero.
- Tour, Actividad, Tarifa, Proveedor, ServicioAdicional y Vehiculo.
- ControladorSalidas, LiquidadorHonorarios y Liquidacion.
- Ventana principal con tabla de reservas y búsqueda, y ventanas de modificación.
- Pruebas de Guia y Pasajero.

**Nayla Dinara Granelli:** requisitos, supuestos y descripción de casos de uso. Además:
- Pago, PagoEfectivo, PagoTransferencia y PagoTarjeta (con recargo por cuotas).
- Cancelacion y NotaAjuste.
- Clases de PoliticaDevolucion y la interfaz Repositorio.
- Sección "dificultades encontradas" del informe final.
