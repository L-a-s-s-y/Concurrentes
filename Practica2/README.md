
  

# SEGUNDA PRÁCTICA

  
  

# Análisis y Diseño

  

Estructuras de datos, variables compartidas y procedimientos necesarios.

  

## Variables compartidas

  

-  `TipoCliente: Enum`

Estatus del cliente. Dos tipos: PREMIUM y ESTANDAR.

-  `TIEMPO_EJECUCION: int`

Tiempo de ejecución de los hilos hasta su interrupción. Expresado en milisegundos.

-  `TIEMPO_LLEGADA_MAX: int`

Tiempo máximo que puede tardar un cliente en llegar a la cola del restaurante. Expresado en segundos.

-  `TIEMPO_LLEGADA_MIN: int`

Tiempo mínimo que puede tardar un cliente en llegar a la cola del restaurante. Expresado en segundos.

-  `AFORO: int`

Cantidad de mesas, y por tanto de comensales, que puede haber a la vez en el restaurante.

-  `MAX_PLATOS: int`

Número máximo de platos que puede pedir un cliente.

-  `MIN_PLATOS: int`

Número mínimo de platos que puede pedir un cliente.

-  `RECURSOS_MAXIMOS: int`

Cantidad de recursos máximos que puede tener asignados un proceso.

-  `PRECIO_MAX: int`

Precio máximo que puede alcanzar un plato.

-  `PRECIO_MIN: int`

Precio mínimo que puede alcanzar un plato.

-  `PRECIO_MIN_PREMIUM: int`

Precio mínimo de plato que pedirá un cliente con estatus PREMIUM.

-  `PRECIO_MAX_ESTANDAR: int`

Precio máxmo de plato que pedirá un cliente con estatus ESTANDAR.

-  `CARTA_TAM: int`

Número de platos distintos en la carta.

-  `LIMITE_TURNOS: int`

Máximo número de clientes que entrarán al restaurante antes de que el cliente con estatus ESTANDAR que esté esperando sea considerado como cliente con estatus PREMIUM.

-  `DISTRIBUCION: int`

Proporcion entre el número clientes ESTANDAR y clientes PREMIUM.

-  `TIEMPO_COCINACION: int`

Tiempo que tarda un plato en ser cocinado. En segundos.

-  `TIEMPO_MAX_COMICION: int`

Tiempo máximo que tarda un cliente en comerse un plato servido.

-  `TIEMPO_MIN_COMICION: int`

Tiempo mínimo que tarda un cliente en comerse un plato servido.

-  `aleatorio: Random`

Variable de tipo Random que se usará para generar números aleatorios.
  

## Clases
* Todas las Clases tienen los getters, setters y constructores que sean necesarios para el correcto funcionamiento del programa.
  

### COMANDA
  

#### Variables locales:

  
-  `ID_cliente: long`

Contiene el dni del cliente que encargó el plato.

-  `plato: Plato`

Plato que debe cocinarse.


#### Funciones:

```
func compareTo

	    Necesaria porque esta clase implementa la interfaz Comparable.
	    
	    if precio de Comanda A > precio Comanda B
		    MayorPrioridad para A.
		else
			MayorPrioridad para B.

Fin func

```

### Mesa

#### Variables locales:

-  `libre: boolean`

True si la mesa está libre. False si la mesa está ocupada.

-  `dni_cliente: long`

DNI del cliente que ocupa la mesa en ese momento. Si está libre valdrá -1 .

-  `fallo_inicial: int`

True si el fallo fue en la creación del proceso. False en caso contrario.

-  `ID: int`

ID único de la mesa.

#### Funciones:

- Ninguna más allá de contructor, getters y setters.

### Plato

#### Variables locales:

-  `ID: int`

ID único del plato.

-  `precio: double`

Precio del plato.

#### Funciones:

- Ninguna más allá de contructor, getters y setters.


### Restaurante

#### Variables locales:

-  `monitor: Monitor`

Instancia del monitor para el uso de las estructuras de datos y funciones.

#### Funciones:

```

func run
        while no sea interrumpido{
	        while haya clientes en la cola y haya sitio{
		        if hay clientes en la cola premium{
			        Entra al restaurante el siguiente cliente.
		        }
		        else{
			        if hay clientes en la cola no premium{
				        Entra al restaurante el siguiente cliente.
			        }
		        }
	        }
	        while haya comandas sin atender{
		        Atiende comandas.
	        }
        }
        Calcular y mostrar recaudación.
Fin func

```

### Cliente

#### Variables locales:

-  `DNI: long`

DNI del cliente. Sirve como identificador único.

-  `turnos: int`

Número de clientes que han entrado antes que él al restaurante. Solo se usa para clientes ESTANDAR.

-  `dentro: boolean`

True si el cliente está comiendo en el restaurante. False en caso contrario.

-  `estatus: TipoCliente`

Denota el tipo de cliente.

-  `platos_pedidos: ArrayList`

Lista con los ids de los platos que ha pedido el cliente.

-  `monitor: Monitor`

Instancia del monitor.

-  `espera: Semaphore`

Semáforo que servirá para dormir/despertar al cliente cuando sea necesario.

#### Funciones:

```

func run

	Llega a la cola.
	Duerme hasta que entra al restaurante.
	Elige los platos que va a tomar.
	for cada plato pedido{
		Espera hasta que el plato está cocinado.
		Come el plato en cuestion.
	}
	Anuncia que ha terminado de comer.
	Paga.
	Deja la mesa.
	Anuncia que se va.

Fin func

```

