/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.tercerapractica.alumno2;

import es.uja.ssccdd.curso2122.tercerapractica.utils.Constantes;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author El Boss
 */
public class Cocinero implements Runnable{
    
    private ActiveMQConnectionFactory conFactory;
    private Connection conection;
    private Session session;
    private Destination destinationEnvio;
    private Destination destinationRespuesta;
    private MessageConsumer consumerComandas;
    private MessageProducer producer;
    
    private final ArrayList<TextMessage> colaComandas;
    private final ArrayList<Comanda> historialComandas;

    public Cocinero() {
        this.colaComandas = new ArrayList<>();
        this.historialComandas= new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            this.before();
            this.task();
        } catch (JMSException ex) {
            //Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("COCINERO: Cagonsos, ocurrió una excepción. JMS");
        } catch (InterruptedException ex) {
            //Logger.getLogger(Cocinero.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("COCINERO: Cagonsos, ocurrió una excepcion. INTERRUPTED");
        }
        finally{
            this.after();
        }
        
        System.out.println("COCINERO: Adiós!");
    }
    
    public void task() throws JMSException, InterruptedException{
        System.out.println("COCINERO: A guisar!!");
        GsonUtil<Comanda> gsonComanda= new GsonUtil<>();
        while(!Thread.currentThread().isInterrupted()){
            if(!this.colaComandas.isEmpty()){
                TextMessage comanda= this.colaComandas.remove(0);
                String cuerpo= comanda.getText();
                Comanda nuevo= gsonComanda.decode(cuerpo, Comanda.class);
                System.out.println("COCIENRO: Nueva comanda: "+nuevo);
                this.historialComandas.add(nuevo);
                //System.out.println(nuevo);
                TimeUnit.SECONDS.sleep(Constantes.TIEMPO_COCINACION);
                this.producer.send(comanda);
            }
        }
        
    }
    
    public void before() throws JMSException{
        this.conFactory= new ActiveMQConnectionFactory(Constantes.CONNECTION);
        this.conection= this.conFactory.createConnection();
        this.conection.start();
        this.session= this.conection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.destinationEnvio= this.session.createQueue(Constantes.DESTINO+Constantes.COCINERO);
        this.destinationRespuesta= this.session.createQueue(Constantes.DESTINO+Constantes.COCINADO);
        
        this.producer= this.session.createProducer(this.destinationRespuesta);
        
        this.consumerComandas= this.session.createConsumer(destinationEnvio);
        
        this.consumerComandas.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message msg) {
                if(msg instanceof TextMessage){
                    TextMessage respuesta= (TextMessage) msg;
                    colaComandas.add(respuesta);
                }
            }
        });
    }
    
    public void after(){
        if(this.conection!=null){
            try {
                this.conection.close();
            } catch (JMSException ex) {
                //Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(this.consumerComandas!=null){
            try {
                this.consumerComandas.close();
            } catch (JMSException ex) {
                //Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(this.producer!=null){
            try {
                this.producer.close();
            } catch (JMSException ex) {
                //Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public ArrayList<Comanda> getHistorialComandas() {
        return historialComandas;
    }
    
}
