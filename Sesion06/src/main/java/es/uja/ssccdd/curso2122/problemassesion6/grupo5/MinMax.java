package es.uja.ssccdd.curso2122.problemassesion6.grupo5;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Luis López Ruiz (llopez)
 */
public class MinMax extends RecursiveTask<Resultado> {
    private Punto2D puntos[];
    private int inicio;
    private int fin;
    //private Resultado resultado;

    public MinMax(Punto2D[] puntos, int inicio, int fin) {
        this.puntos = puntos;
        this.inicio = inicio;
        this.fin = fin;
        //this.resultado= resultado;
    }

    @Override
    protected Resultado compute() {
        Resultado resultado= null;
        if((this.fin-this.inicio)<Utils.UMBRAL){
            float minimoX= this.puntos[this.inicio].getX();
            float minimoY= this.puntos[this.inicio].getY();
            float maximoX= this.puntos[this.inicio].getX();
            float maximoY= this.puntos[this.inicio].getY();
            
            for (int i = this.inicio; i < this.fin; i++) {
                if(this.puntos[i].getX()<minimoX){
                    minimoX= this.puntos[i].getX();
                }
                if(this.puntos[i].getY()<minimoY){
                    minimoY= this.puntos[i].getY();
                }
                if(this.puntos[i].getX()>maximoX){
                    maximoX= this.puntos[i].getX();
                }
                if(this.puntos[i].getY()>maximoY){
                    maximoY= this.puntos[i].getY();
                }
            }
            Punto2D puntoMinimo= new Punto2D(minimoX,minimoY);
            Punto2D puntoMaximo= new Punto2D(maximoX,maximoY);
            resultado= new Resultado(puntoMinimo,puntoMaximo);
            
        }
        else{
            int mitad= (this.inicio+this.fin)/2;
            MinMax diestra= new MinMax(this.puntos, this.inicio, mitad);
            MinMax zurda= new MinMax(this.puntos, mitad, this.fin);
            invokeAll(diestra,zurda);
            try {
                resultado= this.agrupar(diestra.get(), zurda.get());
            } catch (InterruptedException ex) {
                Logger.getLogger(MinMax.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(MinMax.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return resultado;
    }

    private Resultado agrupar(Resultado resultado1, Resultado resultado2){
        float minX;
        float minY;
        float maxX;
        float maxY;
        
        if(resultado1.getMin().getX()<resultado2.getMin().getX()){
            minX=resultado1.getMin().getX();
        }
        else{
            minX=resultado2.getMin().getX();
        }
        
        if(resultado1.getMin().getY()<resultado2.getMin().getY()){
            minY=resultado1.getMin().getY();
        }
        else{
            minY=resultado2.getMin().getY();
        }
        
        if(resultado1.getMax().getX()>resultado2.getMax().getX()){
            maxX=resultado1.getMax().getX();
        }
        else{
            maxX=resultado2.getMax().getX();
        }
        
        if(resultado1.getMax().getY()>resultado2.getMax().getY()){
            maxY=resultado1.getMax().getY();
        }
        else{
            maxY=resultado2.getMax().getY();
        }
        
        Punto2D minimo= new Punto2D(minX,minY);
        Punto2D maximo= new Punto2D(maxX,maxY);
        Resultado resultado= new Resultado(minimo,maximo);
        return resultado;
    }
}
