/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.primerapractica;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author El Boss
 */
public class GestorRecursos implements Runnable{
    
    /**
     * Monitor para tener acceso a las peticiones y registrar los fallos.
     */
    private final Monitor monitor;
    
    /**
     * Lista con los recursos del sistema.
     */
    private final ArrayList<Recurso> recursos;
    /**
     * Numero de recursos disponibles.
     */
    private int recursos_disponibles;

    /**
     * Contructor del Gestor de Recursos
     * @param monitor Monitor
     * @param recursos ArrayList
     */
    public GestorRecursos(Monitor monitor, ArrayList<Recurso> recursos) {
        this.monitor = monitor;
        this.recursos = recursos;
        this.recursos_disponibles= this.recursos.size();
    }
    /**
     * Método para asignar un recurso a una tarea de un proceso.
     * Retorna true en caso de que haya podido asignarlo.
     * Retorna false en caso contrario.
     * @param propietario int
     * @param id_tarea int
     * @return boolean
     */
    private boolean asignarRecurso(int propietario, int id_tarea){
        if(this.recursos_disponibles>0)
            for (int i = 0; i < this.recursos.size(); i++) {
                if(this.recursos.get(i).isDisponibilidad()){
                    this.recursos.get(i).setId_propietario(propietario);
                    this.recursos.get(i).setDisponibilidad(false);
                    this.recursos.get(i).setId_tarea(id_tarea);
                    this.recursos.get(i).rejuvenecer();
                    this.recursos_disponibles--;
                    return true;
                }
            }
        return false;
    }
    
    /**
     * Toma el recurso más antiguo y lo asigna a una tarea de un proceso.
     * @param propietario int
     * @param id_tarea int
     */
    private void robarRecurso(int propietario, int id_tarea){
        int maximo= 0;
        for (int i = 1; i < this.recursos.size(); i++) {
            if(this.recursos.get(i).getLongevidad().compareTo(this.recursos.get(maximo).getLongevidad())<0){
                maximo= i;
            }
        }
        this.recursos.get(maximo).setId_propietario(propietario);
        this.recursos.get(maximo).setDisponibilidad(false);
        this.recursos.get(maximo).setId_tarea(id_tarea);
        this.recursos.get(maximo).rejuvenecer();
    }
    
    /**
     * Dada una petición de asignación, el método la procesa.
     * Siempre que pueda tratará asignar un recurso sin quitarlo a otro proceso.
     * @param peticion Peticion
     */
    private void procesarAsignacion(Peticion peticion){
        if(peticion.getTipo()==Utiles.TipoPeticion.INICIAL || peticion.getTipo()==Utiles.TipoPeticion.ASIGNACION){
            for (Integer id_tarea: peticion.getId_tarea()) {
                if(!this.asignarRecurso(peticion.getId_proceso(), id_tarea)){
                    this.robarRecurso(peticion.getId_proceso(), id_tarea);
                    if(peticion.getTipo()==Utiles.TipoPeticion.INICIAL){
                        this.monitor.incrementarFallo(new Fallo(peticion.getId_proceso(),true));
                    }
                    else{
                        this.monitor.incrementarFallo(new Fallo(peticion.getId_proceso(),false));
                    }
                }
            }
        }
    }
    
    /**
     * Libera todos los recursos asignados al propietario propietario.
     * @param propietario int
     */
    private void liberarRecurso(int propietario){
        for (int i = 0; i < this.recursos.size(); i++) {
            if(this.recursos.get(i).getId_propietario()==propietario){
                this.recursos.get(i).setDisponibilidad(true);
                this.recursos.get(i).setId_propietario(-1);
                this.recursos.get(i).rejuvenecer();
                this.recursos.get(i).setId_tarea(-1);
                this.recursos_disponibles++;
            }
        }
    }
    
    /**
     * Dada una petición de liberación, el método la atiende.
     * @param peticion 
     */
    private void procesarLiberarcion(Peticion peticion){
        if(peticion.getTipo()==Utiles.TipoPeticion.LIBERACION){
            this.liberarRecurso(peticion.getId_proceso());
        }
    }

    /**
     * Mientras no se interrumpa el hilo, el gestor atenderá peticiones de liberación
     * y asignación.
     */
    @Override
    public void run() {
        boolean centinela= true;
        while(centinela){
            try {
                Utiles.LIBERACION.acquire();
                while(this.monitor.hayLiberacion()){
                    Peticion nueva= this.monitor.getSiguienteLiberacion();
                    this.procesarLiberarcion(nueva);
                }
                Utiles.LIBERACION.release();
                while(this.monitor.hayAsignacion()){
                    Utiles.ASIGNACION.acquire();
                    if(this.monitor.hayAsignacion()){
                        Peticion nueva= this.monitor.getSiguienteAsignacion();
                        if(nueva!=null){
                            this.procesarAsignacion(nueva);
                        }
                    }
                    Utiles.ASIGNACION.release();
                }
            } catch (InterruptedException ex) {
                System.out.println("GESTOR: Interrumpido");
                Utiles.LIBERACION.release();
                Utiles.ASIGNACION.release();
                centinela= false;
            }
        }
    }
    
    
}
