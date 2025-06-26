/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.problemassesion2.grupo5;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author UJA
 */
public class Gestor implements Runnable {
    
    private final int id_gestor;
    private ArrayList<Reserva> reservas_gestor;
    private ArrayList<Coche> coches_gestor;
    private ArrayList<Coche> coches_descartados;
    private int siguiente_reserva;
    private int num_coches_procesados;
    private boolean interrumpido;

    public Gestor(int id_gestor, ArrayList<Coche> coches_descartados) {
        this.id_gestor = id_gestor;
        this.coches_descartados = coches_descartados;
        this.coches_gestor= new ArrayList<>();
        this.reservas_gestor= new ArrayList<>();
        this.siguiente_reserva= 0;
        this.num_coches_procesados= 0;
        this.interrumpido= false;
    }

    public int getId_gestor() {
        return id_gestor;
    }

    public ArrayList<Reserva> getReservas_gestor() {
        return reservas_gestor;
    }

    public ArrayList<Coche> getCoches_gestor() {
        return coches_gestor;
    }

    public ArrayList<Coche> getCoches_descartados() {
        return coches_descartados;
    }
    
    public void asignarCoche(Coche nuevo){
        this.coches_gestor.add(nuevo);
    }
    
    public void asignarReserva(Reserva nueva){
        this.reservas_gestor.add(nueva);
    }

    public int getSiguiente_reserva() {
        return siguiente_reserva;
    }

    public int getNum_coches_procesados() {
        return num_coches_procesados;
    }
    
    private boolean reservar(Coche coche){
        int reservasComprobadas = 0; //Para evitar bucles infinitos
        boolean encolado = false;
        while (!encolado && reservasComprobadas < this.reservas_gestor.size()) {// Si no se ha generado una impresora de un tipo, esto evita el bucle infinito

            if (this.reservas_gestor.get(this.siguiente_reserva).addCoche(coche)) {
                encolado = true;
            }

            reservasComprobadas++;
            this.siguiente_reserva = (this.siguiente_reserva + 1) % this.reservas_gestor.size();

        }
        if(!encolado){
            this.coches_descartados.add(coche);
        }
        return encolado;
    }
    
    private float porcentaje(){
        return 100f*this.num_coches_procesados/this.coches_gestor.size();
    }

    @Override
    public void run() {
        this.interrumpido= false;
        System.out.println("GESTOR "+this.id_gestor+": Comienza a gestionar coches!");
        for(Coche coche: this.coches_gestor){
            if(!interrumpido || (interrumpido && coche.getCalidadCoche()==Utils.TipoReserva.PREMIUM)){
                this.reservar(coche);
                this.num_coches_procesados++;
                try {
                    TimeUnit.SECONDS.sleep(Utils.TIEMPO_ESPERA_MAX-Utils.random.nextInt(Utils.TIEMPO_ESPERA_MIN+2));
                } catch (InterruptedException ex) {
                    //Logger.getLogger(Ingeniero.class.getName()).log(Level.SEVERE, null, ex);
                    interrumpido= true;
                }
            }
        }
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        StringBuilder mensaje=new StringBuilder();
        
        mensaje.append("\n\nGestor ").append(this.id_gestor).append("  ");
        
        if(interrumpido){
            mensaje.append("\"INTERRUMPIDO\"");
        }
        mensaje.append(" ").append(this.coches_gestor.size()).append(" coches disponibles, de los cuales se han procesado el ").append(porcentaje()).append("%.");
        
        for( Reserva r : this.reservas_gestor){
            mensaje.append("\n\t").append(r.toString());
        }
        
        if( this.num_coches_procesados < this.coches_gestor.size()){
            mensaje.append("\n\tSe han quedado fuera ").append(this.coches_gestor.size()-this.num_coches_procesados).append(" coches por falta de tiempo.");
        }
        return mensaje.toString();
    }

   
    

}
