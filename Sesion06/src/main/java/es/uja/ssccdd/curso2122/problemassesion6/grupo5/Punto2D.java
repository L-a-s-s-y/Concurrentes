package es.uja.ssccdd.curso2122.problemassesion6.grupo5;

/**
 *
 * @author José Luis López Ruiz (llopez).
 */
public class Punto2D {
    private float x;    // Componente X.
    private float y;    // Componente Y.

    public Punto2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + "x=" + x + ", y=" + y + ')';
    }

    
    
    
}


