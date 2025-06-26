package es.uja.ssccdd.curso2122.problemassesion5.grupo5;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Luis López Ruiz (llopez)
 */
public class Deposito implements Callable<ArrayList<Coche>> {
    
    static private int sig_id_deposito;
    private int id_deposito;
    final private ReentrantLock lock_deposito;

    public Deposito(ReentrantLock lock) {
        this.lock_deposito= lock;
        this.lock_deposito.lock();
        this.id_deposito= sig_id_deposito++;
        this.lock_deposito.unlock();
    }
    
    private Coche GeneraCoche(int currentID) {
        int valor_tipo = Utils.random.nextInt(Utils.VALOR_GENERACION);
        return new Coche(currentID, Utils.TipoReserva.getTipoReserva(valor_tipo));
    }

    @Override
    public ArrayList<Coche> call()  {
        System.out.println("Depósito " + this.id_deposito + ": ha empezado a crear coches");

        ArrayList<Coche> cochesGenerados = new ArrayList<>();
        try {   // Si nos interrumpen finalizamos la generación de coches,
                //y devolvemos los coches que hemos generado de manera parcial.

            // Generamos todos los COCHES.
            for (int i = 0; i < Utils.COCHES_A_GENERAR; i++) {
                cochesGenerados.add(this.GeneraCoche(i + 1));

                // Simulamos un tiempo de creación.
                int tiempoAleatorio = Utils.random.nextInt(Utils.TIEMPO_ESPERA_PREPARACION_MAX - Utils.TIEMPO_ESPERA_PREPARACION_MIN) + Utils.TIEMPO_ESPERA_PREPARACION_MIN;
                TimeUnit.MILLISECONDS.sleep(tiempoAleatorio);
            }
        } catch (InterruptedException e) {
            System.out.println("Depósito " + this.id_deposito + ": ha sido interrumpido");
        }

        System.out.println("Depósito " + this.id_deposito + ": ha terminado de crear coches");
        return cochesGenerados;
    }


}
