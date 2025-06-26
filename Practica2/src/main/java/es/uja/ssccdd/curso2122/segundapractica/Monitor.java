/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uja.ssccdd.curso2122.segundapractica;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author El Boss
 */
public class Monitor {
    
    private final ArrayList<Plato> carta;
    private final PriorityQueue<Comanda> comandas;
    private final ArrayList<Cliente> cola_clientes;
    private final ArrayList<Cliente> colaNoPremium;
    private final ArrayList<Cliente> comensales;
    private double recaudacion;
    private final ArrayList<Mesa> mesas;
    private final ReadWriteLock lockCola;
    private final ReadWriteLock lockComensal;
    private final ReadWriteLock lockMesa;
    private final ReadWriteLock lockNoPremium;
    //private final ReadWriteLock lockNoPremium;
    private final ReadWriteLock lockComandas;
    private final ReentrantLock lockPagar;

    public Monitor() {
        Utiles.aleatorio.setSeed(79483521);
        this.recaudacion= 0.0;
        this.comensales= new ArrayList<>();
        this.carta= new ArrayList<>();
        this.cola_clientes= new ArrayList<>();
        this.colaNoPremium= new ArrayList<>();
        this.mesas= new ArrayList<>();
        this.comandas= new PriorityQueue<>();
        this.lockComensal= new ReentrantReadWriteLock();
        this.lockCola= new ReentrantReadWriteLock();
        this.lockMesa= new ReentrantReadWriteLock();
        this.lockNoPremium= new ReentrantReadWriteLock();
        this.lockComandas= new ReentrantReadWriteLock();
        this.lockPagar= new ReentrantLock();
        
        //Se generan las mesas.
        for (int i = 0; i < Utiles.AFORO; i++) {
            Mesa nueva= new Mesa(i, true);
            this.mesas.add(nueva);
        }
        
        //Se generan la carta. 
        for (int i = 0; i < Utiles.CARTA_TAM; i++) {
            double precio= (double)(Utiles.aleatorio.nextInt(Utiles.PRECIO_MAX-Utiles.PRECIO_MIN)+Utiles.PRECIO_MIN);
            Plato nuevo= new Plato(i,precio);
            this.carta.add(nuevo);
        }
    }
    
    /**
     * Comprueba el estado del aforo.
     * @return True en caso de que haya sitio, false en caso contrario.
     */
    public boolean comprobarAforo(){
        //System.out.println("BLOQUEO 2");
        this.lockComensal.readLock().lock();
        boolean resultado= (this.comensales.size()<Utiles.AFORO);
        this.lockComensal.readLock().unlock();
        //System.out.println("BLOQUEO 2");
        return resultado;
    }
    
    /**
     * Dado el dni del cliente, si dicho dni es válido, se marca la mesa como libre.
     * @param dni Long 
     */
    public void dejarMesa(long dni){
        int posicion= this.buscarMesaPorDNI(dni);
        if(posicion!=-1){
            this.lockMesa.writeLock().lock();
            this.mesas.get(posicion).setLibre(true);
            this.mesas.get(posicion).setDni_cliente(-1);
            this.lockComensal.writeLock().lock();
            Cliente sokorro= this.comensales.remove(this.buscarComensalPorDNI(dni));
            sokorro.setDentro(false);
            this.lockComensal.writeLock().unlock();
            this.lockMesa.writeLock().unlock();
        }
    }
    
    /**
     * Dados los platos pedidos por un cliente, se suma el precio de cada plato a la recaudación.
     * @param idPlatos 
     */
    public void pagar(ArrayList<Integer> idPlatos){
        this.lockPagar.lock();
        for(Integer i: idPlatos){
            this.recaudacion+=this.carta.get(i).getPrecio();
        }
        this.lockPagar.unlock();
    }
    
