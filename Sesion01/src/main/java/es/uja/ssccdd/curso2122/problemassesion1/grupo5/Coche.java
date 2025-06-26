/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.problemassesion1.grupo5;

import es.uja.ssccdd.curso2122.problemassesion1.grupo5.Utils.TipoReserva;

/**
 *
 * @author El Boss
 */
public class Coche {
    
    private final int id_coche;
    private final TipoReserva gama_coche;

    public Coche(int id_coche, TipoReserva gama_coche) {
        this.id_coche = id_coche;
        this.gama_coche = gama_coche;
    }

    public int getId_coche() {
        return id_coche;
    }

    public TipoReserva getGama_coche() {
        return gama_coche;
    }

    @Override
    public String toString() {
        return "Coche{" + "id_coche=" + id_coche + ", gama_coche=" + gama_coche + '}';
    }
    
}
