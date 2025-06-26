package es.uja.ssccdd.curso2122.problemassesion5.grupo5;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Luis López Ruiz (llopez)
 */
public class Gestor implements Callable<Informe>{
    // Atributos.
    private int ID;
    private static int ID_siguiente;
    private final ArrayList<Coche> cochesDesechados;
    private final ArrayList<Reserva> reservas;
    private ArrayList<Coche> cochesDisponibles;
    private CompletionService executor;
    private final ReentrantLock lock;
  
    // Contadores para generar informe.
    private int numCochesAsignados;
    private int numReservasCompletadas;
    
    public Gestor( ArrayList<Coche> cochesDesechados, 
            ArrayList<Reserva> reservas, CompletionService executor, ReentrantLock lock) {
        this.lock= lock;
        this.lock.lock();
        this.ID= this.ID_siguiente ++;
        this.lock.unlock();
        this.cochesDesechados = cochesDesechados;
        this.reservas = reservas;
        this.executor= executor;
        
        this.numCochesAsignados = 0;
        this.numReservasCompletadas = 0;
    }
    
    public void setCochesDisponibles(ArrayList<Coche> cochesDisponibles) {
        this.cochesDisponibles = cochesDisponibles;
    }

    private int organizaCoche(int cocheIndice) {
        int reservaIndice = -1;
        Coche coche = cochesDisponibles.get(cocheIndice);
        
        for(int i = 0; i < this.reservas.size() && reservaIndice == -1; i++)
            if (this.reservas.get(i).addCoche(coche)) {
                reservaIndice = i;
                this.numCochesAsignados++;
            }
        
        if (reservaIndice == -1)
            cochesDesechados.add(coche);
        else if (this.reservas.get(reservaIndice).reservaCompleta())
            this.numReservasCompletadas++;
                    
        return reservaIndice;
    }
    
    @Override
    public Informe call() {
        System.out.println("Gestor " + this.ID + ": ha empezado su ejecución");
        Informe informe = new Informe();
        try {
            Future<ArrayList<Coche>> futuro= this.executor.take();
            this.cochesDisponibles= futuro.get();
        } catch (InterruptedException ex) {
            Logger.getLogger(Gestor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(Gestor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {   // Si nos interrumpen finalizamos la reserva de coches,
                // y devolvemos un informe parcial.
                
            // Asignación de los coches.
            for (int i = 0; i < this.cochesDisponibles.size(); i++) {
                int tiempoAleatorio = Utils.random.nextInt(Utils.TIEMPO_ESPERA_ASIGNACION_MAX - Utils.TIEMPO_ESPERA_ASIGNACION_MIN) + Utils.TIEMPO_ESPERA_ASIGNACION_MIN;
                TimeUnit.MILLISECONDS.sleep(tiempoAleatorio);
                this.organizaCoche(i);
            }
        } catch(InterruptedException e) {
            System.out.println("Gestor " + this.ID + ": ha sido interrumpido");
        }

        // Completamos el informe.
        informe.setGestorID(ID);
        informe.setNumReservasCompletadas(this.numReservasCompletadas);
        informe.setNumCochesAsignados(this.numCochesAsignados);
        informe.setPorcentajeReservasNoCompletadas((this.reservas.size() - this.numReservasCompletadas) / (float) this.reservas.size() * 100.0f);
        
        System.out.println("Gestor " + this.ID + ": ha terminado su ejecución");
        return informe;
    }
}

