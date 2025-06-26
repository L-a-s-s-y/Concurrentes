/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.problemassesion1.grupo5;

import java.util.Random;

/**
 *
 * @author Adrian Luque Luque (alluque)
 */
public class Utils {

    public static Random random = new Random();
    
    // Constantes del problema
    public static final int RESERVAS_A_GENERAR = 10;
    public static final int VALOR_GENERACION = 101; // Valor máximo
    public static final int TOTAL_TIPOS = TipoReserva.values().length;
    public static final int MAXIMO_COCHES_POR_RESERVA = 3;
    public static final int COCHES_A_GENERAR = 20;

    //Enumerado para el tipo de reserva
    public enum TipoReserva {
        BASICO(25), SUPERIOR(75), PREMIUM(100);

        private final int valor;

        private TipoReserva(int valor) {
            this.valor = valor;
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
