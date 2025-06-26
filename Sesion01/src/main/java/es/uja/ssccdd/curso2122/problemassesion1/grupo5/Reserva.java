/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.problemassesion1.grupo5;

import es.uja.ssccdd.curso2122.problemassesion1.grupo5.Utils.TipoReserva;
import java.util.ArrayList;

/**
 *
 * @author El Boss
 */
public class Reserva {
    
    private final int id_reserva;
    private final TipoReserva gama_reserva;
    private final int coches_necesarios;
    private ArrayList<Coche> coches_asignados;

    public Reserva(int id_reserva, TipoReserva gama_reserva, int coches_necesarios) {
        this.id_reserva = id_reserva;
        this.gama_reserva = gama_reserva;
        this.coches_necesarios= coches_necesarios;
        this.coches_asignados= new ArrayList<>();
    }

    public int getId_reserva() {
        return id_reserva;
    }

    public TipoReserva getGama_reserva() {
        return gama_reserva;
    }

    public ArrayList<Coche> getCoches_asignados() {
        return coches_asignados;
    }

    @Override
    public String toString() {
        return "Reserva{" + "id_reserva=" + id_reserva + ", gama_reserva=" + gama_reserva + ", coches_necesarios=" + coches_necesarios + ", coches_asignados=" + coches_asignados + '}';
    }
    
    public boolean insertar_coche(Coche nuevo){
       // boolean exito= false;
        if(this.gama_reserva.equals(nuevo.getGama_coche())){
            if(this.coches_asignados.size()<this.coches_necesarios){
                this.coches_asignados.add(nuevo);
                return true;
            }
        }
        return false;
    }
    
    
}
