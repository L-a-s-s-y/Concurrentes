/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.primerapractica;

import java.util.ArrayList;

/**
 *
 * @author El Boss
 */
public class Monitor {
    
    /**
     * Contiene la lista de peticiones de asignacion de los procesos.
     */
    private final ArrayList<Peticion> peticiones_asignacion;
    
    /**
     * Contiene la lista de peticiones de liberacion de los procesos.
     */
    private final ArrayList<Peticion> peticiones_liberacion;
    
    /**
     * Fallos al intentar asignar recursos.
     */
    private ArrayList<Fallo> fallos;
    /**
     * Lista con los recursos del sistema.
     */
    private final ArrayList<Recurso> recursos;
    /**
     * Contador del número de procesos no completados.
     */
    private int procesos_no_completados;
    /**
     * Contructor de la clase monitor.
     * El contador de fallos iniciamente será 0.
     * @param recursos ArrayList de recursos
     */
    public Monitor( ArrayList<Recurso> recursos) {
        this.peticiones_asignacion = new ArrayList<>();
        this.peticiones_liberacion = new ArrayList<>();
        this.fallos= new ArrayList<>();
        this.recursos= recursos;
        this.procesos_no_completados= 0;
    }
    
    /**
     * Añade uno al número de procesos no completados.
     */
    void incrementarNoCompletados(){
        this.procesos_no_completados++;
    }

    /**
     * Retorna el numero de procesos no completados.
     * @return 
     */
    public int getProcesos_no_completados() {
        return procesos_no_completados;
    }

    /**
     * Retorna la lista de fallos.
     * @return 
     */
    public ArrayList<Fallo> getFallos() {
        return fallos;
    }
    
    /**
     * Incrementa en 1 el número de fallos.
     * @param fallo Fallo
     */
    public void incrementarFallo(Fallo fallo){
        this.fallos.add(fallo);
    }
    
    /**
     * Mira si la lista de peticiones de asignacion esta vacia.
     * @return Booleano
     */
    public boolean hayAsignacion(){
        return !this.peticiones_asignacion.isEmpty();
    }
    
    /**
     * Mira si la lista de peticiones de liberacion esta vacia.
     * @return Booleano
     */
    public boolean hayLiberacion(){
        return !this.peticiones_liberacion.isEmpty();
    }
    
    /**
     * Devuelve la primera peticion de la lista de asignacion.
     * No retira la peticion de la lista.
     * Devuelve null en caso de que no haya ninguna o la lista sea nula.
     * @return Peticion || null
     */
    public Peticion miraSiguienteAsignacion(){
        if(this.peticiones_asignacion != null){
            if(!this.peticiones_asignacion.isEmpty()){
                return this.peticiones_asignacion.get(0);
            }
        }
        
        return null;
    }
    
    /**
     * Devuelve la primera peticion de la lista de asignacion.
     * Retira dicha peticion de la lista.
     * Devuelve null en caso de que no haya ninguna o la lista sea nula.
     * @return Peticion || null
     */
    public Peticion getSiguienteAsignacion(){
        if(this.peticiones_asignacion != null){
            if(!this.peticiones_asignacion.isEmpty()){
                return this.peticiones_asignacion.remove(0);
            }
        }
        
        return null;
    }
    
    /**
     * Añade una nueva peticion a la lista de asignación.
     * @param asignacion Peticion
     */
    public void nuevaAsignacion(Peticion asignacion){
        if(asignacion.getTipo()==Utiles.TipoPeticion.ASIGNACION || asignacion.getTipo()==Utiles.TipoPeticion.INICIAL){
            if(this.peticiones_asignacion != null){
                this.peticiones_asignacion.add(asignacion);
            }
        }
    }
    
    /**
     * Devuelve la primera peticion de la lista de liberacion.
     * No retira la peticion de la lista.
     * Devuelve null en caso de que no haya ninguna o la lista sea nula.
     * @return Peticion || null
     */
    public Peticion miraSiguienteLiberacion(){
        if(this.peticiones_liberacion != null){
            if(!this.peticiones_liberacion.isEmpty()){
                return this.peticiones_liberacion.get(0);
            }
        }
        
        return null;
    }
    
    /**
     * Devuelve la primera peticion de la lista de liberacion.
     * Retira dicha peticion de la lista.
     * Devuelve null en caso de que no haya ninguna o la lista sea nula.
     * @return Peticion || null
     */
    public Peticion getSiguienteLiberacion(){
        if(this.peticiones_liberacion != null){
            if(!this.peticiones_liberacion.isEmpty()){
                return this.peticiones_liberacion.remove(0);
            }
        }
        return null;
    }
    
    /**
     * Añade una nueva peticion a la lista de liberacion.
     * @param liberacion Peticion
     */
    public void nuevaLiberacion(Peticion liberacion){
        if(liberacion.getTipo()==Utiles.TipoPeticion.LIBERACION){
            if(this.peticiones_asignacion != null){
                this.peticiones_asignacion.add(liberacion);
            }
        }
    }
    
    /**
     * Comprueba si la tarea en cuestión tiene un recurso asignado.
     * Retorna true en caso de que la tarea tenga dicho recurso asignado.
     * Retorna false en caso contrario.
     * @param id_tarea int
     * @return boolean
     */
    public boolean tieneRecurso(int id_tarea){
        for (Recurso recurso: this.recursos) {
            if(recurso.getId_tarea()==id_tarea){
                return true;
            }
        }
        return false;
    }
}
