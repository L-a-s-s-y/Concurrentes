package es.uja.ssccdd.curso2122.problemassesion9.grupo5;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Luis López Ruiz - llopez@ujaen.es
 */
public class Sesion9 {
    public static void main(String[] args) {
        System.out.println("MAIN: Inicio...");
        AtomicIntegerArray inventario= new AtomicIntegerArray(Utils.NUM_PIEZAS);

        for(Utils.Pieza pieza: Utils.Pieza.values()){
            inventario.set(pieza.ordinal(), pieza.getUnidades());
        }
        
        Reponedor reponedor= new Reponedor(inventario);
        ArrayList<Mecanico> mecanicos= new ArrayList<>();
        for (int i = 0; i < Utils.MECANICOS; i++) {
            Mecanico mecanico= new Mecanico(inventario);
            mecanicos.add(mecanico);
        }
        
        ExecutorService harry = (ExecutorService) Executors.newCachedThreadPool();
        
        for (int i = 0; i < 10; i++) {
            harry.execute(mecanicos.get(i));
        }
        harry.execute(reponedor);
        try {
            TimeUnit.SECONDS.sleep(Utils.ESPERA_HILO_PRINCIPAL);
        } catch (InterruptedException ex) {
            Logger.getLogger(Sesion9.class.getName()).log(Level.SEVERE, null, ex);
        }
        harry.shutdownNow();
        System.out.println("MAIN: This is the End...");
        
    }
}
