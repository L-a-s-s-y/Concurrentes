package es.uja.ssccdd.curso2122.problemassesion8.grupo5;

import es.uja.ssccdd.curso2122.problemassesion8.grupo5.Utils.TipoReserva;
import java.util.ArrayList;

/**
 *
 * @author José Luis López Ruiz (llopez)
 */
public class Reserva {

    private final int iD;
    private final int nCoches;
    private final TipoReserva tipoCocheReservado;
    private ArrayList<Coche> cochesReservados;

    /**
     * Genera una nueva reserva
     *
     * @param iD id de la reserva
     * @param nCoches numero de coches reservados
     * @param tipoCocheReservado calidad del coche pedido
     */
    public Reserva(int iD, int nCoches, TipoReserva tipoCocheReservado) {
        this.iD = iD;
        this.nCoches = nCoches;
        this.tipoCocheReservado = tipoCocheReservado;
        cochesReservados = new ArrayList<>();
    }

    public int getiD() {
        return iD;
    }

    public ArrayList<Coche> getCochesReservados() {
        return cochesReservados;
    }

    public TipoReserva getTipoCocheReservado() {
        return tipoCocheReservado;
    }

    /**
     * Asigna un nuevo coche a la reserva
     *
     * @param coche para añadir a la lista
     * @return true si se ha añadido false si no tiene requisito minimo
     */
    public boolean addCoche(Coche coche) {
        boolean resultado = false;

        if (!reservaCompleta() && coche.getCalidadCoche().equals(tipoCocheReservado)) {
            cochesReservados.add(coche);
            resultado = true;
        }

        return resultado;
    }
    
    /**
     * Comprueba si las reserva está completa.
     * 
     * @return true si lo está false si no lo está.
     */
    public boolean reservaCompleta() {
        return (cochesReservados.size() == nCoches);
    }

    @Override
    public String toString() {
        StringBuilder resultado = new StringBuilder();

        resultado.append("Reserva[" + iD + ", tipoReservado=" + tipoCocheReservado + ", cochesSolicitados=" + nCoches + ", faltanPorAsignar='" + (nCoches - cochesReservados.size()) + "']{" + "Coches=[");

        for (Coche coche : cochesReservados) {
            resultado.append(coche.toString() + " -> ");
        }

        resultado.append("FIN]}");

        return resultado.toString();
    }

}
