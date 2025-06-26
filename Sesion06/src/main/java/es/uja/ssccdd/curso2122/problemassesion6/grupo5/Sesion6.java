package es.uja.ssccdd.curso2122.problemassesion6.grupo5;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Luis López Ruiz (llopez)
 */
public class Sesion6 {
    public static void main(String[] args) {
        // Crear un vector aleatorio de 10000 puntos. Cada coordenada debe generarse aleatoriamente entre [-100, 100].
        Punto2D array[]= new Punto2D[Utils.TAM_ARRAY];
        for (int i = 0; i < array.length; i++) {
            float laX= (float)Utils.random.nextInt(Utils.COTA)*Utils.random.nextFloat();
            float laY= (float)Utils.random.nextInt(Utils.COTA)*Utils.random.nextFloat();
            if(Utils.random.nextBoolean()){
                laX*=-1.0;
            }
            if(Utils.random.nextBoolean()){
                laY*=-1.0;
            }
            array[i]= new Punto2D(laX,laY);
            System.out.println("Array["+i+"]: "+array[i]);
        }
        // Crear un hilo de la clase MinMax y ejecutar en ForkJoinPool.
        MinMax algoritmo= new MinMax(array,0,array.length);
        ForkJoinPool harry= new ForkJoinPool();
        harry.execute(algoritmo);
        // Esperamos el resultado final y lo mostramos por pantalla.
        do {
            System.out.printf("******************************************\n");
            System.out.printf("Main: Parallelism: %d\n",harry.getParallelism());
            System.out.printf("Main: Active Threads: %d\n",harry.getActiveThreadCount());
            System.out.printf("Main: Task Count: %d\n",harry.getQueuedTaskCount());
            System.out.printf("Main: Steal Count: %d\n",harry.getStealCount());System.out.printf("******************************************\n");
            try {
                    TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!algoritmo.isDone());
        harry.shutdown();
        try {
            harry.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            Logger.getLogger(Sesion6.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Resultado resultado= algoritmo.get();
            System.out.println("MAIN: Resultado: "+resultado);
        } catch (InterruptedException ex) {
            Logger.getLogger(Sesion6.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(Sesion6.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
