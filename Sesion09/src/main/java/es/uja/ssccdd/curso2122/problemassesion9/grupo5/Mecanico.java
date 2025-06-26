package es.uja.ssccdd.curso2122.problemassesion9.grupo5;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Luis López Ruiz - llopez@ujaen.es
 */
public class Mecanico implements Runnable{
    private int iD;
    private static AtomicInteger sigID= new AtomicInteger();
    private AtomicIntegerArray inventario;

    public Mecanico( AtomicIntegerArray inventario) {
        //this.sigID = sigID;
        this.inventario = inventario;
        this.iD= sigID.getAndIncrement();
        ThreadLocalRandom.current();
    }

    public int getiD() {
        return iD;
    }

    @Override
    public void run() {
        try{
            while(true){
                int seleccionada= ThreadLocalRandom.current().nextInt(Utils.NUM_PIEZAS);
                if(this.inventario.get(seleccionada)>=0){
                    System.out.println("MECANICO "+this.iD+": Reparando...");
                    this.inventario.getAndDecrement(seleccionada);
                    System.out.println("MECANICO "+this.iD+": Fin de reparar.");
                    TimeUnit.SECONDS.sleep(Utils.FRECUENCIA_MECANICO);
                }
                else{
                    System.out.println("MECANICO "+this.iD+": Fallo, no hay pieza.");
                    TimeUnit.SECONDS.sleep(Utils.ESPERA_REINTENTO);
                }
            }
        } catch (InterruptedException ex) {
            //Logger.getLogger(Mecanico.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("MECANICO "+this.iD+": Interrumpido.");
        }
    }
}
