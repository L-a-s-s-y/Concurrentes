package es.uja.ssccdd.curso2122.problemassesion9.grupo5;

/**
 *
 * @author José Luis López Ruiz - llopez@ujaen.es
 */
public class Utils {
    
    public static final int MECANICOS= 10;
    public static final int ESPERA_HILO_PRINCIPAL= 30;//SEGUNDOS
    public static final int FRECUENCIA_MECANICO= 1;//SEGUNDOS
    public static final int FRECUENCIA_REPUESTOS= 10;//SEGUNDOS
    public static final int ESPERA_REINTENTO= 2;//SEGUNDOS
    
    public enum Pieza { 
        BUJIAS(10), EMBRAGUES(2), BATERIAS(1), FRENOS(3), NEUMATICOS(4);
        
        private int unidades;

        private Pieza(int unidades) {
            this.unidades = unidades;
        }

        public int getUnidades() {
            return unidades;
        }
    }
    public static final int NUM_PIEZAS = Pieza.values().length;

}
