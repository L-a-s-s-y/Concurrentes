package es.uja.ssccdd.curso2122.problemassesion11.grupo5;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Luis López Ruiz - llopez@ujaen.es
 */
public class Sesion11 {
    public static void main(String[] args) {
        System.out.println("MAIN: Comienza la ejecución.");
        ExecutorService harry = (ExecutorService) Executors.newFixedThreadPool(Utils.NUM_HILOS);
        ArrayList<Reserva> reservas= new ArrayList<>();
        ArrayList<Deposito> depositos= new ArrayList<>();
        for (int i = 0; i < Utils.NUM_DEPOSITOS; i++) {
            Deposito deposito= new Deposito();
            depositos.add(deposito);
            harry.execute(deposito);
        }
        Gestor gestor= new Gestor(reservas);
        harry.execute(gestor);

        try {
            TimeUnit.SECONDS.sleep(Utils.TIEMPO_MAIN);
        } catch (InterruptedException ex) {
            Logger.getLogger(Sesion11.class.getName()).log(Level.SEVERE, null, ex);
        }
        harry.shutdownNow();
        System.out.println("Coches creados: "+depositos.get(0).getIdCoche());
        System.out.println("MAIN: This is the End...?");
    }
}
