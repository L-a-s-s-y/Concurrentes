package es.uja.ssccdd.curso2122.problemassesion4.grupo5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Luis López Ruiz (llopez)
 */
public class Sesion4 {
    public static void main(String[] args) {
        System.out.println("Hilo principal: Iniciando ejecución");
        System.out.println("------------------------------------");
        ExecutorService executor_deposito= (ExecutorService)Executors.newFixedThreadPool(4);
        ArrayList<Deposito> depositos= new ArrayList<>();
        for (int i = 0; i < Utils.DEPOSITOS_A_GENERAR; i++) {
            Deposito nuevo= new Deposito(i);
            depositos.add(nuevo);
        }
        List<Future<ArrayList<Coche>>> resultados= null;
        try {
            // Lanzamos depósitos.
            resultados= executor_deposito.invokeAll(depositos);
           
        } catch (InterruptedException ex) {
            Logger.getLogger(Sesion4.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         // Esperamos la devolución de los resultados.
        executor_deposito.shutdown();
        ArrayList<ArrayList<Coche>> todos= new ArrayList<>();
        for (int i = 0; i < resultados.size(); i++) {
            Future<ArrayList<Coche>> future= resultados.get(i);
            try {
                ArrayList<Coche> coches= future.get();
//                for (Coche coche: coches) {
//                    System.out.println(coche);
//                }
                todos.add(coches);
            } catch (InterruptedException ex) {
                Logger.getLogger(Sesion4.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(Sesion4.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        // Asignamos a cada gestor los recursos de su depósito.
        ArrayList<Gestor> gestores= new ArrayList<>();
        ArrayList<Coche> no_asignados= new ArrayList<>();
        for (int i = 0; i < Utils.GESTORES_A_GENERAR; i++) {
            ArrayList<Reserva> reservas= new ArrayList<>();
            for (int j = 0; j < Utils.RESERVAS_A_GENERAR; j++) {
                Reserva nueva= new Reserva(j,Utils.MAXIMO_COCHES_POR_RESERVA,Utils.TipoReserva.getTipoReserva(Utils.random.nextInt(Utils.VALOR_GENERACION)));
                reservas.add(nueva);
            }
            Gestor nuevo= new Gestor(i,todos.get(i),no_asignados,reservas);
            gestores.add(nuevo);
        }
        // Lanzamos gestores.
        ExecutorService executor_gestor= (ExecutorService)Executors.newFixedThreadPool(2);
        List<Future<Informe>> resultados_gestor= null;
        try {
            // Lanzamos depósitos.
            resultados_gestor= executor_gestor.invokeAll(gestores);
           
        } catch (InterruptedException ex) {
            Logger.getLogger(Sesion4.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Esperamos la devolución de todos los informes
        executor_gestor.shutdown();
        
        // Imprimimos informes.
        for (int i = 0; i < resultados_gestor.size(); i++) {
            Future<Informe> future= resultados_gestor.get(i);
            try {
                Informe informe= future.get();
                System.out.println(informe);
            } catch (InterruptedException ex) {
                Logger.getLogger(Sesion4.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(Sesion4.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        
        // Finalizamos la ejecución del executer.
        
        System.out.println("------------------------------------");
        System.out.println("Hilo principal: Terminando ejecución");
    }
}
