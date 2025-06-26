/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.uja.ssccdd.curso2122.problemassesion3.grupo5;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian Luque Luque (alluque)
 */
public class Deposito implements Runnable {
    
    private int id_deposito;
    private ArrayList<Coche> coches_deposito_gestor;
    private CyclicBarrier barrier1;
    private CyclicBarrier barrier2;

    public Deposito(int id_deposito, ArrayList<Coche> coches_deposito, CyclicBarrier barrier, CyclicBarrier barredor) {
        this.id_deposito = id_deposito;
        this.coches_deposito_gestor = coches_deposito;
        this.barrier1 = barrier;
        this.barrier2 = barredor;
    }

    @Override
    public void run() {
        boolean centinela= true;
        int contador= 0;
        int fase= 0;
        while(centinela){
            fase= 0;
            try {
                System.out.println("DEPOSITO "+this.id_deposito+": preparando...");
                TimeUnit.MILLISECONDS.sleep(Utils.TIEMPO_ESPERA_PREPARACION_MAX-Utils.random.nextInt(Utils.TIEMPO_ESPERA_PREPARACION_MIN+1));
            } catch (InterruptedException ex) {
                //Logger.getLogger(Deposito.class.getName()).log(Level.SEVERE, null, ex);
                centinela= false;
            }
            System.out.println("DEPOSITO "+this.id_deposito+": preparao");

            fase++;
            Coche nuevo= new Coche(contador,Utils.TipoReserva.getTipoReserva(Utils.random.nextInt(Utils.VALOR_GENERACION)));
            this.coches_deposito_gestor.add(nuevo);
            contador++;
            
            try {
                System.out.println("DEPOSITO "+this.id_deposito+": esperando");
                this.barrier1.await();
                fase++;
                System.out.println("DEPOSITO "+this.id_deposito+": esperando otra vez");
                this.barrier2.await();
            } catch (InterruptedException ex) {
                //Logger.getLogger(Deposito.class.getName()).log(Level.SEVERE, null, ex);
                centinela= false;
            } catch (BrokenBarrierException ex) {
                //Logger.getLogger(Deposito.class.getName()).log(Level.SEVERE, null, ex);
                centinela= false;
            }
            if(!centinela){
                System.out.println("DEPOSITO "+this.id_deposito+": interrumpido");

                if(fase==0){
                    System.out.println("DEPOSITO "+this.id_deposito+": Fase: preparación.");
                }
                if(fase==1){
                    System.out.println("DEPOSITO "+this.id_deposito+": Fase: espera depositos.");
                }
                if(fase==2){
                    System.out.println("DEPOSITO "+this.id_deposito+": Fase: espera gestores.");
                }
                System.out.println("DEPOSITO "+this.id_deposito+": Coches generados: "+contador);
            }
        }
    }

}
