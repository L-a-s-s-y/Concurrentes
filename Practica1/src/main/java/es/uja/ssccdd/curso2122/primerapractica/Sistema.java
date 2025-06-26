/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.primerapractica;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author El Boss
 */
public class Sistema implements Runnable {
    
    /**
     * Executor para los procesos.
     */
    private ExecutorService executor;
    /**
     * Guarda el número de procesos que se crean.
     */
    private int contador;

    /**
     * Constructor del sistema.
     * @param executor ExecutorService
     */
    public Sistema(ExecutorService executor) {
        this.executor = executor;
        contador= 0;
    }

    /**
     * Hilo principal del sistema.
     * Crea el monitor, los recursos, y el gestor de recursos.
     * Crea y manda procesos al ejecutor mientras dure la ejecución.
     * Cuando termine la ejecución procesa los resultados.
     */
    @Override
    public void run() {
        Date inicio= new Date();
        ArrayList<Recurso> recursos= new ArrayList<>();
        for (int i = 0; i < Utiles.RECURSOS_TOTALES; i++) {
            Recurso nuevo= new Recurso();
            recursos.add(nuevo);
        }
        Monitor monitor= new Monitor(recursos);
        GestorRecursos gestor= new GestorRecursos(monitor,recursos);
        ArrayList<Proceso> procesos= new ArrayList<>();
        this.executor.execute(gestor);
        

        boolean centinela= true;
        while(centinela && !this.executor.isShutdown()){
            int num_tareas= Utiles.random.nextInt(Utiles.TAREAS_MAXIMAS+1-Utiles.TAREAS_MINIMAS)+Utiles.TAREAS_MINIMAS;
            Proceso nuevo_proceso= new Proceso(contador++,num_tareas,monitor);
            procesos.add(nuevo_proceso);
            this.executor.execute(nuevo_proceso);
            int espera= Utiles.random.nextInt(Utiles.MAX_CREACION_PROCESO+1-Utiles.MIN_CREACION_PROCESO)+Utiles.MIN_CREACION_PROCESO;
            try {
                TimeUnit.SECONDS.sleep(espera);
            } catch (InterruptedException ex) {
                centinela= false;
                System.out.println("SISTEMA: Interrumpido");
            }
        }
        try {
            this.executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
        }
        ArrayList<Fallo> fallos= monitor.getFallos();

        this.procesarResultados(monitor, procesos);
        Date fin= new Date();
        System.out.println("Tiempo inicio: "+inicio);
        System.out.println("Tiempo fin: "+fin);
        System.out.println("Tiempo de ejecucion: "+(fin.getTime()-inicio.getTime())+" milliseconds");
        System.out.println("THIS IS THE END...");
    }
    
    private void procesarResultados(Monitor monitor, ArrayList<Proceso> procesos){
        int suma= 0;
        int fallosPorProceso[]= new int[contador];
        for (int i = 0; i < fallosPorProceso.length; i++) {
            fallosPorProceso[i]= 0;
        }
        for (Fallo fallo: monitor.getFallos()) {
            fallosPorProceso[fallo.getId_proceso()]++;
            suma++;
        }
        for (Proceso proceso: procesos) {
            System.out.println("*****************************************************");
            System.out.println("PROCESO "+proceso.getId_proceso()+": Inicio: "+proceso.getInicio());
            System.out.println("PROCESO "+proceso.getId_proceso()+": Duracion: "+proceso.getDuracion()+" milliseconds");
            System.out.println("PROCESO "+proceso.getId_proceso()+": Numero de fallos: "+fallosPorProceso[proceso.getId_proceso()]);
            System.out.println("*****************************************************");
        }
        double media= (double)(suma/contador);
        System.out.println("Media de fallos: "+media);
        System.out.println("Procesos no completados: "+monitor.getProcesos_no_completados());
        
    }
    
}
