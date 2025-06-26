package es.uja.ssccdd.curso2122.problemassesion9.grupo5;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Luis López Ruiz - llopez@ujaen.es
 */
public class Reponedor implements Runnable{
    
    private AtomicIntegerArray inventario;

    public Reponedor(AtomicIntegerArray inventario) {
        this.inventario = inventario;
    }

    @Override
    public void run() {
        System.out.println("REPONEDOR: Comienza");
        try{
            while(true){
                TimeUnit.SECONDS.sleep(Utils.FRECUENCIA_REPUESTOS);
                System.out.println("REPONEDOR: Reponiendo...");
                for(Utils.Pieza pieza: Utils.Pieza.values()){
                    this.inventario.getAndSet(pieza.ordinal(), pieza.getUnidades());
                }
                System.out.println("REPONEDOR: Fin de reponer.");
            } 
        } catch (InterruptedException ex) {
            //Logger.getLogger(Reponedor.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("REPONEDOR: Interrumpido");
        }
    }

}
