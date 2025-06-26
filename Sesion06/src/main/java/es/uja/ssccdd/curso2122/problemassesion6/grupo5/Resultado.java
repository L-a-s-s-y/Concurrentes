package es.uja.ssccdd.curso2122.problemassesion6.grupo5;

/**
 *
 * @author José Luis López Ruiz (llopez)
 */
public class Resultado {
    private Punto2D min;    // Punto 2D que almacena la coordenadas mínimas de (x, y).
    private Punto2D max;    // Punto 2D que almacena la coordenadas máximas de (x, y).

    @Override
    public String toString() {
        return "Resultado{" + "min=" + min + ", max=" + max + '}';
    }

    public Resultado(Punto2D min, Punto2D max) {
        this.min = min;
        this.max = max;
    }

    public Punto2D getMin() {
        return min;
    }

    public void setMin(Punto2D min) {
        this.min = min;
    }

    public Punto2D getMax() {
        return max;
    }

    public void setMax(Punto2D max) {
        this.max = max;
    }
    
}
