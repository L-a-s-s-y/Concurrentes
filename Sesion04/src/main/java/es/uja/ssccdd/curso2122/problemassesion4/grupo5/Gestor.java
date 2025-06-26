package es.uja.ssccdd.curso2122.problemassesion4.grupo5;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Luis López Ruiz (llopez)
 */
public class Gestor implements Callable<Informe>{
    
    private int id_gestor;
    private ArrayList<Coche> coches_asignar;
    private ArrayList<Coche> coches_no_asignados;
    private ArrayList<Reserva> reservas_gestor;

    public Gestor(int id_gestor, ArrayList<Coche> coches_asignar, ArrayList<Coche> coches_no_asignados, ArrayList<Reserva> reservas_gestor) {
        this.id_gestor = id_gestor;
        this.coches_asignar = coches_asignar;
        this.coches_no_asignados = coches_no_asignados;
        this.reservas_gestor = reservas_gestor;
    }

    @Override
    public Informe call() throws Exception {
        System.out.println("GESTOR "+this.id_gestor+": Comienza la organización de coches");
        int coches_procesados= 0;
        int coches_reservados= 0;
        
        for (Coche coche: this.coches_asignar){
            boolean centinela= false;
            for (int i = 0; i < this.reservas_gestor.size()&&!centinela; i++) {
                if(!this.reservas_gestor.get(i).reservaCompleta()){
                    centinela= this.reservas_gestor.get(i).addCoche(coche);
                    if(centinela){
                        coches_reservados++;
                    }
                }
            }
            if(!centinela){
                this.coches_no_asignados.add(coche);
            }
            coches_procesados++;
            try{
                TimeUnit.MILLISECONDS.sleep(Utils.TIEMPO_ESPERA_ASIGNACION_MAX-Utils.random.nextInt(Utils.TIEMPO_ESPERA_ASIGNACION_MAX-Utils.TIEMPO_ESPERA_ASIGNACION_MIN+1));
            } catch (InterruptedException ex) {
                Logger.getLogger(Gestor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("GESTOR "+this.id_gestor+": Finalizada la organización de coches");
        Informe informe= new Informe(Thread.currentThread().getId(),coches_procesados,(100f*coches_procesados / this.coches_asignar.size()),coches_reservados);

        return informe;
    }
    
}
