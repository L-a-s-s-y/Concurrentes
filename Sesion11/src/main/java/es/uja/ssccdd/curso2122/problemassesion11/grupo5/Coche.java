package es.uja.ssccdd.curso2122.problemassesion11.grupo5;

import es.uja.ssccdd.curso2122.problemassesion11.grupo5.Utils.TipoReserva;
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
    
    public Coche(String fromCSV){
        String[] csvSplit= fromCSV.split(";");
        this.iD= Integer.parseInt(csvSplit[0]);
        this.calidadCoche= TipoReserva.getTipoReserva(Integer.parseInt(csvSplit[1]));
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

    //@Override
    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append("").append(iD);
        sb.append(";").append(calidadCoche.getValue());
        //sb.append();
        return sb.toString();
    }
    
    
}
