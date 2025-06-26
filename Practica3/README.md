
  

# Tercera PRÁCTICA

Autor: Jose Antonio Laserna Beltrán  

# Análisis y Diseño

  

Estructuras de datos, variables compartidas y procedimientos necesarios.

  

## Variables compartidas

 -  `DESTINO: String`

Prefijo inicial para los buzones.

 -  `PREMIUM: String`

Sufijo para el buzón de llegada de los clientes premium.

 -  `ESTANDAR: String`

Sufijo para el buzón de llegada de los clientes estandar.

 -  `PEDIDO: String`

Sufijo para el buzón de llegada de los platos pedidos por los clientes.

 -  `COCINERO: String`

Sufijo para el buzón de llegada de las comandas al cocinero.

 -  `COCINADO: String`

Sufijo para el buzón de llegada de los platos ya concinados al restaurante.

 -  `SERVIR: String`

Sufijo para el buzón de llegada de los platos ya concinados al cliente.

 -  `FIN: String`

Sufijo para el buzón de llegada de la confirmación de que el cliente ha finalizado.

 -  `CONNECTION: String`

Dirección de la conexión.

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

-  `precio: double`

Precio del plato asociado.
  
-  `ID_cliente: int`

Contiene el dni del cliente que encargó el plato.

-  `platoID: int`

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

### Restaurante

#### Variables locales:

-  `conFactory: ActiveMQConnectionFactory`

Constructor factoría para crear la conexión.

-  `conection: Connection`

Guarda la conexión al servidor.

-  `session: Session`

Guarda la sesión.

-  `destinationPREMIUM: Destination`

Destino de los mensajes de la cola de clientes PREMIUM.

-  `destinationESTANDAR: Destination`

Destino de los mensajes de la cola de clientes ESTANDAR.

-  `destinationConfirmacion: Destination`

Destino de los mensajes de respuesta al cliente para que entre.

-  `destinationPedidos: Destination`

Destino de los mensajes de lo que han pedido los clientes.

-  `destinationComandas: Destination`

Destino de los mensajes con las comandas para el cocinero.

-  `destinationPlatos: Destination`

Destino de los mensajes del cocinero al restaurante con los platos cocinados.

-  `destinationServir: Destination`

Destino de los mensajes para el cliente con los platos cocinados.

-  `destinationFin: Destination`

Destino de los mensajes que avisan de que el cliente ha terminado.

-  `consumerPREMIUM: MessageConsumer`

Consumidor para destinationPREMIUM.

-  `consumerESTANDAR: MessageConsumer`

Consumidor para destinationESTANDAR.

-  `consumerPedidos: MessageConsumer`

Consumidor para destinationPedidos.

-  `consumerPlatos: MessageConsumer`

Consumidor para destinationPlatos.

-  `consumerFin: MessageConsumer`

Consumidor para destinationFin.

-  `producerLlegada: MessageProducer`

Productor para la confirmación de acceso a los clientes.

-  `producerComandas: MessageProducer`

Productor para el envío de comandas al cocinero.

-  `producerServir: MessageProducer`

Productor para servir los platos cocinados a los clientes.

-  `carta: ArrayList`

Arrya con cada uno de los platos que ofrece el restaurante.

-  `comensales: int`

Número de comensales que hay en el restaurante en el momento.

-  `recaudacion: double`

Cantidad total facturada por el restaurante.

-  `colaPremium: ArrayList`

Almacena los mensajes de consumerPREMIUM.

-  `colaEstandar: ArrayList`

Almacena los mensajes de consumerESTANDAR.

-  `colaPedidos: ArrayList`

Almacena los mensajes de consumerPedidos.

-  `colaPlatos: ArrayList`

Almacena los mensajes de consumerPlatos.

-  `colaFin: ArrayList`

Almacena los mensajes de consumerFin.

-  `comanda: PriorityQueue`

Ordena las comandas según precio para ser enviadas posteriormente al cocinero.

#### Funciones:

```

func run
        try{
			before()
			task()
		}
		catch(interrupciones){
			informa que excepción ha sido lanzada
		}
		finally{
			after()
			muestra la recaudación total
		}
Fin func

```

```

func task
	
	turno= 0
	while(no sea interrumpido){
	
		if(turnos mayor que el límite){
			mete el primero de la colaEstandar en la primera posición de la colaPremium
		}
		
		while(colaFin no esté vacía){
			colaFin.remove(0)
			comensales--
		}
		
		if(hay sitio en el restaurante){
			· Toma una mensaje de la colaPremium si no está vacía y turnos++.
			· Si no toma un mensaje de la colaEstandar y turnos= 0.
			· Envía un mensaje al cliente que mandó el mensaje tomado para que pueda entrar mediante producerLlegada.
		}
		
		if(colaPedidos no está vacía){
			· Toma el pedido del cliente.
			· Crea una comanda para cada plato que ha elegido el cliente.
			· La añade a comandas.
		}
		
		while(comandas no esté vacía){
			· Toma la primera comanda de comandas y la envía al cocinero con producerComandas.
		}
		
		while(colaPlatos no esté vacía){
			· Toma el primer mensaje de colaPlatos.
			· Envía un mensaje al cliente correpondiente mediante producerServir.
		}
	}
	
Fin func

```

