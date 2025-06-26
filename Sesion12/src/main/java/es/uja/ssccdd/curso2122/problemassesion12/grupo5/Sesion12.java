package es.uja.ssccdd.curso2122.problemassesion12.grupo5;

import java.util.ArrayList;
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
 * @author José Luis López Ruiz - llopez@ujaen.es
 */
public class Sesion12 {

    /**
     * Método encargado de limpiar los buffers de mensajes anteriores.
     *
     * @param buffers Dirección relativa de los buffers.
     * @throws JMSException Lanza esta excepción en caso de haber algún problema
     * con la conexión.
     */
    public static void limpiaBuffers(String[] buffers) throws JMSException {
        // Creación de la conexión.
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Utils.CONNECTION);
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
            consumer = sesion.createConsumer(sesion.createQueue(Utils.QUEUE + "." + buffer));
            MessageProducer producer = sesion.createProducer(sesion.createQueue(Utils.QUEUE + "." + buffer));

            do {
                mensaje = (TextMessage) consumer.receiveNoWait();
            } while (mensaje != null);  // Obtenemos mensajes hasta que esté vacío.

            consumer.close();
        }

        connection.close();
    }

    public static void main(String[] args) {
        String[] buffers = {"deposito0", "deposito1","deposito2","deposito3","deposito4"};    // Dirección relativa.

        // Limpieza de mensajes previos.
        try {
            limpiaBuffers(buffers);
            System.out.println("MAIN: Comienza la ejecución.");
            ExecutorService harry = (ExecutorService) Executors.newFixedThreadPool(Utils.NUM_HILOS);
            ArrayList<Reserva> reservas= new ArrayList<>();
            ArrayList<Deposito> depositos= new ArrayList<>();
            for (int i = 0; i < Utils.NUM_DEPOSITOS; i++) {
                Deposito deposito= new Deposito();
                depositos.add(deposito);
                harry.execute(deposito);
            }
            Gestor gestor= new Gestor(reservas);
            Gestor gestor1= new Gestor(reservas);
            harry.execute(gestor);
            harry.execute(gestor1);

            try {
                TimeUnit.SECONDS.sleep(Utils.TIEMPO_MAIN);
            } catch (InterruptedException ex) {
                //Logger.getLogger(Sesion11.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("AAAAAAAAA");
            }
            harry.shutdownNow();
            try {
                harry.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException ex) {
                Logger.getLogger(Sesion12.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Coches creados: "+depositos.get(0).getIdCoche());
            System.out.println("MAIN: This is the End...?");
            
            System.out.println("MAIN: Shutdown: "+harry.isShutdown());
            System.out.println("MAIN: Terminated: "+harry.isTerminated());
        } catch (JMSException ex) {
            System.out.println("HiloPrincipal: Problema con la conexión JMS");
        }
    }
}
