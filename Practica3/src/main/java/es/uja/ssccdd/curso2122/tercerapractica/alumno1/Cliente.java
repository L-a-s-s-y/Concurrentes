/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.tercerapractica.alumno1;
import es.uja.ssccdd.curso2122.tercerapractica.utils.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author El Boss
 */
public class Cliente implements Runnable{
    
    private final int dni;
    private final Constantes.TipoCliente estatus;
    //private final ArrayList<Integer> platosPedidos;
    
    private ActiveMQConnectionFactory conFactory;
    private Connection conection;
    private Session session;
    
    private Destination destinationFin;
    private Destination destinationEnvio;
    private Destination destinationRespuesta;
    private Destination destinationCarta;
    private Destination destinationServir;
    
    private MessageConsumer consumerPlatos;
    private MessageConsumer consumerEntrada;
    
    private MessageProducer producerLlegada;
    private MessageProducer producerCarta;
    private MessageProducer producerFin;

    public Cliente(int dni, Constantes.TipoCliente estatus) {
        this.dni = dni;
        this.estatus = estatus;
        //this.platosPedidos = new ArrayList<>();
    }

    @Override
    public void run() {
        
        try {
            this.before();
            this.task();
        } catch (JMSException ex) {
            System.out.println("CLIENTE "+this.dni+": Cagonsos, ocurrió una excepción. JMS");
        } catch (InterruptedException ex) {
            System.out.println("CLIENTE "+this.dni+": Cagonsos, ocurrió una excepción. INTERRUPTED");
        }
        finally{
            this.after();
        }
        
        System.out.println("CLIENTE "+this.dni+": Adiós!");

    }
    
    public void task() throws JMSException, InterruptedException{
        
        System.out.println("CLIENTE "+this.dni+": Está vivo!!");
        this.producerLlegada= this.session.createProducer(this.destinationEnvio);
        TextMessage message= this.session.createTextMessage(Constantes.DESTINO+"."+this.dni);
        this.producerLlegada.send(message);
        System.out.println("CLIENTE "+this.dni+": Llega a la cola.");
        
        this.consumerEntrada= this.session.createConsumer(destinationRespuesta);
        TextMessage confirmacion= (TextMessage) this.consumerEntrada.receive();
        System.out.println("CLIENTE "+this.dni+": Entra en el restaurante.");
        GsonUtil<ArrayList> gsonCarta= new GsonUtil<ArrayList>();
        ArrayList<Double> carta= gsonCarta.decode(confirmacion.getText(), ArrayList.class);

        int menu= Constantes.aleatorio.nextInt(Constantes.MAX_PLATOS-Constantes.MIN_PLATOS)+Constantes.MIN_PLATOS;
        ArrayList<Integer> platosPedidos= new ArrayList<>();
        while(platosPedidos.size()<menu){
            int seleccion= Constantes.aleatorio.nextInt(Constantes.CARTA_TAM);
            if(this.estatus==Constantes.TipoCliente.PREMIUM){
                if(carta.get(seleccion)>=Constantes.PRECIO_MIN_PREMIUM){
                    platosPedidos.add(seleccion);
                }
            }
            else{
                if(carta.get(seleccion)>=Constantes.PRECIO_MAX_ESTANDAR){
                    platosPedidos.add(seleccion);
                }
            }
        }
        platosPedidos.add(this.dni);

        String cuerpo= gsonCarta.encode(platosPedidos, ArrayList.class);
        message= this.session.createTextMessage(cuerpo);

        this.producerCarta= this.session.createProducer(this.destinationCarta);

        this.producerCarta.send(message);
        System.out.println("CLIENTE "+this.dni+": Platos pedidos; esperando a comer.");
        platosPedidos.remove(platosPedidos.size()-1);
        this.consumerPlatos= this.session.createConsumer(this.destinationServir);
        
        while(!platosPedidos.isEmpty()){
            TextMessage messagePlato= (TextMessage)this.consumerPlatos.receive();
            String cuerpoPlato= messagePlato.getText();
            int plato= Integer.parseInt(cuerpoPlato);

            int consumir= 0;
            for (int i = 0; i < platosPedidos.size(); i++) {
                if(platosPedidos.get(i)==plato){
                    consumir= i;
                    i+= platosPedidos.size();
                }
            }
            platosPedidos.remove(consumir);

            TimeUnit.SECONDS.sleep(Constantes.aleatorio.nextInt(Constantes.TIEMPO_MAX_COMICION-Constantes.TIEMPO_MIN_COMICION)+Constantes.TIEMPO_MIN_COMICION);
        }
        
        System.out.println("CLIENTE "+this.dni+": Finaliza la comición.");
        this.producerFin= this.session.createProducer(this.destinationFin);
        TextMessage messageFin= this.session.createTextMessage("Sayonara bb");
        this.producerFin.send(messageFin);
        
    }
    
    public void before() throws JMSException{
        this.conFactory= new ActiveMQConnectionFactory(Constantes.CONNECTION);
        this.conection= this.conFactory.createConnection();
        this.conection.start();
        this.session= this.conection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        this.destinationCarta= this.session.createQueue(Constantes.DESTINO+Constantes.PEDIDO);
        this.destinationRespuesta= this.session.createQueue(Constantes.DESTINO+"."+this.dni);
        this.destinationServir= this.session.createQueue(Constantes.DESTINO+Constantes.SERVIR+this.dni);
        this.destinationFin= this.session.createQueue(Constantes.DESTINO+Constantes.FIN);
        if(this.estatus==Constantes.TipoCliente.PREMIUM){
            this.destinationEnvio= this.session.createQueue(Constantes.DESTINO+Constantes.PREMIUM);
        }
        else{
            this.destinationEnvio= this.session.createQueue(Constantes.DESTINO+Constantes.ESTANDAR);
        }

    }
    
    public void after(){
        if(this.conection!=null){
            try {
                this.conection.close();
            } catch (JMSException ex) {
                //Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(this.consumerPlatos!=null){
            try {
                this.consumerPlatos.close();
            } catch (JMSException ex) {
                //Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(this.consumerEntrada!=null){
            try {
                this.consumerEntrada.close();
            } catch (JMSException ex) {
                //Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(this.producerCarta!=null){
            try {
                this.producerCarta.close();
            } catch (JMSException ex) {
                //Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(this.producerLlegada!=null){
            try {
                this.producerLlegada.close();
            } catch (JMSException ex) {
                //Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(this.producerFin!=null){
            try {
                this.producerFin.close();
            } catch (JMSException ex) {
                //Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
