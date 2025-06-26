/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.segundapractica;

/**
 *
 * @author El Boss
 */
public class Comanda implements Comparable<Comanda>{

    private final long ID_cliente;
    private final Plato plato;

    public Comanda(long ID_cliente, Plato plato) {
        this.ID_cliente = ID_cliente;
        this.plato = plato;
    }

    public long getID_cliente() {
        return ID_cliente;
    }

    public Plato getPlato() {
        return plato;
    }
    
    @Override
    public int compareTo(Comanda o) {
        if(this.plato.getPrecio()>o.getPlato().getPrecio()){
            return -1;
        }
        else{
            if(this.plato.getPrecio()<o.getPlato().getPrecio()){
                return 1;
            }
            return 0;
        }
    }
    
}
