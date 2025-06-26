package es.uja.ssccdd.curso2122.problemassesion8.grupo5;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Luis López Ruiz (llopez)
 */
public class Deposito implements Runnable {
    
    private final int id_deposito;
    private static int sig_id= 0;
    private PriorityBlockingQueue<Coche> list_coches;

    public Deposito(PriorityBlockingQueue<Coche> list_coches) {
        Utils.lock_ids.lock();
        sig_id++;
        this.id_deposito= sig_id;
        Utils.lock_ids.unlock();
        this.list_coches = list_coches;
    }

    @Override
    public void run() {
        System.out.println("DEPOSITO "+this.id_deposito+": Comienza la preparación de coches");
        boolean centinela= true;
        int contador= 0;
        while(centinela){
            Coche nuevo= new Coche(contador++,Utils.TipoReserva.getTipoReserva(Utils.random.nextInt(Utils.VALOR_GENERACION)));
            try {
            this.list_coches.
            
                TimeUnit.MILLISECONDS.sleep(Utils.random.nextInt(Utils.random.nextInt(Utils.TIEMPO_ESPERA_PREPARACION_MAX - Utils.TIEMPO_ESPERA_PREPARACION_MIN) + Utils.TIEMPO_ESPERA_PREPARACION_MIN));
            } catch (InterruptedException ex) {
                //Logger.getLogger(Deposito.class.getName()).log(Level.SEVERE, null, ex);
                centinela= false;
            }
        }
    }
}