    /**
     * Dado un cliente, hace la pedida de platos a comer y rellena el array de platos pedidos en el cliente.
     * @param comensal Cliente
     */
    public void elegirPlatos(Cliente comensal){
        //Numero de platos que se van a pedir
        int menu= Utiles.aleatorio.nextInt(Utiles.MAX_PLATOS-Utiles.MIN_PLATOS)+Utiles.MIN_PLATOS;
        //Mientras ese número no se haya logrado se piden más.
        while(comensal.getPlatos_pedidos().size()<menu){
            //Se toma un plato aleatorio de la carta
            int seleccion= Utiles.aleatorio.nextInt(Utiles.CARTA_TAM);
            //Se ve si es aceptable.
            if(comensal.getEstatus()==Utiles.TipoCliente.PREMIUM){
                if(this.carta.get(seleccion).getPrecio()>=Utiles.PRECIO_MIN_PREMIUM){
                    comensal.getPlatos_pedidos().add(this.carta.get(seleccion).getiD());
                    this.lockComandas.writeLock().lock();
                    this.comandas.add(new Comanda(comensal.getDni(),this.carta.get(seleccion)));
                    this.lockComandas.writeLock().unlock();
                }
            }
            else{
                if(this.carta.get(seleccion).getPrecio()<=Utiles.PRECIO_MAX_ESTANDAR){
                    comensal.getPlatos_pedidos().add(this.carta.get(seleccion).getiD());
                    this.lockComandas.writeLock().lock();
                    this.comandas.add(new Comanda(comensal.getDni(),this.carta.get(seleccion)));
                    this.lockComandas.writeLock().unlock();
                }
            }
        }
    }
    
    /**
     * Dado un cliente, entra en el restaurante si puede.
     * @param comensal Cliente
     * @return True en caso de que hay podido entrar, false en caso contrario.
     */
    public boolean entrarEnRestaurante(Cliente comensal){
        this.lockComensal.writeLock().lock();
        if(this.comensales.size()<this.mesas.size()){
                int mesaAsignada= this.mesaDisponible();
                if(mesaAsignada!=-1){
                    this.lockMesa.writeLock().lock();
                    this.mesas.get(mesaAsignada).setDni_cliente(comensal.getDni());
                    this.mesas.get(mesaAsignada).setLibre(false);
                    this.lockMesa.writeLock().unlock();
                    this.comensales.add(comensal);
                    this.actualizarTurnos();
                    comensal.setDentro(true);
                    System.out.println("El cliente con DNI "+comensal.getDni()+" entra al restaurante.");
                    comensal.despertar();
                    this.lockComensal.writeLock().unlock();
                    return true;
                }
        }
        this.lockComensal.writeLock().unlock();
        return false;
    }
    
    /**
     * Atienede y cocina la comanda.
     * @return True si la ha podido cocinar, false en caso contrario.
     * @throws InterruptedException 
     */
    public boolean cocinarEtServir() throws InterruptedException{
        this.lockComandas.writeLock().lock();
        if(!this.comandas.isEmpty()){
            Comanda atender= this.comandas.poll();
            if(atender!=null){
                //Cocinando
                TimeUnit.SECONDS.sleep(Utiles.TIEMPO_COCINACION);
                this.lockComandas.writeLock().unlock();
                //System.out.println("Plato "+atender.getPlato().getiD()+" de la carta del cliente "+atender.getID_cliente()+" preparado");
                this.comensales.get(this.buscarComensalPorDNI(atender.getID_cliente())).despertar();
                return true;
            }
        }
        this.lockComandas.writeLock().unlock();
        return false;
    }
    
    /**
     * Mira si hay demandas en la cola de demandas.
     * @return True si hay, false en caso contrario.
     */
    public boolean hayComandas(){
        this.lockComandas.readLock().lock();
        boolean resultado= !(this.comandas.isEmpty());
        this.lockComandas.readLock().unlock();
        return resultado;
    }
    
