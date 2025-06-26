package es.uja.ssccdd.curso2122.problemassesion12.grupo5;

import es.uja.ssccdd.curso2122.problemassesion12.grupo5.Utils.TipoReserva;
/**
 *
 * @author José Luis López Ruiz (llopez)
 */
public class Coche {

    private final int iD;
    private final int iD_Creador;
    private final TipoReserva calidadCoche;

    public Coche(int iD, int creador, TipoReserva calidadCoche) {
        this.iD = iD;
        this.iD_Creador= creador;
        this.calidadCoche = calidadCoche;
    }

    public int getiD_Creador() {
        return iD_Creador;
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
