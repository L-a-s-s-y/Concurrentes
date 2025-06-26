/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.problemassesion2.grupo5;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author UJA
 */
public class Sesion2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //System.out.println("FULLY AUTOMATED LUXURY GAY SPACE COMMUNISM");
//        for (int i = 0; i < 100; i++) {
//            int result= Utils.COCHES_A_GENERAR_MAX-Utils.random.nextInt(Utils.COCHES_A_GENERAR_MAX-Utils.COCHES_A_GENERAR_MIN+1);
//            System.out.println("MAIN: Probando el random: "+result);
//        }
        System.out.println("MAIN: COMIENZA LA EJECUCIÓN!");
        ArrayList<Coche> descartados= new ArrayList<>();
        ArrayList<Gestor> gestores= new ArrayList<>();
        for (int i = 0; i < Utils.GESTORES_A_GENERAR; i++) {
            Gestor gestor= new Gestor(i,descartados);
            for (int j = 0; j < Utils.RESERVAS_A_GENERAR; j++) {
                Reserva reserva= new Reserva(i,Utils.random.nextInt(Utils.MAXIMO_COCHES_POR_RESERVA)+1,Utils.TipoReserva.getTipoReserva(Utils.random.nextInt(Utils.VALOR_GENERACION)));
                gestor.asignarReserva(reserva);
            }
            int coches_a_generar= Utils.COCHES_A_GENERAR_MAX-Utils.random.nextInt(Utils.COCHES_A_GENERAR_MAX-Utils.COCHES_A_GENERAR_MIN+1);
            for (int j = 0; j < coches_a_generar; j++) {
                Coche coche= new Coche(i,Utils.TipoReserva.getTipoReserva(Utils.random.nextInt(Utils.VALOR_GENERACION)));
                gestor.asignarCoche(coche);
            }
            gestores.add(gestor);
        }
        
        ArrayList<Thread> hilos= new ArrayList<>();
        for (int i = 0; i < gestores.size(); i++) {
            Thread hilo= new Thread(gestores.get(i));
            hilos.add(hilo);
        }
        for (int i = 0; i < hilos.size(); i++) {
            hilos.get(i).start();
        }
        System.out.println("MAIN: El hilo principal espera.");
        try {
            TimeUnit.SECONDS.sleep(Utils.TIEMPO_ESPERA_HILO_PRINCIPAL);
        } catch (InterruptedException ex) {
            Logger.getLogger(Sesion2.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("MAIN: Se interrumpen los gestores.");
        for (int i = 0; i < hilos.size(); i++) {
            hilos.get(i).interrupt();
            try {
                hilos.get(i).join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Sesion2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("MAIN: coches descartados:");
        for (int i = 0; i < descartados.size(); i++) {
            System.out.println(descartados.get(i).toString());
        }
        System.out.println("MAIN: Fin!");
    }
    
}