    /**
     * Devuelve la posición en el array de mesas de la primera mesa libre o -1 si no hay ninguna.
     * @return Int
     */
    private int mesaDisponible(){
        this.lockMesa.readLock().lock();
        for (int i = 0; i < this.mesas.size(); i++) {
            if(this.mesas.get(i).isLibre()){
                this.lockMesa.readLock().unlock();
                return i;
            }
        }
        this.lockMesa.readLock().unlock();
        return -1;
    }
    
    /**
     * Inserta al cliente proporcionado en la cola de espera.
     * @param nuevo Cliente
     */
    public void insertarCola(Cliente nuevo){
        
        if(nuevo.getEstatus()==Utiles.TipoCliente.PREMIUM){
            this.lockCola.writeLock().lock();
            this.cola_clientes.add(nuevo);
            this.lockCola.writeLock().unlock();
        }
        else{
            this.lockNoPremium.writeLock().lock();
            this.colaNoPremium.add(nuevo);
            this.lockNoPremium.writeLock().unlock();
        }
        System.out.println("El cliente con DNI "+nuevo.getDni()+" llega a la cola.");
        
    }
    
    /**
     * Mira si hay gente esperando en alguna cola.
     * @return True en caso de afirmativo, false en caso contrario.
     */
    public boolean genteEnCola(){
        //System.out.println("BlOQUEO 1 A");
        this.lockCola.readLock().lock();
        //System.out.println("BlOQUEO 1 B");
        this.lockNoPremium.readLock().lock();
        //System.out.println("BlOQUEO 1 C");
        boolean resultado= !(this.cola_clientes.isEmpty() && this.colaNoPremium.isEmpty());
        this.lockNoPremium.readLock().unlock();
        this.lockCola.readLock().unlock();
        //System.out.println("BLOQUEO 1 D");
        return resultado;
    }
    
    /**
     * Mira si hay gente esperando en la cola premium.
     * @return True en caso de afirmativo, false en caso contrario.
     */
    public boolean genteEnColaPremium(){
        this.lockCola.readLock().lock();
        boolean resultado= !(this.cola_clientes.isEmpty());
        this.lockCola.readLock().unlock();
        return resultado;
    }
    
    /**
     * Mira si hay gente esperando en la cola no Premium.
     * @return True en caso de afirmativo, false en caso contrario.
     */
    public boolean genteEnColaNoPremium(){
        this.lockNoPremium.readLock().lock();
        boolean resultado= !(this.colaNoPremium.isEmpty());
        this.lockNoPremium.readLock().unlock();
        return resultado;
    }
    
    /**
     * Retorna pero no elimina al siguiente cliente en la cola Premium.
     * @return Cliente
     */
    public Cliente mirarColaPremium(){
        this.lockCola.readLock().lock();
        if(!(this.cola_clientes.isEmpty())){
            Cliente siguiente= this.cola_clientes.get(0);
            this.lockCola.readLock().unlock();
            if(siguiente!=null){
                return siguiente;
            }
        }
        this.lockCola.readLock().unlock();
        return null;
    }
    
    
    /**
     * Retorna pero no elimina al siguiente cliente en la cola no Premium.
     * @return Cliente
     */
    public Cliente mirarColaNoPremium(){
        this.lockNoPremium.readLock().lock();
        if(!(this.colaNoPremium.isEmpty())){
            Cliente siguiente= this.colaNoPremium.get(0);
            this.lockNoPremium.readLock().unlock();
            if(siguiente!=null){
                return siguiente;
            }
        }
        this.lockNoPremium.readLock().unlock();
        return null;
    }
    
    /**
     * Retorna y elimina al siguiente cliente en la cola Premium.
     * @return Cliente
     */
    public Cliente siguienteColaPremium(){
        this.lockCola.writeLock().lock();
        if(!(this.cola_clientes.isEmpty())){
            Cliente siguiente= this.cola_clientes.remove(0);
            this.lockCola.writeLock().unlock();
            if(siguiente!=null){
                return siguiente;
            }
        }
        this.lockCola.writeLock().unlock();
        return null;
    }
    
