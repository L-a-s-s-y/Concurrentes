package es.uja.ssccdd.curso2122.problemassesion12.grupo5;

import java.util.ArrayList;
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
 * @author José Luis López Ruiz (llopez)
 */
public class Gestor implements Runnable{
    
    private final ArrayList<Reserva> reservas;
    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private int desechados;
    private GsonUtil<Coche> gson;

    public Gestor(ArrayList<Reserva> reservas) {
        this.gson= new GsonUtil<Coche>();
        this.reservas = reservas;
        this.desechados= 0;
    }

    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("GESTOR: Comienza a procesar.");
        for (int i = 0; i < Utils.NUM_RESERVAS; i++) {
            Reserva nueva= new Reserva(i, Utils.MAXIMO_COCHES_POR_RESERVA, Utils.TipoReserva.getTipoReserva(Utils.random.nextInt(Utils.VALOR_GENERACION)));
            this.reservas.add(nueva);
        }
        try{
            this.before();
            this.execution();
        } catch (JMSException ex) {
            //Logger.getLogger(Gestor.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            this.after();
        }
        System.out.println("GESTOR: Coches desechados: "+this.desechados);
        int completadas= 0;
        for (Reserva reserva: this.reservas) {
            if(reserva.reservaCompleta()){
                completadas++;
            }
        }
        System.out.println("GESTOR: Reservas completadas: "+completadas);
        System.out.println("GESTOR: Fin");
    }
    
    private void before() throws JMSException{
        this.connectionFactory= new ActiveMQConnectionFactory(Utils.CONNECTION);
        this.connection= this.connectionFactory.createConnection();
        this.connection.start();
        this.session= this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.destination= this.session.createQueue(Utils.QUEUE);
    }
    
    private boolean completas(){
        for (int i = 0; i < this.reservas.size(); i++) {
            if(!this.reservas.get(i).reservaCompleta()){
                return false;
            }
        }
        return true;
    }
    
    private void execution() throws JMSException{
        boolean centinela= true;
            while(centinela){
                MessageConsumer consumer= this.session.createConsumer(this.destination);
                TextMessage msg= (TextMessage) consumer.receive();
                //System.out.println("SYNC-CONSUMER "+this.ID+": Procesando la trabajación: "+msg.getText());
                //Coche nuevo= new Coche(msg.getText());
                //this.gson= new GsonUtil<Coche>();
                Coche nuevo= this.gson.decode(msg.getText(), Coche.class);
                System.out.println("GESTOR: "+nuevo);
                boolean desechado= false;
                for (int i = 0; i < this.reservas.size(); i++) {
                    if(this.reservas.get(i).addCoche(nuevo)){
                        i= this.reservas.size()+3;
                        desechado= true;
                        Destination destinoRespuesta= this.session.createQueue(Utils.QUEUE_DEPOSITO+nuevo.getiD_Creador());
                        MessageProducer respuesta= this.session.createProducer(destinoRespuesta);
                        TextMessage message= this.session.createTextMessage("Recibido");
                        respuesta.send(message);
                        respuesta.close();
                    }
                }
                if(!desechado){
                    this.desechados++;
                }
                if(Thread.currentThread().isInterrupted()||this.completas()){
                    centinela= false;
                    this.connection.stop();
                    consumer.close();
                    Thread.currentThread().interrupt();
                }
                consumer.close();
            }
        
    }
    private void after(){
        try{
            if(this.connection!=null){
                this.connection.close();
            }
        } catch (JMSException ex) {
            //Logger.getLogger(SyncConsumer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("GESTOR: AA");
        }
    }
}