```

func before
        Crea las conexiones, sesiones, destinos, productores, consumidores y listener necesarios.
Fin func

```

```

func after
        Cierra todas conexiones, productores, consumidores.
Fin func

```

### Cliente

#### Variables locales:

-  `DNI: int`

DNI del cliente. Sirve como identificador único.

-  `estatus: TipoCliente`

Denota el tipo de cliente.

-  `conFactory: ActiveMQConnectionFactory`

Constructor factoría para crear la conexión.

-  `conection: Connection`

Guarda la conexión al servidor.

-  `session: Session`

Guarda la sesión.

-  `destinationEnvio: Destination`

Destino del mensaje de que ha llegado.

-  `destinationRespuesta: Destination`

Destino de los mensajes de respuesta al cliente para que entre.

-  `destinationPedidos: Destination`

Destino de los mensajes de lo que ha pedido el cliente.

-  `destinationServir: Destination`

Destino de los mensajes para el cliente con los platos cocinados.

-  `destinationFin: Destination`

Destino de los mensajes que avisan de que el cliente ha terminado.

-  `consumerPlatos: MessageConsumer`

Consumidor para destinationServir.

-  `consumerEntrada: MessageConsumer`

Consumidor para destinationRespuesta.

-  `producerLlegada: MessageProducer`

Productor para avisar de que ha llegado al restaurante.

-  `producerCarta: MessageProducer`

Productor para enviar al restaurante los platos elegidos.

-  `producerFin: MessageProducer`

Productor para notificar al cliente que ha terminado.

#### Funciones:

```

func run
        try{
			before()
			task()
		}
		catch(interrupciones){
			Informa qué excepción ha sido lanzada.
		}
		finally{
			after()
		}
Fin func

```

```

func before
        Crea las conexiones, sesiones, destinos, productores, consumidores y listener necesarios.
Fin func

```

```

func after
        Cierra todas conexiones, productores, consumidores.
Fin func

```

```

func task

	· Envía un mensaje al restaurante avisando de que ha llegado mediante producerLlegada.
	· Espera a recibir el mensaje que le permite entrar al restaurante.
	· Elige qué platos va a comer.
	· Envía dichos platos al restaurante mediante producerCarta.
	· Come los platos que se le van sirviendo, esperando a que le sirvan cada vez.
	· Envía un mensaje al restaurante de que ha terminado mediante producerFin.

Fin func

```

### Cocinero

#### Variables locales:

-  `conFactory: ActiveMQConnectionFactory`

Constructor factoría para crear la conexión.

-  `conection: Connection`

Guarda la conexión al servidor.

-  `session: Session`

Guarda la sesión.

-  `destinationEnvio: Destination`

Destino de los mensajes con las comandas que llegan al cocinero.

-  `destinationRespuesta: Destination`

Destino de los mensajes de respuesta al restaurante con los platos cocinados.

-  `consumerComandas: MessageConsumer`

Consumidor para destinationEnvio.

-  `producer: MessageProducer``

Producer para enviar los platos cocinados al restaurante.

-  `colaComandas: ArrayList`

Almacena lso mensajes de consumerComandas.

-  `historialComandas: ArrayList`

Guarda la comanda de cada plato que se ha cocinado.

#### Funciones:

```

func run
        try{
			before()
			task()
		}
		catch(interrupciones){
			Informa qué excepción ha sido lanzada.
		}
		finally{
			after()
		}
Fin func

```

```

func before
        Crea las conexiones, sesiones, destinos, productores, consumidores y listener necesarios.
Fin func

```

```

func after
        Cierra todas conexiones, productores, consumidores.
Fin func

```

```

func task()

	while (no sea interrumpido){
		if(hay mensajes en colaComandas){
			· Toma la primera comanda.
			· Cocina el plato de la comanda tomada.
			· Almacena la comanda en historialComandas.
			· Envía el plato cocinado al Restaurante mediante producer.
		}
	}

Fin func

```

### Main

#### Funciones:

```

func main()

	Crea el executor.
	Crea el cocinero.
	Ejecuta el cocinero.
	Crea el Restaurante.
	Ejecuta el Restaurante
	
	Espera hasta que haya pasado el tiempo estipulado.
	
	Se muestra la recaudación.
	Se muestra qué pidió cada cliente.
	

Fin func

```

### Proceso principal.

#### Funciones:

```
func main()

	Crea el executor.
	while no haya pasado el tiempo de ejecución{
		Crea un cliente.
		Ejecuta el cliente creado.
	}

Fin func
```
