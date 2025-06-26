package es.uja.ssccdd.curso2122.problemassesion11.grupo5;

import java.util.Random;

/**
 *
 * @author José Luis López Ruiz - llopez@ujaen.es
 */
public class Utils {
    public static Random random = new Random();
    
    public static String USERNAME = "jalb0002";
    public static String QUEUE = "2122.problemassesion11.grupo5." + USERNAME;
    public static String CONNECTION = "tcp://suleiman.ujaen.es:8018";
    
    public static final int TIEMPO_CREAR_COCHE= 2;
    public static final int TIEMPO_MAIN= 30;
    public static final int NUM_HILOS= 6;
    public static final int NUM_RESERVAS= 100;
    public static final int NUM_DEPOSITOS= 5;

    public static final int VALOR_GENERACION = 101; // Valor máximo
    public static final int TOTAL_TIPOS = TipoReserva.values().length;
    public static final int MAXIMO_COCHES_POR_RESERVA = 3;
    //static int NUM_DEPOSITOS;

    //Enumerado para el tipo de reserva
    public enum TipoReserva {
        BASICO(25), SUPERIOR(75), PREMIUM(100);

        private final int valor;

        private TipoReserva(int valor) {
            this.valor = valor;
        }
        
        public int getValue(){
            return valor;
        }

        /**
         * Obtenemos un tipo de reserva relacionada con su valor de
         * generación
         *
         * @param valor, entre 0 y 100, de generación de tipos
         * @return el tipo de reserva con el valor de generación
         */
        public static TipoReserva getTipoReserva(int valor) {
            TipoReserva resultado = null;
            TipoReserva[] reservas = TipoReserva.values();
            int i = 0;

            while ((i < reservas.length) && (resultado == null)) {
                if (reservas[i].valor >= valor) {
                    resultado = reservas[i];
                }

                i++;
            }

            return resultado;
        }
    }
}