    /**
     * Retorna y elimina al siguiente cliente en la cola no Premium.
     * @return Cliente
     */
    public Cliente siguienteColaNoPremium(){
        this.lockNoPremium.readLock().lock();
        if(!(this.colaNoPremium.isEmpty())){
            Cliente siguiente= this.colaNoPremium.remove(0);
            this.lockNoPremium.readLock().unlock();
            if(siguiente!=null){
                return siguiente;
            }
        }
        this.lockNoPremium.readLock().unlock();
        return null;
    }
    
    /**
     * Devuelve la posicion en el array de comensales del comensal con el dni proporcionado, o -1 si no está.
     * @param dni Long
     * @return Int
     */
    private int buscarComensalPorDNI(long dni){
        this.lockComensal.readLock().lock();
        for (int i = 0; i < this.comensales.size(); i++) {
            if(this.comensales.get(i).getDni()==dni){
                this.lockComensal.readLock().unlock();
                return i;
            }
        }
        this.lockComensal.readLock().unlock();
        return -1;
    }
    
    /**
     * Actualiza los turnos que llevan esperando los clientes no premium.
     * Si alguno supera el límite lo inserta el primero en la cola premium.
     */
    public void actualizarTurnos(){
        this.lockNoPremium.writeLock().lock();
        for (int i = 0; i < this.colaNoPremium.size(); i++) {
            this.colaNoPremium.get(i).incrementarTurno();
            if(this.colaNoPremium.get(i).getTurnos()>=Utiles.LIMITE_TURNOS){
                Cliente nuevo= this.colaNoPremium.remove(i);
                this.lockCola.writeLock().lock();
                Cliente antiguo= this.cola_clientes.set(0, nuevo);
                this.cola_clientes.add(antiguo);
                this.lockCola.writeLock().unlock();
                System.out.println("UN CLIENTE HA SIDO ASCENDIDO");
            }
        }
        this.lockNoPremium.writeLock().unlock();
    }
    
    /**
     * Dado un dni, retorna la posición de la mesa con dicho dni asociado -1 en caso de que no exista.
     * @param ID int
     * @return int posicion o -1.
     */
    private int buscarMesaPorDNI(long dni){
        this.lockMesa.readLock().lock();
        for (int i = 0; i < this.mesas.size(); i++) {
            if(this.mesas.get(i).getDni_cliente()==dni){
                this.lockMesa.readLock().unlock();
                return i;
            }
        }
        this.lockMesa.readLock().unlock();
        return -1;
    }
    
    /**
     * Imprime por pantalla la carta.
     */
    public void muestraCarta(){
        for(Plato plato: this.carta){
            System.out.println(plato);
        }
    }
    
    /**
     * Imprime por pantalla la lista de mesas.
     */
    public void muestraMesas(){
        for(Mesa mesa: this.mesas){
            System.out.println(mesa);
        }
    }
    
    /**
     * Imprime por pantalla la lista de comensales.
     */
    public void muestraComensales(){
        for(Cliente cliente: this.comensales){
            System.out.println(cliente);
        }
    }
    
    /**
     * Imprime por pantalla la carta.
     */
    public void muestraCola(){
        for(Cliente cliente: this.cola_clientes){
            System.out.println(cliente);
        }
    }

    /**
     * Retorna la recaudación del restaurante hasta el momento.
     * @return double
     */
    public double getRecaudacion() {
        this.lockPagar.lock();
        double resultado= recaudacion;
        this.lockPagar.unlock();
        return resultado;
    }
    
    public ArrayList<Plato> getCarta() {
        return carta;
    }
     
    public void muestraEleccionPlatos(long dni, ArrayList<Integer> seleccion){
        System.out.println("CLIENTE "+dni+": Platos pedido:");
        for(Integer i: seleccion){
            //System.out.println("Plato ID: "+i);
            System.out.println(this.carta.get(i));
        }
    }
    
}
