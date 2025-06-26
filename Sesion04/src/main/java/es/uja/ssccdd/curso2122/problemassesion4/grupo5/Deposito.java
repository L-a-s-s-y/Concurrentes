package es.uja.ssccdd.curso2122.problemassesion4.grupo5;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Luis López Ruiz (llopez)
 */
public class Deposito implements Callable<ArrayList<Coche>>{
    
    private int id_deposito;

    public Deposito(int id_deposito) {
        this.id_deposito = id_deposito;
    }

    @Override
    public ArrayList<Coche> call() throws Exception {
        ArrayList<Coche> generados_deposito= new ArrayList<>();
        System.out.println("DEPOSITO "+this.id_deposito+": Comienza la creación de coches.");
        for (int i = 0; i < Utils.COCHES_A_GENERAR; i++) {
            Coche nuevo= new Coche(i,Utils.TipoReserva.getTipoReserva(Utils.random.nextInt(Utils.VALOR_GENERACION)));
            generados_deposito.add(nuevo);
            try {
                TimeUnit.MILLISECONDS.sleep(Utils.TIEMPO_ESPERA_PREPARACION_MAX-Utils.random.nextInt(Utils.TIEMPO_ESPERA_PREPARACION_MAX-Utils.TIEMPO_ESPERA_PREPARACION_MIN+1));
            } catch (InterruptedException ex) {
                Logger.getLogger(Deposito.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("DEPOSITO "+this.id_deposito+": Finaliza la creación de coches.");
        return generados_deposito;
    }
    
    
}
