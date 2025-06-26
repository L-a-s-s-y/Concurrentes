  

## PRIMERA PRACTICA

  
  

# Análisis y Diseño

  

Estructuras de datos, variables compartidas y procedimientos necesarios.

  

## Variables compartidas

  

-  `TipoPeticion: Enum`

Tipo de petición. Tres tipos: ASIGNACION, LIBERACION E INICIAL.

-  `TIEMPO_EJECUCION: int`

Tiempo de ejecución de los hilos hasta su interrupción. Expresado en segundos.

-  `MAX_CREACION_PROCESO: int`

Tiempo máximo que puede tardar el sistema en crear un proceso. Expresado en segundos.

-  `MIN_CREACION_PROCESO: int`

Tiempo mínimo que puede tardar el sistema en crear un proceso. Expresado en segundos..

-  `RECURSOS_TOTALES: int`

Cantidad total de recursos disponibles.

-  `RECURSOS_MAXIMOS: int`

Cantidad de recursos máximos que puede tener asignados un proceso.

-  `RECURSOS_INICIALES: int`

Numero de recursos que se asignan a un proceso cuando se crea.

-  `DURACION_MAXIMA_OPERACION: int`

Maxima duración de una operación. Expresado en segundos.

-  `TAREAS_MINIMAS: int`

Numero mínimo de tareas a realizar por un proceso.

-  `TAREAS_MAXIMAS: int`

Numero máximo de tareas a realizar por un proceso.

-  `OPERACIONES_MINIMAS: int`

Numero mínimo de operaciones que realizará el proceso.

-  `OPERACIONES_MAXIMAS: int`

Numero máximo de operaciones que realizará el proceso.

-  `LOCK: ReentrantLock`

Lock para la generación de ids de las distintas clases. Meramente estético no influye en la resolución de la práctica.

-  `ASIGNACION: Semaphore`

Controla el acceso a la lista de peticiones de asignación.

-  `LIBERACION: Semaphore`

Controla el acceso a la lista de peticiones de liberación.
  

## Clases
* Todas las Clases tienen getters y setters correspondientes.
  

### RECURSO

  

- Variables locales:

  

-  `disponibilidad: boolean`

Informa si el recurso está libre. True: libre; false: ocupado.

-  `longevidad: Date`

Fecha de asignación del recurso.

-  `id_propietario: int`

ID del proceso que tiene en uso el recuros. Si está libre tendrá el valor -1.

-  `id_tarea: int`

ID de la tarea a la cual está asignado el recurso. Si está libre tendrá valor -1.

  

- Funciones:

```

func constructorRecurso

	    disponibilidad= true
        longevidad= new Date()
        id_propietario= -1
        id_tarea= -1

Fin func

```

```

func rejuvenecer

	    longevidad= new Date();

Fin func

```
También los getters y setters necesarios.

  

### Fallo

  

- Variables locales:

  

-  `id_fallo: int`

Identificador único.


-  `id_propietario: int`

ID del proceso para el que se produjo el fallo.

-  `fallo_inicial: int`

True si el fallo fue en la creación del proceso. False en caso contrario.

-  `id_sig: int`

Variable auxiliar para la generación de ids únicos.

- Funciones:
```

func constructorFallo

	    LOCK.lock()
        id_sig++
        LOCK.unlock()
        id_fallo = id_sig
        id_proceso = id_proceso
        fallo_inicial = fallo_inicial

Fin func

```
También los getters y setters necesarios.
  


### Monitor

  

- Variables locales:

  

-  `peticiones_asignacion: ArrayList de Peticion`

Lista de peticiones de asignación.

-  `peticiones_liberacion: ArrayList de Peticion`

Lista de peticiones de liberacion.

-  `recursos: ArrayList de Recurso`

Lista con los recursos y la información de los mismos.

-  `fallos: ArrayList de Fallo`

Lista con los fallos producidos.

- Funciones:

  

