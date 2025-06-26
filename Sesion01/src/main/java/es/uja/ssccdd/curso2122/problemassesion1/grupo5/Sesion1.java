/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package es.uja.ssccdd.curso2122.problemassesion1.grupo5;

import es.uja.ssccdd.curso2122.problemassesion1.grupo5.Utils.TipoReserva;
import java.util.ArrayList;

/**
 *
 * @author Adrian Luque Luque (alluque)
 */
public class Sesion1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<Reserva> reservas= new ArrayList<>();
        for (int i = 0; i < Utils.RESERVAS_A_GENERAR; i++) {
            Reserva nueva= new Reserva(i,TipoReserva.getTipoReserva(Utils.random.nextInt(Utils.VALOR_GENERACION)),Utils.random.nextInt(Utils.MAXIMO_COCHES_POR_RESERVA)+1);
            reservas.add(nueva);
        }
        System.out.println("\nRESERVAS GENERADAS");
        for (int i = 0; i < reservas.size(); i++) {
            System.out.println("MAIN: Reserva "+i+": "+reservas.get(i).toString());
        }
        
        int siguiente_reserva= 0;
        ArrayList<Coche> coches= new ArrayList<>();
        for (int i = 0; i < Utils.COCHES_A_GENERAR; i++) {
            Coche nuevo= new Coche(i,TipoReserva.getTipoReserva(Utils.random.nextInt(Utils.VALOR_GENERACION)));
            coches.add(nuevo);
            
            int comprobados= 0;
            boolean centinela= false;
            while(!centinela && comprobados<Utils.COCHES_A_GENERAR){
                if(reservas.get(siguiente_reserva).getGama_reserva().equals(nuevo.getGama_coche())){
                    reservas.get(siguiente_reserva).insertar_coche(nuevo);
                    centinela= true;
                }
                comprobados++;
                siguiente_reserva= (siguiente_reserva+1)%reservas.size();
            }
        }
        
        System.out.println("\nCOCHES GENERADOS");
        for (int i = 0; i < coches.size(); i++) {
            System.out.println("MAIN: Coche "+i+": "+coches.get(i).toString());
        }
        
        System.out.println("\nASIGNACIÓN");
        for (int i = 0; i < reservas.size(); i++) {
            System.out.println("Reserva: "+reservas.get(i).getId_reserva()+": "+reservas.get(i).toString());
        }
    }
    
}
