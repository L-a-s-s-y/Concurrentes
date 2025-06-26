/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.primerapractica;

import es.uja.ssccdd.curso2122.primerapractica.Utiles.TipoPeticion;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author El Boss
 */
public class Peticion {
    /**
     * Identificador único de la petición.
     */
    private final int id_peticion;
    private static int id_sig= 0;
    /**
     * Identificador del proceso que hizo la petición.
     */
    private final int id_proceso;
    /**
     * Numero de recursos que se piden en la petición.
     */
    private final int num_recursos;
    /**
     * Tareas a las que se van a asignar los recuros.
     */
    private final ArrayList<Integer> id_tareas;
    /**
     * Tipo de la petición.
     */
    private final TipoPeticion tipo;

    /**
     * Constructor usual para peticiones de tipo Asignacion.
     * @param id_proceso int
     * @param id_tarea int
     */
    public Peticion(int id_proceso, int id_tarea) {
        this.id_tareas= new ArrayList<>();
        Utiles.LOCK_PETICION.lock();
        id_sig++;
        Utiles.LOCK_PETICION.unlock();
        this.id_peticion = id_sig;
        this.id_proceso = id_proceso;
        this.id_tareas.add(id_tarea);
        this.num_recursos= this.id_tareas.size();
        this.tipo = Utiles.TipoPeticion.ASIGNACION;
    }
    
    /**
     * Constructor para peticiones iniciales de tipo Asignacion.
     * @param id_proceso int
     * @param id_tarea int
     */
    public Peticion(int id_proceso, ArrayList<Integer> id_tarea) {
        Utiles.LOCK_PETICION.lock();
        id_sig++;
        Utiles.LOCK_PETICION.unlock();
        this.id_peticion = id_sig;
        this.id_proceso = id_proceso;
        this.id_tareas= id_tarea;
        this.num_recursos= this.id_tareas.size();
        this.tipo = Utiles.TipoPeticion.INICIAL;
    }
    
    /**
     * Constructor para peticiones de tipo liberacion.
     * @param id_proceso int
     */
    public Peticion(int id_proceso) {
        this.id_tareas= null;
        Utiles.LOCK_PETICION.lock();
        id_sig++;
        Utiles.LOCK_PETICION.unlock();
        this.id_peticion = id_sig;
        this.id_proceso = id_proceso;
        this.num_recursos= 0;
        this.tipo = Utiles.TipoPeticion.LIBERACION;
    }

    /**
     * Devuelve la lista con los ids de las tareas.
     * @return ArrayList
     */
    public ArrayList<Integer> getId_tarea() {
        return this.id_tareas;
    }

    /**
     * Devuelve el numero de recursos para asignar.
     * @return int
     */
    public int getNum_recursos() {
        return num_recursos;
    }

    /**
     * Devuelve el id de la peticion.
     * @return int
     */
    public int getId_peticion() {
        return id_peticion;
    }

    /**
     * Devuelve el id del proceso que creó la petición.
     * @return int
     */
    public int getId_proceso() {
        return id_proceso;
    }

    /**
     * Devuelve el tipo de la peticion.
     * @return TipoPeticion
     */
    public TipoPeticion getTipo() {
        return tipo;
    } 
}
