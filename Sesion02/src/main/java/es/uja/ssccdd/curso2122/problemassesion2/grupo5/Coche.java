/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.problemassesion2.grupo5;

import es.uja.ssccdd.curso2122.problemassesion2.grupo5.Utils.TipoReserva;
/**
 *
 * @author Adrian Luque Luque (alluque)
 */
public class Coche {

    private final int iD;
    private final TipoReserva calidadCoche;

    public Coche(int iD, TipoReserva calidadCoche) {
        this.iD = iD;
        this.calidadCoche = calidadCoche;
    }

    public int getiD() {
        return iD;
    }

    public TipoReserva getCalidadCoche() {
        return calidadCoche;
    }
    
    @Override
    public String toString() {
        return "Coche{" + "iD= " + iD + ", calidadCoche= " + calidadCoche + "}";
    }
}