```

func constructorMonitor(ArrayList recursos)

	    peticiones_asignacion = new ArrayList
        peticiones_liberacion = new ArrayList
        fallos= new ArrayList
        recursos= ArrayList recursos;

Fin func

```
```

func incrementarFallo (nuevo fallo)

	    añade un fallo a la lista.

Fin func

```
```

func hayAsignacion

	    retorna true si hay una peticion en la lista peticiones_asignación.
	    retorna false en caso contrario.

Fin func

```
Hay una función análoga para peticiones_liberacion.
```

func getSiguienteAsignacion

	  Devuelve la primera peticion de la lista de asignacion.
      Retira dicha peticion de la lista.
      Devuelve null en caso de que no haya ninguna o la lista sea nula.

Fin func

```
Hay una función análoga para peticiones_liberacion.
```

func nuevaAsignacion(Peticion asignacion)

	  Hay una función análoga para peticiones_liberacion.

Fin func

```
Hay una función análoga para peticiones_liberacion.
```

func procesarFallosPorProceso(int id_proceso)

	  int contado= 0;
	  para cada elemento de la lista de fallos{
		  if id_proceso del fallo == id_proceso{
			  contador++
		  }
	  }
	  return contador
Fin func
```
```

func tieneRecurso(int id_tarea)
	retorna true si la tarea con id_tarea tiene un recurso asignado.
	retorna false en caso contrario.
Fin func
```

### Tarea

  

- Variables locales:

-  `id_tarea: int`

Identificador único.

-  `num_operaciones: int`

Número de operaciones que tiene que realizar la tarea.

-  `id_sig: int`

Variable auxiliar para la generación de ids únicos.

- Funciones:

  

```

func constructorTarea
        LOCK.lock()
        id_sig++
        Utiles.LOCK.unlock()
        id_tarea = id_sig
        num_operaciones= 0
Fin func

```

```

func annadirOperacion

	num_operaciones++

Fin func

```

  

### Petición

  

- Variables locales:

  

-  `id_peticion: int`

ID único.

-  `id_proceso: int`

ID del proceso que creó la petición.

-  `id_sig: int`

Variable auxiliar para la generación de ids únicos.


-  `num_recursos: int`

Número de recursos requeridos.

-  `id_Tareas: ArrayList`

Lista con los ids de las tareas a las cuales se les van a asignar recursos.

-  `Tipo: TipoPeticion`

Tipo de la petición.

- Funciones:


```

func constructorPeticion

	crea una petición acorde con el tipo requerido.

Fin func

```

### Gestor de Recursos

  

- Variables locales:

  

-  `monitor: Monitor`

Monitor con las listas de peticiones.


-  `recursos: ArrayList de Recurso`

Lista con los recursos y la información de los mismos.

-  `recursos_disponibles: int`

Cantidad de recursos no ocupados.




- Funciones:


```

func constructorGestorRecursos( nuevo_monitor, los_recursos)

	    monitor = nuevo_monitor
        recursos = los_recursos
        recursos_disponibles= tamaño de la lista de recursos

Fin func

```
```

func asignarRecurso( id_proceso, id_tarea)

	    if(recursos disponibles>0){
		    for i desde 0 hasta tam lista recursos{
			    if(recurso(i) disponible){
				    recurso(i).setId_propietario(id_proceso)
                    recurso(i).setDisponibilidad(false)
                    recurso(i).setId_tarea(id_tarea)
                    recurso(i).rejuvenecer()
                    recursos_disponibles--
                    return true
	            }
		    }
	    }
	    return false

Fin func

```
```

func robarRecurso( id_proceso, id_tarea)

	    int maximo= 0
        for i desde 1 hasta tam lista recursos {
            if(recurso(i) más viejo que recurso(maximo)){
                maximo= i
            }
        }
        recurso(maximo).setId_propietario(id_proceso)
        recurso(maximo).setDisponibilidad(false)
        recurso(maximo).setId_tarea(id_tarea)
        recurso(maximo).rejuvenecer()

Fin func

```
```

func procesarAsignacion(Peticion peticion)
		if(TipoPeticion es ASIGNACION ó INICIAL){
            for cada id_tarea en peticion.lista ids tareas {
                if(asignarRecurso devuelve falso){
                    robarRecurso(peticion.getId_proceso(), id_tarea)
                    añade un fallo.
                    }
                }
            }
        }

Fin func

```
```

func liberarRecurso(int id_proceso)
		
		    for i desde 0 hasta tam lista recursos{
			    if(id_proceso de recurso(i) == id_proceso){
				    recurso(i).setId_propietario(-1)
                    recurso(i).setDisponibilidad(true)
                    recurso(i).setId_tarea(-1)
                    recurso(i).rejuvenecer()
                    recursos_disponibles--
	            }
		    }

Fin func

```
```

func procesarLiberarcion(Peticion peticion)
		
		   if(Tipo de peticion es LIBERACION){
            liberarRecurso(id_proceso de peticion);
        }

Fin func

```
```

func run
        while(hasta que sea interrumpido){
                LIBERACION.wait()
                while(haya peticiones de liberacion en la lista){
                    procesarLiberacion(siguiente en la lista de peticiones);
				}
                LIBERACION.signal()
                while(haya peticiones de asignacion en la lista){
                    ASIGNACION.wait()
                    procesarAsignacion(siguiente en la lista de peticiones);
                    ASIGNACION.signal()
                }
        }
Fin func

```
### Proceso

  

