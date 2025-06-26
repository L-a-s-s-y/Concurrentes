/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.tercerapractica.alumno2;

/**
 *
 * @author El Boss
 */
public class Comanda implements Comparable<Comanda>{

    private final int ID_cliente;
    private final int platoID;
    private final double precio;

    public Comanda(int ID_cliente, int plato, double precio) {
        this.ID_cliente = ID_cliente;
        this.platoID = plato;
        this.precio= precio;
    }

    public double getPrecio() {
        return precio;
    }

    public int getID_cliente() {
        return ID_cliente;
    }

    public int getPlatoID() {
        return this.platoID;
    }
    
    @Override
    public int compareTo(Comanda o) {
        if(this.precio>o.getPrecio()){
            return -1;
        }
        else{
            if(this.precio<o.getPrecio()){
                return 1;
            }
            return 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Comanda{ID_cliente=").append(ID_cliente);
        sb.append(", platoID=").append(platoID);
        sb.append(", precio=").append(precio);
        sb.append('}');
        return sb.toString();
    }
    
}
