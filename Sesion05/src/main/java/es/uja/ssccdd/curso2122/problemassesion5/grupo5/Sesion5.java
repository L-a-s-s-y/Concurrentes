package es.uja.ssccdd.curso2122.problemassesion5.grupo5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Luis López Ruiz (llopez)
 */
public class Sesion5 {
    public static void main(String[] args) {
        // Lanzamos todos los hilos.
        System.out.println("Hilo principal: Iniciando ejecución");
        System.out.println("------------------------------------");
        
        // Creamos todo lo necesario.
        ExecutorService executor = (ExecutorService) Executors.newCachedThreadPool();
        CompletionService<ArrayList<Coche>> completionDepositos= new ExecutorCompletionService<>(executor);

        ArrayList<Deposito> depositos = new ArrayList<>();
        ReentrantLock lock= new ReentrantLock();
        for (int i = 0; i < Utils.DEPOSITOS_A_GENERAR; i++) {
            depositos.add(new Deposito(lock));
        }
        for (int i = 0; i < depositos.size(); i++) {
            completionDepositos.submit(depositos.get(i));
        }
        
        ArrayList<Reserva> reservas = null;
        ArrayList<Coche> cochesDesechados = new ArrayList<>();

        ArrayList<Gestor> gestores = new ArrayList<>();
        for (int i = 0; i < Utils.GESTORES_A_GENERAR; i++) {
            // - Creación de reservas.
            reservas = new ArrayList<>();
            for (int j = 0; j < Utils.RESERVAS_A_GENERAR; j++) {
                int numCoches = Utils.random.nextInt(Utils.MAXIMO_COCHES_POR_RESERVA + 1);
                int valorAleatorio = Utils.random.nextInt(Utils.VALOR_GENERACION);
                reservas.add(new Reserva(j + 1, numCoches, Utils.TipoReserva.getTipoReserva(valorAleatorio)));
            }

            gestores.add(new Gestor( cochesDesechados, reservas,completionDepositos,lock));
        }

        List<Future<Informe>> informes = null;
        try {
            informes= executor.invokeAll(gestores);
        } catch (InterruptedException e) {
            System.out.println("Ha ocurrido algún problema al esperar el informe");
        }
        
        try {
            executor.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(Sesion5.class.getName()).log(Level.SEVERE, null, ex);
        }
        executor.shutdownNow();
        
        for (int i = 0; i < informes.size(); i++) {
            try {
                System.out.println(informes.get(i).get().toString());
            } catch (InterruptedException | ExecutionException ex) {
                System.out.println("Ha ocurrido algún problema al imprimir el informe " + (i + 1));
            }
        }

        
      
  } 
}
