/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author UJA
 */
public class Gestor implements Runnable {
    
    private int id_gestor;
    private ArrayList<Coche> coches_deposito_gestor;
    private ArrayList<Reserva> reservas_gestor;
    private ArrayList<Coche> descartados;
    private CyclicBarrier barrier1;
    private CyclicBarrier barrier2;

    public Gestor(int id_gestor, ArrayList<Coche> coches_deposito_gestor, ArrayList<Reserva> reservas_gestor, ArrayList<Coche> descartados, CyclicBarrier barrier1, CyclicBarrier barrier2) {
        this.id_gestor = id_gestor;
        this.coches_deposito_gestor = coches_deposito_gestor;
        this.reservas_gestor = reservas_gestor;
        this.barrier1 = barrier1;
        this.barrier2 = barrier2;
        this.descartados= descartados;
    }

    @Override
    public void run() {
        boolean centinela= true;
        int fase= 0;
        int contador= 0;
        try {
            //fase++;
            System.out.println("GESTOR "+this.id_gestor+": esperando.");

            this.barrier1.await();
        } catch (InterruptedException ex) {
            //Logger.getLogger(Gestor.class.getName()).log(Level.SEVERE, null, ex);
            centinela= false;
        } catch (BrokenBarrierException ex) {
            //Logger.getLogger(Gestor.class.getName()).log(Level.SEVERE, null, ex);
            centinela= false;
        }
        while(centinela){
            fase= 0;
            Coche ultimo= this.coches_deposito_gestor.get(this.coches_deposito_gestor.size()-1);
            boolean insertado= false;
            for (int i = 0; i < this.reservas_gestor.size()&&!insertado; i++) {
                insertado=this.reservas_gestor.get(i).addCoche(ultimo);
                contador++;
            }
            if(!insertado){
                this.descartados.add(ultimo);
            }
            try {
                fase++;
                System.out.println("GESTOR "+this.id_gestor+": asignando.");

                TimeUnit.MILLISECONDS.sleep(Utils.TIEMPO_ESPERA_ASIGNACION_MAX-Utils.random.nextInt(Utils.TIEMPO_ESPERA_ASIGNACION_MIN+1));
            } catch (InterruptedException ex) {
                //Logger.getLogger(Gestor.class.getName()).log(Level.SEVERE, null, ex);
                centinela= false;
            }
            try {
                fase++;
                System.out.println("GESTOR "+this.id_gestor+": esperando otra vez.");

                this.barrier2.await();
            } catch (InterruptedException ex) {
                //Logger.getLogger(Gestor.class.getName()).log(Level.SEVERE, null, ex);
                centinela= false;
            } catch (BrokenBarrierException ex) {
                //Logger.getLogger(Gestor.class.getName()).log(Level.SEVERE, null, ex);
                centinela= false;
            }
            if(!centinela){
                if(fase==0){
                    System.out.println("GESTOR "+this.id_gestor+": Fase: preparación.");
                }
                if(fase==1){
                    System.out.println("GESTOR "+this.id_gestor+": Fase: espera depositos.");
                }
                if(fase==2){
                    System.out.println("GESTOR "+this.id_gestor+": Fase: espera gestores.");
                }
                System.out.println("GESTOR "+this.id_gestor+": Coches preparados: "+contador);
            }
        }
    }

}
