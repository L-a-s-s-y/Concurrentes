package es.uja.ssccdd.curso2122.problemassesion5.grupo5;

/**
 *
 * @author José Luis López Ruiz (llopez)
 */
public class Informe {
    private int gestorID;
    private int numReservasCompletadas;
    private int numCochesAsignados;
    private float porcentajeReservasNoCompletadas;

    public Informe() {
    }
    
    @Override
    public String toString() {
        return "el gestor " + gestorID + " ha conseguido completar " 
                + numReservasCompletadas + " reservas. En cambio un " + porcentajeReservasNoCompletadas
                + "% de reservas no han sido completadas. En total se han asignado "
                + numCochesAsignados + " coches";
    } 

    /**
     * @param gestorID
     */
    public void setGestorID(int gestorID) {
        this.gestorID = gestorID;
    }

    /**
     * @param numPlanesCompletados 
     */
    public void setNumReservasCompletadas(int numReservasCompletadas) {
        this.numReservasCompletadas = numReservasCompletadas;
    }

    /**
     * @param numCochesAsignados
     */
    public void setNumCochesAsignados(int numCochesAsignados) {
        this.numCochesAsignados = numCochesAsignados;
    }

    /**
     * @param porcentajeReservasNoCompletadas
     */
    public void setPorcentajeReservasNoCompletadas(float porcentajeReservasNoCompletadas) {
        this.porcentajeReservasNoCompletadas = porcentajeReservasNoCompletadas;
    }
}
