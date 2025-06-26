/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.segundapractica;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author El Boss
 */
public class Cliente implements Runnable{
    private final long dni;
    private int turnos;
    private boolean dentro;
    private final Utiles.TipoCliente estatus;
    private final ArrayList<Integer> platos_pedidos;
    private final Monitor monitor;
    private final Semaphore espera;

    public Cliente(long dni, Monitor monitor, Utiles.TipoCliente estatus) {
        this.dni = dni;
        this.dentro= false;
        this.turnos= 0;
        this.platos_pedidos = new ArrayList<>();
        this.monitor = monitor;
        this.espera= new Semaphore(0);
        this.estatus= estatus;
    }

    @Override
    public void run() {
        
        try {
            //Llegada
            //int llegada= Utiles.aleatorio.nextInt(Utiles.TIEMPO_LLEGADA_MAX-Utiles.TIEMPO_LLEGADA_MIN)+Utiles.TIEMPO_LLEGADA_MIN;
            //TimeUnit.SECONDS.sleep(llegada);
            //Se añade a la cola
            this.monitor.insertarCola(this);
            //Mientras no haya entrado debe esperar.
            this.espera.acquire();
            this.monitor.elegirPlatos(this);
            //Come los platos.
            for (int i = 0; i < this.platos_pedidos.size(); i++) {
                this.espera.acquire();
                TimeUnit.SECONDS.sleep(Utiles.aleatorio.nextInt(Utiles.TIEMPO_MAX_COMICION-Utiles.TIEMPO_MIN_COMICION)+Utiles.TIEMPO_MIN_COMICION);
            }
            System.out.println("CLIENTE "+this.dni+": Finaliza la comicion");
            System.out.println("CLIENTE "+this.dni+": Pagando.");
            this.monitor.pagar(this.platos_pedidos);
            System.out.println("CLIENTE "+this.dni+": Dejando mesa.");
            this.monitor.dejarMesa(this.dni);
            
        } catch (InterruptedException ex) {
            System.out.println("CLIENTE "+this.dni+": Interrumpido.");
        }
        System.out.println("CLIENTE "+this.dni+": Adiós.");
    }

    public Utiles.TipoCliente getEstatus() {
        return estatus;
    }

    public long getDni() {
        return dni;
    }

    public boolean isDentro() {
        return dentro;
    }

    public void setDentro(boolean dentro) {
        this.dentro = dentro;
    }

    public ArrayList<Integer> getPlatos_pedidos() {
        return platos_pedidos;
    }
    
    public void despertar(){
        this.espera.release();
    }
    
    public void dormir() throws InterruptedException{
        this.espera.acquire();
    }
    
    public void incrementarTurno(){
        this.turnos++;
    }

    public int getTurnos() {
        return turnos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cliente{dni=").append(dni);
        sb.append(", dentro=").append(dentro);
        sb.append(", estatus=").append(estatus);
        sb.append('}');
        return sb.toString();
    }
    
    
}
