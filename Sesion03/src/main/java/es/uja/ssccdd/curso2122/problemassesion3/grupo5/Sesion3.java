/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.problemassesion3.grupo5;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author UJA
 */
public class Sesion3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("MAIN: empieza");
        CyclicBarrier barrier1= new CyclicBarrier(Utils.GESTORES_A_GENERAR+Utils.GESTORES_A_GENERAR);
        CyclicBarrier barrier2= new CyclicBarrier(Utils.GESTORES_A_GENERAR+Utils.GESTORES_A_GENERAR);
        ArrayList<Gestor> gestores= new ArrayList<>();
        ArrayList<Deposito> depositos= new ArrayList<>();
        ArrayList<Coche> descartados= new ArrayList<>();
        for (int i = 0; i < Utils.GESTORES_A_GENERAR; i++) {
            ArrayList<Coche> coches= new ArrayList<>();
            Deposito deposito= new Deposito(i,coches,barrier1,barrier2);
            depositos.add(deposito);
            ArrayList<Reserva> reservas= new ArrayList<>();
            for (int j = 0; j < Utils.RESERVAS_A_GENERAR; j++) {
                Reserva nueva= new Reserva(j,Utils.MAXIMO_COCHES_POR_RESERVA,Utils.TipoReserva.getTipoReserva(Utils.random.nextInt(Utils.VALOR_GENERACION)));
                reservas.add(nueva);
            }
            Gestor gestor= new Gestor(i,coches,reservas,descartados,barrier1,barrier2);
            gestores.add(gestor);
        }
        ArrayList<Thread> hilos= new ArrayList<>();
        for (int i = 0; i < Utils.GESTORES_A_GENERAR; i++) {
            Thread hilo= new Thread(depositos.get(i));
            hilos.add(hilo);
        }
        for (int i = 0; i < Utils.GESTORES_A_GENERAR; i++) {
            Thread hilo= new Thread(gestores.get(i));
            hilos.add(hilo);
        }
        System.out.println("MAIN: start hilos");
        for (int i = 0; i < hilos.size(); i++) {
            hilos.get(i).start();
        }
        System.out.println("MAIN: espera pricipal");
        try {
            TimeUnit.SECONDS.sleep(Utils.TIEMPO_ESPERA_HILO_PRINCIPAL);
        } catch (InterruptedException ex) {
            Logger.getLogger(Sesion3.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("MAIN: interrupcion");
        for (int i = 0; i < hilos.size(); i++) {
            hilos.get(i).interrupt();
        }
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException ex) {
            Logger.getLogger(Sesion3.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("MAIN: descartados");
        for (int i = 0; i < descartados.size(); i++) {
            System.out.println(descartados.get(i).toString());
        }
    }
    
}
