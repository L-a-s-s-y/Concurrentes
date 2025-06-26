/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.segundapractica;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pedroj
 */
public class SegundaPractica {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("MAIN: Comienza la ejecución.");
        Monitor monitor= new Monitor();
        System.out.println("MAIN: Creando el restaurante.");
        Restaurante juanito= new Restaurante(monitor);
        ExecutorService harry = (ExecutorService) Executors.newCachedThreadPool();
        ArrayList<Cliente> clientes= new ArrayList<>();
        harry.execute(juanito);
        int contador= 1;
        Date inicio= new Date();
        Date fin= new Date();
        System.out.println("MAIN: Comienzan a llegar los clientes.");
        while((fin.getTime()-inicio.getTime())<Utiles.TIEMPO_EJECUCION){
            Cliente nuevo= null;
            if(Utiles.aleatorio.nextBoolean()){
                nuevo= new Cliente(79483532+contador,monitor,Utiles.TipoCliente.PREMIUM);
            }
            else{
                nuevo= new Cliente(19483532+contador,monitor,Utiles.TipoCliente.ESTANDAR);
            }
            clientes.add(nuevo);
            harry.execute(nuevo);
            contador++;
            int llegada= Utiles.aleatorio.nextInt(Utiles.TIEMPO_LLEGADA_MAX-Utiles.TIEMPO_LLEGADA_MIN)+Utiles.TIEMPO_LLEGADA_MIN;
            try {
                TimeUnit.SECONDS.sleep(llegada);
            } catch (InterruptedException ex) {
                Logger.getLogger(SegundaPractica.class.getName()).log(Level.SEVERE, null, ex);
            }
            fin= new Date();
        }
        harry.shutdownNow();
        
        try {
            //Esta espera es simplmente para que no se solapen los prints en la consola.
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ex) {
            Logger.getLogger(SegundaPractica.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("MAIN: Fin de la jornada.");
        System.out.println("MAIN: Tiempo de ejecución: "+(fin.getTime()-inicio.getTime()));
        System.out.println("MAIN: Registro de los platos pedidos por cada cliente: ");
        for (Cliente i: clientes) {
            monitor.muestraEleccionPlatos(i.getDni(), i.getPlatos_pedidos());
        }
        System.out.println("MAIN: Estado de las mesas del restaurante cuando termina la ejecución: ");
        monitor.muestraMesas();
        System.out.println("MAIN: This is the End...?");
    }
    
}
