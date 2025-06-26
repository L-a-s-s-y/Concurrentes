package es.uja.ssccdd.curso2122.problemassesion12.grupo5;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
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
 * @author José Luis López Ruiz (llopez)
 */
public class Deposito implements Runnable{
    
    private final int ID;
    private static AtomicInteger idGen= new AtomicInteger();
    private static AtomicInteger idCoche= new AtomicInteger();
    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private GsonUtil<Coche> gson;
    private MessageConsumer asyncConsumer;
    private int contador;

    public static AtomicInteger getIdCoche() {
        return idCoche;
    }
    
    public Deposito() {
        this.ID= idGen.getAndIncrement();
        this.gson= new GsonUtil<Coche>();
        this.contador= 0;
    }

    @Override
    public void run() {

        try{
            this.before();
            this.execution();
        } catch (JMSException ex) {
            //Logger.getLogger(Deposito.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            this.after();
        }
        
        
        System.out.println("DEPOSITO "+this.ID+": Confirmados: "+this.contador);
        System.out.println("DEPOSITO "+this.ID+": FIN");
    }
    
    private void before() throws JMSException{
        this.connectionFactory= new ActiveMQConnectionFactory(Utils.CONNECTION);
        this.connection= this.connectionFactory.createConnection();
        this.connection.start();
        this.session= this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.destination= this.session.createQueue(Utils.QUEUE);
        
        Destination asyncDestino= this.session.createQueue(Utils.QUEUE_DEPOSITO+this.ID);
        this.asyncConsumer= session.createConsumer(asyncDestino);
        this.asyncConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message msg) {
                contador++;
            }
        });
    }
    
    private void after(){
        if(this.connection!=null){
            try {
                this.connection.close();
                //this.destination.close();
            } catch (JMSException ex) {
                //Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("DEPOSITO "+this.ID+": AA");
            }
        }
        if(this.asyncConsumer!=null){
            try {
                this.asyncConsumer.close();
            } catch (JMSException ex) {
                Logger.getLogger(Deposito.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void execution() throws JMSException{
        boolean centinela= true;
        MessageProducer producer= null;
        
        try {
            //try{
            this.before();
        } catch (JMSException ex) {
            Logger.getLogger(Deposito.class.getName()).log(Level.SEVERE, null, ex);
        }
            System.out.println("DEPOSITO "+this.ID+": Comienza a enviar coches.");
            
            while(centinela){
                try{
                    producer= this.session.createProducer(this.destination);
                    Coche nuevo= new Coche(idCoche.getAndIncrement(),this.ID,Utils.TipoReserva.getTipoReserva(Utils.random.nextInt(Utils.VALOR_GENERACION)));
                    //System.out.println(nuevo.toCSV());
                    
                    String cuerpo= this.gson.encode(nuevo, Coche.class);
                    TextMessage message= session.createTextMessage(cuerpo);
                    producer.send(message);
                    TimeUnit.SECONDS.sleep(Utils.TIEMPO_CREAR_COCHE);
                }catch (JMSException ex) {
                //Logger.getLogger(Deposito.class.getName()).log(Level.SEVERE, null, ex);
                    centinela= false;
                } catch (InterruptedException ex) {
                //Logger.getLogger(Deposito.class.getName()).log(Level.SEVERE, null, ex);
                centinela= false;
                }
            //Logger.getLogger(Deposito.class.getName()).log(Level.SEVERE, null, ex);
                
                producer.close();
                
            }
            
    }
}