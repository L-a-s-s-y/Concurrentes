/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.primerapractica;

/**
 *
 * @author El Boss
 */
public class Fallo {
    
    /**
     * Identificador único del objeto fallo.
     */
    private final int id_fallo;
    private static int id_sig= 0;
    /**
     * Identificador del proceso que provocó el fallo.
     */
    private final int id_proceso;
    /**
     * Informa si el fallo fue inicial o no.
     */
    private final boolean fallo_inicial;

    /**
     * Constructor de la clase fallo.
     * @param id_proceso int
     * @param fallo_inicial boolean
     */
    public Fallo(int id_proceso, boolean fallo_inicial) {
        Utiles.LOCK_PETICION.lock();
        id_sig++;
        Utiles.LOCK_PETICION.unlock();
        this.id_fallo = id_sig;
        this.id_proceso = id_proceso;
        this.fallo_inicial = fallo_inicial;
    }

    /**
     * Retorna el identificador del fallo.
     * @return int
     */
    public int getId_fallo() {
        return id_fallo;
    }

    /**
     * Retorna el identificador del proceso asociado al fallo.
     * @return int
     */
    public int getId_proceso() {
        return id_proceso;
    }

    /**
     * Retorna true si el fallo fue inicial.
     * Retorna false en caso contrario.
     * @return boolean
     */
    public boolean isFallo_inicial() {
        return fallo_inicial;
    }

    @Override
    public String toString() {
        return "Fallo{" + "id_fallo=" + id_fallo + ", id_proceso=" + id_proceso + ", fallo_inicial=" + fallo_inicial + '}';
    }
    
}