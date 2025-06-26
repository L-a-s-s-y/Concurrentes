package es.uja.ssccdd.curso2122.problemassesion8.grupo5;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Luis López Ruiz (llopez)
 */
public class Gestor implements Runnable {
    
    private final int id_gestor;
    private static int sig_id= 0;
    private PriorityBlockingQueue<Coche> lista_coches;
    private LinkedBlockingDeque<Reserva> lista_reservas;
    private ArrayList<Reserva> reservas_locales;

    public Gestor(PriorityBlockingQueue<Coche> lista_coches, LinkedBlockingDeque<Reserva> lista_reservas) {
        Utils.lock_ids.lock();
        sig_id++;
        this.id_gestor= sig_id;
        Utils.lock_ids.unlock();
        this.lista_coches = lista_coches;
        this.lista_reservas= lista_reservas;
        this.reservas_locales= new ArrayList<>();
    }

    public int getId_gestor() {
        return id_gestor;
    }

    public ArrayList<Reserva> getReservas_locales() {
        return reservas_locales;
    }

    @Override
    public void run() {
        System.out.println("GESTOR "+this.id_gestor+": Comienza a organizar reservas.");
        boolean centinela= true;
        Coche nuevo= null;
        Reserva candidata= null;
        while(centinela){
            try {
                nuevo= this.lista_coches.take();
                boolean exito= this.insertarLocal(nuevo);
                if(!this.lista_reservas.isEmpty()){
                    while(!exito){
                        if(!this.lista_reservas.isEmpty()){
                            candidata= this.lista_reservas.take();
                            exito= candidata.addCoche(nuevo);
                            this.reservas_locales.add(candidata);
                        }
                    }
                }
                TimeUnit.MILLISECONDS.sleep(Utils.random.nextInt(Utils.TIEMPO_ESPERA_ASIGNACION_MAX - Utils.TIEMPO_ESPERA_ASIGNACION_MIN) + Utils.TIEMPO_ESPERA_ASIGNACION_MIN);
            } catch (InterruptedException ex) {
                centinela= false;
                if(nuevo!=null)
                    this.lista_coches.add(nuevo);
                if(candidata!=null)
                    this.lista_reservas.add(candidata);
                Thread.currentThread().interrupt();
            }
        }  
    }
    
    private boolean insertarLocal(Coche nuevo){
        if(!this.reservas_locales.isEmpty()){
            for (int i = 0; i < this.reservas_locales.size(); i++) {
                if(this.reservas_locales.get(i).addCoche(nuevo)){
                    return true;
                }
            }
        }
        return false;
    }
    
}
