/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.tercerapractica.alumno2;
        
import es.uja.ssccdd.curso2122.tercerapractica.utils.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
 * @author pedroj
 */
public class Main{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ExecutorService harry = (ExecutorService) Executors.newCachedThreadPool();
        Cocinero juanito= new Cocinero();
        harry.execute(juanito);
        Restaurante chikito= new Restaurante();
        harry.execute(chikito);

        try {
            TimeUnit.MILLISECONDS.sleep(Constantes.TIEMPO_EJECUCION+5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        harry.shutdownNow();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("*********HISTORIAL DE COMANDAS*********");
        for (Comanda i: juanito.getHistorialComandas()) {
            System.out.println(i);
        }
        System.out.println("MAIN: This is the End...?");
    }
    
    public static void limpiaBuffers(String[] buffers) throws JMSException {
        // Creación de la conexión.
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Constantes.CONNECTION);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        // Estableciendo una sesión.
        Session sesion = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Inicializando variables auxiliares.
        Destination destination = null;
        MessageConsumer consumer = null;
        TextMessage mensaje = null;

        // Limpieza de los buffers.
        for (String buffer : buffers) {
            //consumer = sesion.createConsumer(sesion.createQueue(Utils.QUEUE + "." + buffer));
            consumer = sesion.createConsumer(sesion.createQueue(Constantes.DESTINO+"."+buffer));
            //MessageProducer producer = sesion.createProducer(sesion.createQueue(Utils.QUEUE + "." + buffer));

            do {
                mensaje = (TextMessage) consumer.receiveNoWait();
            } while (mensaje != null);  // Obtenemos mensajes hasta que esté vacío.

            consumer.close();
        }

        connection.close();
    }
}
