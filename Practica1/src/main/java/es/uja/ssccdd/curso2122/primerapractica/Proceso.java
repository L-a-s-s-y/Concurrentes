/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.primerapractica;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author El Boss
 */
public class Proceso implements Runnable {
    
    /**
     * Registra la duración de la actividad.
     */
    private long duracion;
    /**
     * Registra el momento en que se inicia la ejecución del proceso.
     */
    private Date inicio;
    /**
     * Identificador único del proceso.
     */
    private final int id_proceso;
    /**
     * El monitor donde se almacenan los búfferes de peticiones.
     */
    private final Monitor monitor;
    /**
     * Lista de tareas que tiene que ejecutar el proceso.
     */
    private final ArrayList<Tarea> tareas;

    /**
     * Constructor del proceso. 
     * Hace la solicitud inicial de recursos.
     * Asignará las tareas que el proceso tiene que hacer.
     * @param id_proceso
     * @param num_tareas
     * @param monitor 
     */
    public Proceso(int id_proceso, int num_tareas, Monitor monitor) {
        this.id_proceso = id_proceso;
        this.monitor= monitor;
        this.tareas= new ArrayList<>();
        for (int i = 0; i < num_tareas; i++) {
            Tarea nueva= new Tarea();
            this.tareas.add(nueva);
        }
        this.asignarOperaciones();
            ArrayList<Integer> tareas_iniciales= new ArrayList<>();
            tareas_iniciales.add(this.tareas.get(0).getId_tarea());
            tareas_iniciales.add(this.tareas.get(1).getId_tarea());
        try {
            Utiles.ASIGNACION.acquire();
            Peticion inicio= new Peticion(this.id_proceso, tareas_iniciales);
            this.monitor.nuevaAsignacion(inicio);
        } catch (InterruptedException ex) {
            //Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("PROCESO "+this.id_proceso+": Interrumpido.");

        }
        Utiles.ASIGNACION.release();
        this.inicio= new Date();
        System.out.println("PROCESO "+this.id_proceso+": Proceso creado!");
    }

    /**
     * Retorna el valor de la fecha cuando comenzó la ejecución del proceso.
     * @return 
     */
    public Date getInicio() {
        return inicio;
    }

    /**
     * Retorna la duración del proceso.
     * @return long
     */
    public long getDuracion() {
        return duracion;
    }

    /**
     * Retorna el identificador del proceso.
     * @return int
     */
    public int getId_proceso() {
        return id_proceso;
    }
    
    /**
     * Asigna las operaciones que debe realizar cada tarea del proceso.
     */
    private void asignarOperaciones(){
        int num_operaciones= Utiles.random.nextInt(Utiles.OPERACIONES_MAXIMAS+1-Utiles.OPERACIONES_MINIMAS)+Utiles.OPERACIONES_MINIMAS;
        int contador= 0;
        for (int i = 0; i < num_operaciones; i++) {
            this.tareas.get(contador%this.tareas.size()).annadirOperacion();
            contador++;
        }
    }
    
    /**
     * Ejecuta la tarea requerida.
     * @param tarea Tarea
     * @return boolean
     */
    private boolean ejecutarTarea(Tarea tarea){
        while(tarea.getNum_operaciones()>0){
            int duracion= Utiles.random.nextInt(Utiles.DURACION_MAXIMA_OPERACION)+1;
            try {
                TimeUnit.SECONDS.sleep(duracion);
            } catch (InterruptedException ex) {
                //Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
                //System.out.println("PROCESO "+this.id_proceso+": Interrumpido.");
                Thread.currentThread().interrupt();
                return true;
            }
            tarea.quitarOperacion();
        }
        return false;
    }

    /**
     * Método principal del proceso.
     * Ejecuta todas las tareas asociadas al proceso.
     */
    @Override
    public void run() {
        System.out.println("PROCESO "+this.id_proceso+": Comienza la ejecución.");
        try{
            for(Tarea tarea: this.tareas){
                
                this.ejecutarTarea(tarea);
                if(!this.monitor.tieneRecurso(tarea.getId_tarea())){
                    Utiles.ASIGNACION.acquire();
                    Peticion peticion= new Peticion(this.id_proceso, tarea.getId_tarea());
                    this.monitor.nuevaAsignacion(peticion);
                    Utiles.ASIGNACION.release();
                }
            }
            Utiles.LIBERACION.acquire();
            this.monitor.nuevaLiberacion(new Peticion(this.id_proceso));
            Utiles.LIBERACION.release();
            Date fin= new Date();
            this.duracion= fin.getTime()-inicio.getTime();
            System.out.println("PROCESO "+this.id_proceso+": Termina la ejecución.");
        }
        catch (InterruptedException ex) {
            System.out.println("PROCESO "+this.id_proceso+": Interrumpido.");
            Utiles.ASIGNACION.release();
            this.monitor.incrementarNoCompletados();
            Date fin= new Date();
            this.duracion= fin.getTime()-inicio.getTime();
            Thread.currentThread().interrupt();
        }        
    }
    

}
