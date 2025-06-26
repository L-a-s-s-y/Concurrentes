/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.tercerapractica.utils;

import java.util.Random;

/**
 *
 * @author pedroj
 */
public class Constantes {
    
    public static final String DESTINO= "ssccdd.curso2021.J4LB0002.BUZON";
    public static final String PREMIUM= ".PREMIUM";
    public static final String ESTANDAR= ".ESTANDAR";
    public static final String PEDIDO= ".PEDIDO";
    public static final String COCINERO= ".COCINERO";
    public static final String COCINADO= ".COCINADO";
    public static final String SERVIR= ".SERVIR";
    public static final String FIN= ".FIN";
    public static final String CONNECTION = "tcp://suleiman.ujaen.es:8018";
    
   /**
     * En segundos.
     */
    public static final int TIEMPO_LLEGADA_MAX = 10;
    /**
     * En segundos.
     */
    public static final int TIEMPO_LLEGADA_MIN = 0;
    public static final int AFORO = 10;
    public static final int MAX_PLATOS = 5;
    public static final int MIN_PLATOS = 3;
    public static final int PRECIO_MAX = 40;
    public static final int PRECIO_MIN_PREMIUM = 10;
    public static final int PRECIO_MAX_ESTANDAR = 20;
    public static final int PRECIO_MIN = 5;
    public static final int CARTA_TAM= 15;
    public static final int LIMITE_TURNOS= 10;
    public static final double DISTRIBUCION= 0.5;
    /**
     * En milisegundos.
     */
    public static final long TIEMPO_EJECUCION= 60000;
    
    public static final Random aleatorio= new Random();
    /**
     * En segundos.
     */
    public static final int TIEMPO_COCINACION= 1;
    /**
     * En segundos.
     */
    public static final int TIEMPO_MAX_COMICION = 5;
    /**
     * En segundos.
     */
    public static final int TIEMPO_MIN_COMICION = 2;
    
    public enum TipoCliente {
        ESTANDAR, PREMIUM;
    } 
    
}