- Variables locales:

  

-  `id_proceso: int`

Identificador único.


-  `monitor: Monitor`

Monitor con las listas de peticiones.

-  `tareas: ArrayList`

Lista con las tareas a realizar.


- Funciones:
```

func constructorProceso(id,num_tareas, nuevo_monitor)

	    id_proceso = id
        monitor= nuevo_monitor
        tareas= new ArrayList
        for i desde 0 hasta num_tareas {
            crea nueva tarea
            la añade a la lista de tareas
        }
        asignarOperaciones()
            ArrayList tareas_iniciales= new ArrayList
            tareas_iniciales.add(id de tareas(0))
            tareas_iniciales.add(id de tareas(1))

            ASIGNACION.wait()
            Peticion inicio= new Peticion(id_proceso, tareas_iniciales)
            monitor.nuevaAsignacion(inicio)
        
        ASIGNACION.signal()

Fin func

```
```

func asignarOperaciones

	    int num_operaciones= aleatorio entre el máximo y el mínimo de operaciones permitidas.
        int contador= 0
        for i desde 0 hasta num_operaciones {
            tareas(contador%tam de tareas).annadirOperacion();
            contador++
        }

Fin func

```
```

func ejecutarTarea(Tarea tarea)
        while(haya operaciones que realizar){
            int duracion= aleatorio entre la duración máxima de una operación y la mínima
            hilo espera(duracion);
            tarea.quitarOperacion();
        }
Fin func

```
```

func run
            for cada tarea en la lista de tareas{
                ejecutarTarea(tarea)
                if(la tarea no tiene recurso asignado){
                    ASIGNACION.wait()
                    Peticion peticion= nueva Peticion de asignación
                    monitor.nuevaAsignacion(peticion)
                    ASIGNACION.signal()
                }
            }
            LIBERACION.wait()
            Peticion peticion= nueva Peticion de liberación
            monitor.nuevaLiberacion(peticion)
	        LIBERACION.signal();
    }
Fin func

```
  

### Proceso Sistema (Hilo Principal)

  

- Funciones:

  

```

func principal
		Date inicio= nueva fecha
        ArrayList recursos= new ArrayList
        for desde i = 0 hasta RECURSOS_TOTALES {
            Recurso nuevo= nuevo Recurso
            recursos.add(nuevo)
        }
        Monitor monitor= nuevo Monitor(recursos)
        GestorRecursos gestor= new GestorRecurosos(monitor)
        ExecutorService harry= nuevo Executor
        harry.ejercutar(gestor)
        contador= 0
        while(durante TIEMPO_EJECUCION){
            int num_tareas= aleatorio entre el numero máximo de tareas y el mínimo
            Proceso nuevo_proceso= new Proceso(contador++,num_tareas,monitor)
            harry.ejecutar(nuevo_proceso)
            int espera= aleatorio entre le tiempo máximo de creacion de un proceso y el minimo.
        }
        harry.terminar ejecucion
        procesarResultados(calculos)

Fin func

```

  



