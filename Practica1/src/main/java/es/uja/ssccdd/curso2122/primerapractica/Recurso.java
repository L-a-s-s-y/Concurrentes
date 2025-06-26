/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.primerapractica;

import java.util.Date;

/**
 *
 * @author El Boss
 */
public class Recurso {
    /**
     * Informa si el recurso está disponible.
     */
    private boolean disponibilidad;
    //private int longevidad;
    /**
     * Guarda la última fecha de asignación.
     */
    private Date longevidad;
    /**
     * Identificador del proceso que tiene el recurso.
     */
    private int id_propietario;
    /**
     * Identificador de la tarea que está usando el recurso.
     */
    private int id_tarea;

    /**
     * Contructor del recurso.
     */
    public Recurso() {
        this.disponibilidad= true;
        this.longevidad= new Date();
        this.id_propietario= -1;
        this.id_tarea= -1;
    }

    /**
     * Permite modificar el identificador de la tarea que usa el recurso.
     * @param id_tarea int
     */
    public void setId_tarea(int id_tarea) {
        this.id_tarea = id_tarea;
    }

    /**
     * Retorna el el identificador de la tarea que usa el recurso.
     * @return int
     */
    public int getId_tarea() {
        return id_tarea;
    }
    
    /**
     * Permite modificar el identificador del porceso que usa el recurso.
     * @param id_propietario int
     */
    public void setId_propietario(int id_propietario) {
        this.id_propietario = id_propietario;
    }

    /**
     * Retorna el identificador del proceso que usa el recurso.
     * @return int
     */
    public int getId_propietario() {
        return id_propietario;
    }

    /**
     * Retorna true en caso de que el recuros esté libre, false en caso contrario.
     * @return boolean
     */
    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    /**
     * Retorna la fecha en la que se asignó el recurso por última vez.
     * @return date
     */
    public Date getLongevidad() {
        return longevidad;
    }

    /**
     * Permite modificar si el recurso está o no en uso.
     * @param disponibilidad boolean
     */
    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }
    
    /**
     * Refresca la fecha de asignación del recurso.
     */
    public void rejuvenecer(){
        this.longevidad= new Date();
    }
}