```
func dormir

	espera.wait()

Fin func
```
```
func despertar

	espera.signal()

Fin func
```
```
func incrementarTurnos

	turnos++

Fin func
```
### Monitor

#### Variables locales:

-  `carta: ArrayList de Plato`

Lista con cada plato que ofrece el restaurante.


-  `comandas: PriorityQueue de Comandas`

Cola ordena con las comandas encargadas. Cuanto mayor precio mayor prioridad.

-  `cola_clientes: ArrayList de Cliente`

Cola de clientes PREMIUM.

-  `colaNoPremium: ArrayList de Cliente`

Cola de clientes ESTANDAR.

-  `comensales: ArrayList de Cliente`

Lista de clientes que están comiendo en el Restaurante.

-  `mesas: ArrayList de Mesa`

Lista de mesas que hay en el Restaurante.

-  `recaudacion: double`

Recaudación total del Restaurante.

-  `lockCola: ReadWriteLock`

Lock que garantiza el acceso seguro a los datos de cola_clientes.

-  `lockNoPremium: ReadWriteLock`

Lock que garantiza el acceso seguro a los datos de colaNoPremium.

-  `lockComensal: ReadWriteLock`

Lock que garantiza el acceso seguro a los datos de comensales.

-  `lockMesa: ReadWriteLock`

Lock que garantiza el acceso seguro a los datos de mesas.

-  `lockComandas: ReadWriteLock`

Lock que garantiza el acceso seguro a los datos de comandas.

-  `lockCola: ReentrantLock`

Lock que garantiza el acceso seguro a la variable recaudacion.

#### Funciones:

```

func comprobarAforo()

	if el tamaño de la lista de comensales es menor que AFORO
		retorna true
	else
		retorna false

Fin func

```
```

func dejarMesa(long dni)

	Localiza la mesa que ocupa el cliente con el dni proporcionado.
	Pone la mesa.disponibilidad a true.
	Pone el mesa.dni_cliente a -1.
	Elimina al cliente de comensales.
	Marca la variable cliente.dentro a false.

Fin func

```
```

func pagar(ArrayList idPlatos)

	for cada id de idPlatos
		recaudacion+= precio del plato

Fin func

```
```

func elegirPlato(Cliente comensal)

	menu= numero de platos que comerá el comensal
	while el numero de platos pedidos < menu{
		seleccion= plato aleatorio de la carta
		if precio de seleccion se ajusta a los parámetros{
			Se apunta como plato a comer.
		}
	}

Fin func

```
```

func entrarEnRestaurante(Cliente comensal)

	if hay sitio en el restaurante{
		Selecciona una mesa disponible.
		Asigna los valores correspondientes a la mesa.
		Añade al comensal a la lista de comensales.
		Actualiza los turnos de los clientes ESTANDAR.
		comensal.dentro= true
		Despierta al comensal.
	}

Fin func

```
```

func cocinarEtServir(Peticion peticion)

	if hay comandas{
		Toma la primera comanda de la cola.
		Cocina el plato correspondiente.
		Despierta al comensal haciéndole saber que su plato está listo.
	}

Fin func

```
```
func hayComandas()

	Retorna true en caso de que haya comandas.
	Retorna false en caso contrario.

Fin func
```
```
func mesaDisponible()

	Retorna la posicion de la primera mesa disponible.
	Retorna -1 en caso contrario.

Fin func
```
```
func insertarCola(Cliente nuevo)

	if cliente premium
		inserta en la cola_clientes
	else
		inserta en la colaNoPremium

Fin func
```
```
func genteEnCola()

	Retorna true en caso de que haya algún cliente esperando en alguna
	de las dos colas.
	Retorna false en caso contrario.

Fin func
```
```
func genteEnColaPremium()

	Retorna true en caso de que haya algún cliente esperando en la
	cola_clientes.
	Retorna false en caso contrario.

Fin func
```
```
func genteEnColaNoPremium()

	Retorna true en caso de que haya algún cliente esperando en la
	colaNoPremium.
	Retorna false en caso contrario.

Fin func
```
```
func miraColaPremium()

	Retorna pero no elimina al siguiente cliente en la cola Premium.

Fin func
```
```
func miraColaNoPremium()

	Retorna pero no elimina al siguiente cliente en la cola no Premium.

Fin func
```
```
func siguienteColaPremium()

	Retorna y elimina al siguiente cliente en la cola Premium.

Fin func
```
```
func siguienteColaNoPremium()

	Retorna y elimina al siguiente cliente en la cola no Premium.

Fin func
```
```
func buscarComensalPorDNI(long dni)

	Retorna la posicion en comensales del Cliente con el dni proporcionado.
	Retorna -1 si no está.

Fin func
```
```
func actualizarTurnos()

	Para cada cliente en la cola no premium, suma uno a su variable turnos.

Fin func
```
```
func buscarMesaPorDNI(long dni)

	Retorna la posición en la lista de mesas la mesa con el dni
	proporcionado asociado.

Fin func
```
```
func siguienteColaNoPremium()

	Retorna y elimina al siguiente cliente en la cola no Premium.

Fin func
```

### Hilo Principal

#### Funciones:
```
func main()

	Crea el Monitor.
	Crea el executor.
	Crea el Restaurante.
	Ejecuta el Restaurante.
	while no haya pasado el tiempo de ejecución{
		Crea un cliente.
		Ejecuta el cliente creado.
	}
	Se muestra qué pidió cada cliente.
	Se muestra la recaudación.

Fin func
```
