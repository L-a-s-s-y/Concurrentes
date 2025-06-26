/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.segundapractica;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author El Boss
 */
public class Restaurante implements Runnable{
    private final Monitor monitor;

    public Restaurante(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        System.out.println("RESTAURANTE: Abrimos.");
        try{
            boolean centinela= true;
            while(centinela){
                while(this.monitor.genteEnCola() && this.monitor.comprobarAforo()){
                    if(this.monitor.genteEnColaPremium()){
                        Cliente cliente= this.monitor.siguienteColaPremium();
                        if(cliente!=null){
                            this.monitor.entrarEnRestaurante(cliente);
                        }
                    }
                    else{
                        if(this.monitor.genteEnColaNoPremium()){
                            Cliente cliente= this.monitor.siguienteColaNoPremium();
                            if(cliente!=null){
                                this.monitor.entrarEnRestaurante(cliente);
                            }
                        }
                    }
                }
                while(this.monitor.hayComandas()){
                    this.monitor.cocinarEtServir();
                }
                if(Thread.currentThread().isInterrupted()){
                    centinela= false;
                    System.out.println("RESTAURANTE: Recaudación: "+this.monitor.getRecaudacion());
                }
            }
        } catch (InterruptedException ex) {
            System.out.println("RESTAURANTE: Interrumpido.");
            System.out.println("RESTAURANTE: Recaudación: "+this.monitor.getRecaudacion());
        }
        System.out.println("RESTAURANTE: Chapando.");
    }
}
