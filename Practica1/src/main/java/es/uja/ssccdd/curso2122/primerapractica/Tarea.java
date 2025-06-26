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
public class Tarea {
    
    /**
     * Identificador único de la tarea.
     */
    private final int id_tarea;
    private static int id_sig= 0;
    /**
     * Número de operaciones que tiene asignada la tarea.
     */
    private int num_operaciones;

    /**
     * Contructor de la tarea.
     */
    public Tarea() {
        Utiles.LOCK_PETICION.lock();
        id_sig++;
        Utiles.LOCK_PETICION.unlock();
        this.id_tarea = id_sig;
        this.num_operaciones= 0;
    }
    
    /**
     * Añade una operación a realizar.
     */
    public void annadirOperacion(){
        this.num_operaciones++;
    }
    
    /**
     * Modifica el número de operaciones que tiene que realizar una tarea.
     * @param num 
     */
    public void setNum_operaciones(int num){
        this.num_operaciones= num;
    }
    
    /**
     * Si el número de operaciones es mayor que 0 resta una operación a realizar.
     */
    public void quitarOperacion(){
        if(this.num_operaciones>0)
            this.num_operaciones--;
    }

    /**
     * Retorna el identificador de la tarea.
     * @return int
     */
    public int getId_tarea() {
        return id_tarea;
    }

    /**
     * Retorna el número de operaciones que tienen que realizarse para completar la taera.
     * @return int
     */
    public int getNum_operaciones() {
        return num_operaciones;
    }
    
    
    
    
}

