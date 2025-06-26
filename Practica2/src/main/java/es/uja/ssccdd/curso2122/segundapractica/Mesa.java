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
public class Mesa {
    private boolean libre;
    private long dni_cliente;
    private final int iD;

    public Mesa(int id, boolean libre) {
        this.dni_cliente= -1;
        this.libre = libre;
        this.iD= id;
    }

    public long getDni_cliente() {
        return dni_cliente;
    }

    public void setDni_cliente(long dni_cliente) {
        this.dni_cliente = dni_cliente;
    }

    public int getiD() {
        return iD;
    }

    public boolean isLibre() {
        return libre;
    }

    public void setLibre(boolean libre) {
        this.libre = libre;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Mesa{libre=").append(libre);
        sb.append(", dni_cliente=").append(dni_cliente);
        sb.append(", iD=").append(iD);
        sb.append('}');
        return sb.toString();
    }
    
    
}
