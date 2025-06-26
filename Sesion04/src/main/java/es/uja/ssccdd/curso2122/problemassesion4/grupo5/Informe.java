
package es.uja.ssccdd.curso2122.problemassesion4.grupo5;

/**
 *
 * @author José Luis López Ruiz (llopez)
 */
public class Informe {
    private long id_hilo;
    private int reservas_completadas;
    private float porcentaje_reservas;
    private int coches_en_reserva;

    @Override
    public String toString() {
        return "Informe{" + "id_hilo=" + id_hilo + ", reservas_completadas=" + reservas_completadas + ", porcentaje_reservas=" + porcentaje_reservas + ", coches_en_reserva=" + coches_en_reserva + '}';
    }
   

    public Informe(long id_hilo, int reservas_completadas, float porcentaje_reservas, int coches_en_reserva) {
        this.id_hilo = id_hilo;
        this.reservas_completadas = reservas_completadas;
        this.porcentaje_reservas = porcentaje_reservas;
        this.coches_en_reserva = coches_en_reserva;
       
    }

    

    
    
    
    
    
}
