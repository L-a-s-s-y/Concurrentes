/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.primerapractica;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pedroj
 */
public class PrimeraPractica {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //ExecutorService harry= (ThreadPoolExecutor)Executors.newFixedThreadPool();
        ExecutorService harry= (ThreadPoolExecutor)Executors.newCachedThreadPool();
        Sistema sistema= new Sistema(harry);
        ExecutorService harry_sistema= (ThreadPoolExecutor)Executors.newFixedThreadPool(1);
        harry_sistema.submit(sistema);
        try {
            TimeUnit.SECONDS.sleep(Utiles.TIEMPO_EJECUCION);
        } catch (InterruptedException ex) {
            Logger.getLogger(PrimeraPractica.class.getName()).log(Level.SEVERE, null, ex);
        }
        harry.shutdownNow();

        try {
            harry.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            Logger.getLogger(PrimeraPractica.class.getName()).log(Level.SEVERE, null, ex);
        }
        harry_sistema.shutdownNow();
        
    }
    
}
