/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.primerapractica;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author El Boss
 */
public class Utiles {
    public static Random random = new Random();

    // Constantes del problema
    /**
     * En segundos.
     */
    public static final int TIEMPO_EJECUCION= 120;
    public static final int MAX_CREACION_PROCESO= 3;
    public static final int MIN_CREACION_PROCESO= 1;
    public static final int RECURSOS_TOTALES = 20;
    public static final int RECURSOS_MAXIMOS = 4;
    public static final int RECURSOS_INICIALES = 2; // Valor máximo
    public static final int DURACION_MAXIMA_OPERACION = 2;
    public static final int TAREAS_MINIMAS = 4;
    public static final int TAREAS_MAXIMAS = 8;
    public static final int OPERACIONES_MINIMAS = 8;
    public static final int OPERACIONES_MAXIMAS = 12;
    public static final ReentrantLock LOCK_PETICION = new ReentrantLock();
    //public static final Semaphore ESPERA= new Semaphore(0,true);
    public static final Semaphore ASIGNACION= new Semaphore(1);
    public static final Semaphore LIBERACION= new Semaphore(1);

    //Enumerado para el tipo de calidad de impresión
    public enum TipoPeticion {
            ASIGNACION, LIBERACION, INICIAL;
        }
    }