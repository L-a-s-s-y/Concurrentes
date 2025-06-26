package es.uja.ssccdd.curso2122.problemassesion8.grupo5;

import es.uja.ssccdd.curso2122.problemassesion8.grupo5.Utils.TipoReserva;
/**
 *
 * @author José Luis López Ruiz (llopez)
 */
public class Coche implements Comparable<Coche>{

    private int prioridad;
    private final int iD;
    private final TipoReserva calidadCoche;

    public Coche(int iD, TipoReserva calidadCoche) {
        //this.prioridad= 0;
        this.iD = iD;
        this.calidadCoche = calidadCoche;
        if(this.calidadCoche==TipoReserva.BASICO){
            this.prioridad= 1;
        }
        else{
            if(this.calidadCoche==TipoReserva.SUPERIOR){
                this.prioridad= 2;
            }
            else{
                this.prioridad= 3;
            }
        }
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
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

    @Override
    public int compareTo(Coche o) {
        if(this.prioridad>o.prioridad){
            return -1;
        }
        else{
            if(this.prioridad<o.prioridad){
                return 1;
            }
            else{
                return 0;
            }
        }
    }
}
