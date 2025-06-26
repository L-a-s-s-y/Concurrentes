/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.problemassesion11.grupo5;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author Adrian Luque Luque (alluque)
 */
public class ListenerDeposito implements MessageListener {

    private final int iD;
    //private final ArrayList<Integer> confirmados;
    private int contador;
    //private final ReentrantLock excMutua;

    public ListenerDeposito (int iD) {
        this.iD = iD;
        //this.confirmados = confirmados;
        this.contador= 0;
        //this.excMutua = excMutua;
        System.out.println("Listener del deposito " + iD + " iniciado.");
    }

    public int getContador() {
        return contador;
    }

    @Override
    public void onMessage(Message msg) {

        try {

            int id = Integer.parseInt(((TextMessage) msg).getText());
            
            this.contador++;

//            excMutua.lock();
//            confirmados.add(id);
//            excMutua.unlock();

            System.out.println("El listener del deposito " + iD + " ha recibido la confirmación del modelo: " + id);

        } catch (JMSException ex) {
            Logger.getLogger(ListenerDeposito.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
