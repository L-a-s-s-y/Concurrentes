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
public class Plato {
    private final int iD;
    private final double precio;

    public Plato(int iD, double precio) {
        this.iD = iD;
        this.precio = precio;
    }

    public int getiD() {
        return iD;
    }

    public double getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Plato{iD=").append(iD);
        sb.append(", precio=").append(precio);
        sb.append('}');
        return sb.toString();
    }
    
    
}
