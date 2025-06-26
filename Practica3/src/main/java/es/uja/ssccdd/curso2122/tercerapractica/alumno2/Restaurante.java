/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.tercerapractica.alumno2;

import es.uja.ssccdd.curso2122.tercerapractica.utils.Constantes;
import java.util.ArrayList;
import java.util.PriorityQueue;
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
public class Restaurante implements Runnable{
    
    private ActiveMQConnectionFactory conFactory;
    private Connection conection;
    private Session session;
    
    private Destination destinationPREMIUM;
    private Destination destinationESTANDAR;
    private Destination destinationConfirmacion;
    private Destination destinationPedidos;
    private Destination destinationComandas;
    private Destination destinationPlatos;
    private Destination destinationServir;
    private Destination destinationFin;

    private MessageConsumer consumerPREMIUM;
    private MessageConsumer consumerESTANDAR;
    private MessageConsumer consumerPedidos;
    private MessageConsumer consumerPlatos;
    private MessageConsumer consumerFin;
    
    private MessageProducer producerLlegada;
    private MessageProducer producerComandas;
    private MessageProducer producerServir;
    
    private final ArrayList<TextMessage> colaPremium;
    private final ArrayList<TextMessage> colaEstandar;
    private final ArrayList<TextMessage> colaPedidos;
    private final ArrayList<TextMessage> colaPlatos;
    private final ArrayList<TextMessage> colaFin;
    private final PriorityQueue<Comanda> comandas;
    
    private final ArrayList<Double> carta;
    private int comensales;
    private double recaudacion;
    

    public Restaurante() {
        //this.respuesta= null;
        this.carta= new ArrayList<>();
        this.colaPremium= new ArrayList<>();
        this.colaEstandar= new ArrayList<>();
        this.colaPedidos= new ArrayList<>();
        this.colaPlatos= new ArrayList<>();
        this.colaFin= new ArrayList<>();
        this.comandas= new PriorityQueue<>();
        this.comensales= 0;
        this.recaudacion= 0.0;
        
        for (int i = 0; i < Constantes.CARTA_TAM; i++) {
            double precio= (double)(Constantes.aleatorio.nextInt(Constantes.PRECIO_MAX-Constantes.PRECIO_MIN)+Constantes.PRECIO_MIN);
            this.carta.add(precio);
        }
    }
    

    @Override
    public void run() {
        
        try {
            this.before();
            this.task();
        } catch (JMSException ex) {
            System.out.println("RESTAURANTE: Interrumpido. JMS");
        } catch (InterruptedException ex) {
            System.out.println("RESTAURANTE: Interrumpido. INTERRUPTED");
        }
        finally{
            this.after();
            System.out.println("RESTAURANTE: Recaudación: "+this.recaudacion);
        }
        
        System.out.println("RESTAURANTE: Chapando!");
    }
    
    public void task() throws JMSException, InterruptedException{
        
        System.out.println("RESTAURANTE: Abrimos!!");
        
        int turnos= 0;
        
        while(!Thread.currentThread().isInterrupted()){
            
            if(turnos>Constantes.LIMITE_TURNOS){
                this.colaPremium.add(0, this.colaEstandar.get(0));
                turnos= 0;
            }
            
            while(!this.colaFin.isEmpty()){
                this.colaFin.remove(0);
                this.comensales--;
            }
            
            if(this.comensales<=Constantes.AFORO){
                TextMessage respuesta= null;
                if(!this.colaPremium.isEmpty()){
                    respuesta= this.colaPremium.remove(0);
                    turnos++;
                }
                else{
                    if(!this.colaEstandar.isEmpty()){
                        respuesta= this.colaEstandar.remove(0);
                        turnos= 0;
                    }
                }
                if(respuesta != null){
                    System.out.println("RESTAURANTE: Petición de entrada recibida. ****"+respuesta.getText()+"****");
                    this.destinationConfirmacion= this.session.createQueue(respuesta.getText());
                    this.producerLlegada= this.session.createProducer(this.destinationConfirmacion);
                    GsonUtil<ArrayList> gsonCarta= new GsonUtil<ArrayList>();
                    String cuerpo= gsonCarta.encode(this.carta, ArrayList.class);
                    TextMessage message= this.session.createTextMessage(cuerpo);
                    this.producerLlegada.send(message);
                    this.producerLlegada.close();
                    this.comensales++;
                }
            }

            if(!this.colaPedidos.isEmpty()){
                TextMessage respuesta= this.colaPedidos.remove(0);
                String cuerpo= respuesta.getText();
                GsonUtil<ArrayList> gsonPedido= new GsonUtil<ArrayList>();
                ArrayList<Double> pedido= (gsonPedido.decode(cuerpo, ArrayList.class));

                int dniCliente= pedido.remove(pedido.size()-1).intValue();

                while(!pedido.isEmpty()){
                    int platoID= pedido.remove(0).intValue();
                    Comanda nueva= new Comanda(dniCliente, platoID, this.carta.get(platoID));
                    this.comandas.add(nueva);
                } 
            }
            
            while(!this.comandas.isEmpty()){
                Comanda nueva= this.comandas.poll();
                GsonUtil<Comanda> gsonComanda= new GsonUtil<>();
                String cuerpo= gsonComanda.encode(nueva, Comanda.class);
                TextMessage message= this.session.createTextMessage(cuerpo);
                this.producerComandas= this.session.createProducer(this.destinationComandas);
                this.producerComandas.send(message);
                this.producerComandas.close();
            }
            
            while(!this.colaPlatos.isEmpty()){
                TextMessage respuesta= this.colaPlatos.remove(0);
                String cuerpo= respuesta.getText();
                GsonUtil<Comanda> gsonComanda= new GsonUtil<>();
                Comanda nueva= gsonComanda.decode(cuerpo, Comanda.class);
                this.recaudacion+=nueva.getPrecio();
                
                this.destinationServir= this.session.createQueue(Constantes.DESTINO+Constantes.SERVIR+nueva.getID_cliente());
                
                this.producerServir= this.session.createProducer(this.destinationServir);
                String plato= ""+nueva.getPlatoID();
                TextMessage servir= this.session.createTextMessage(plato);
                this.producerServir.send(servir);
                this.producerServir.close();
            }
            
        }
        
        
    }
    
