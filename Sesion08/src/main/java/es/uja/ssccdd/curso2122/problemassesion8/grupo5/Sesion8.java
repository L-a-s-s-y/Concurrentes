package es.uja.ssccdd.curso2122.problemassesion8.grupo5;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sesion8 {
    public static void main(String[] args) {
        ArrayList<Gestor> gestores= new ArrayList<>();
        ArrayList<Deposito> depositos= new ArrayList<>();
        PriorityBlockingQueue<Coche> coches= new PriorityBlockingQueue<>();
        LinkedBlockingDeque<Reserva> reservas= new LinkedBlockingDeque<>();
        for (int i = 0; i < Utils.RESERVAS_A_GENERAR; i++) {
            int numCoches = Utils.random.nextInt(Utils.MAXIMO_COCHES_POR_RESERVA + 1);
            int valorAleatorio = Utils.random.nextInt(Utils.VALOR_GENERACION);
            reservas.add(new Reserva(i + 1, numCoches, Utils.TipoReserva.getTipoReserva(valorAleatorio)));
        }
        ExecutorService harry = (ExecutorService) Executors.newCachedThreadPool();
        
        for (int i = 0; i < Utils.DEPOSITOS_A_GENERAR; i++) {
            Deposito nuevo= new Deposito(coches);
            depositos.add(nuevo);
            harry.execute(nuevo);
        }
        for (int i = 0; i < Utils.GESTORES_A_GENERAR; i++) {
            Gestor nuevo= new Gestor(coches,reservas);
            gestores.add(nuevo);
            harry.execute(nuevo);  
        }
        try {
            TimeUnit.MILLISECONDS.sleep(Utils.TIEMPO_ESPERA_HILO_PRINCIPAL);
        } catch (InterruptedException ex) {
            Logger.getLogger(Sesion8.class.getName()).log(Level.SEVERE, null, ex);
        }
        harry.shutdownNow();
        try {
            harry.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            Logger.getLogger(Sesion8.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("MAIN: No se han insertado "+coches.size()+" coches.");
        double total_reservas= reservas.size();
        double reservas_completadas= 0.0;
        for (Gestor gestor: gestores) {
            for (Reserva reserva: gestor.getReservas_locales()) {
                total_reservas++;
                if(reserva.reservaCompleta()){
                    reservas_completadas++;
                }
            }
        }
        double porcentaje= (double)(reservas_completadas/total_reservas)*100.0;
        System.out.println("MAIN: Porcentaje reservas completadas: "+porcentaje);
        System.out.println("MAIN: This is the End...");
        
    }
}
