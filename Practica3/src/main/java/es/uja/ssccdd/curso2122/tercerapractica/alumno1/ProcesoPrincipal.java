/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.tercerapractica.alumno1;

//import es.uja.ssccdd.curso2122.tercerapractica.alumno2.Cliente;
import es.uja.ssccdd.curso2122.tercerapractica.utils.Constantes;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author pedroj
 */
public class ProcesoPrincipal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ExecutorService harry = (ExecutorService) Executors.newCachedThreadPool();
        Date inicio= new Date();
        Date fin= new Date();
        int contador= 0;
        System.out.println("MAIN: Comienzan a llegar los clientes.");
        while((fin.getTime()-inicio.getTime())<Constantes.TIEMPO_EJECUCION){
            Cliente nuevo= null;
            if(Constantes.aleatorio.nextBoolean()){
                nuevo= new Cliente(7355608+contador,Constantes.TipoCliente.PREMIUM);
            }
            else{
                nuevo= new Cliente(8735560 +contador,Constantes.TipoCliente.ESTANDAR);
            }
            harry.execute(nuevo);
            contador++;
            int llegada= Constantes.aleatorio.nextInt(Constantes.TIEMPO_LLEGADA_MAX-Constantes.TIEMPO_LLEGADA_MIN)+Constantes.TIEMPO_LLEGADA_MIN;
            try {
                TimeUnit.SECONDS.sleep(llegada);
            } catch (InterruptedException ex) {
                //Logger.getLogger(SegundaPractica.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("MAIN: Interrumpido");
            }
            fin= new Date();
        }
        harry.shutdownNow();
        System.out.println("MAIN: This is the End...?");
    }
    
}
