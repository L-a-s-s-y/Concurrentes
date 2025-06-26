package es.uja.ssccdd.curso2122.problemassesion4.grupo5;

import es.uja.ssccdd.curso2122.problemassesion4.grupo5.Utils.TipoReserva;
/**
 *
 * @author José Luis López Ruiz (llopez)
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