    public void before() throws JMSException{
        this.conFactory= new ActiveMQConnectionFactory(Constantes.CONNECTION);
        this.conection= this.conFactory.createConnection();
        this.conection.start();
        this.session= this.conection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        this.destinationPREMIUM= this.session.createQueue(Constantes.DESTINO+Constantes.PREMIUM);
        this.destinationESTANDAR= this.session.createQueue(Constantes.DESTINO+Constantes.ESTANDAR);
        this.destinationPedidos= this.session.createQueue(Constantes.DESTINO+Constantes.PEDIDO);
        this.destinationComandas= this.session.createQueue(Constantes.DESTINO+Constantes.COCINERO);
        this.destinationPlatos= this.session.createQueue(Constantes.DESTINO+Constantes.COCINADO);
        this.destinationFin= this.session.createQueue(Constantes.DESTINO+Constantes.FIN);
        
        this.consumerPREMIUM= this.session.createConsumer(destinationPREMIUM);
        this.consumerPREMIUM.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message msg) {
                if(msg instanceof TextMessage){
                    TextMessage respuesta= (TextMessage) msg;
                    colaPremium.add(respuesta);
                }
            }
        });
        
        this.consumerESTANDAR= this.session.createConsumer(destinationESTANDAR);
        this.consumerESTANDAR.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message msg) {
                if(msg instanceof TextMessage){
                    TextMessage respuesta= (TextMessage) msg;
                    colaEstandar.add(respuesta);
                }
            }
        });
        
        this.consumerPedidos= this.session.createConsumer(this.destinationPedidos);
        this.consumerPedidos.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message msg) {
                if(msg instanceof TextMessage){
                    TextMessage respuesta= (TextMessage) msg;
                    colaPedidos.add(respuesta);
                }
            }
        });
        
        this.consumerPlatos= this.session.createConsumer(this.destinationPlatos);
        this.consumerPlatos.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message msg) {
                if(msg instanceof TextMessage){
                    TextMessage respuesta= (TextMessage) msg;
                    colaPlatos.add(respuesta);
                }
            }
        });
        
        this.consumerFin= this.session.createConsumer(this.destinationFin);
        this.consumerFin.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message msg) {
                if(msg instanceof TextMessage){
                    TextMessage respuesta= (TextMessage) msg;
                    colaFin.add(respuesta);
                }
            }
        });
    }
    
    public void after(){
        try{
            if(this.conection!=null){
                this.conection.close();
            }
        }
        catch (JMSException ex) {
            //Logger.getLogger(Restaurante.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            if(this.consumerPREMIUM!=null){
                this.consumerPREMIUM.close();
            }
        } catch (JMSException ex) {
            //Logger.getLogger(Restaurante.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            if(this.consumerESTANDAR!=null){
                this.consumerESTANDAR.close();
            }
        } catch (JMSException ex) {
            //Logger.getLogger(Restaurante.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            if(this.consumerPedidos!=null){
                this.consumerPedidos.close();
            }
        } catch (JMSException ex) {
            //Logger.getLogger(Restaurante.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            if(this.consumerPlatos!=null){
                this.consumerPlatos.close();
            }
        } catch (JMSException ex) {
            //Logger.getLogger(Restaurante.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            if(this.consumerFin!=null){
                this.consumerFin.close();
            }
        } catch (JMSException ex) {
            //Logger.getLogger(Restaurante.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            if(this.producerLlegada!=null){
                this.producerLlegada.close();
            }
        } catch (JMSException ex) {
            //Logger.getLogger(Restaurante.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            if(this.producerComandas!=null){
                this.producerComandas.close();
            }
        } catch (JMSException ex) {
            //Logger.getLogger(Restaurante.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            if(this.producerServir!=null){
                this.producerServir.close();
            }
        } catch (JMSException ex) {
            //Logger.getLogger(Restaurante.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
