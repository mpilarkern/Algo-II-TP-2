package aed;
import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.ArrayList;

// esta clase guarda la información sobre todo lo que ha sido despachado
// tiene varios atributos para poder modularizar facilmente y reducir las complejidades

//Las operaciones elementales, las operaciones sobre arrayList (constructor, add, set, get, clear, size) y
//los comparadores que creamos son O(1) así que, para facilitar la lectura, no aclararemos esto en cada aparición.

// llamamos C al conjunto de ciudades

public class Estadistica {
    private InfoCiudad[] infoCiudades;
    private int cantDespachados;
    private Heap<InfoCiudad> mayorSuperavit; // la cantidad de elementos de este heap es |C|, registra el superavit para cada ciudad
    private Integer[] indicesMayorSuperavit; // usamos una lista donde cada posicion refiere al id de una ciudad, el valor en esa posicion es su indice en el heap mayorSuperavit)
    private ArrayList<Integer> ciudadesConMayorGanancia;
    private int gananciaMaxima;
    private ArrayList<Integer> ciudadesConMayorPerdida;
    private int perdidaMaxima;
    private int gananciaTotal;
    private SuperavitComparator superavitComparator;

    public class SuperavitComparator implements Comparator <InfoCiudad>{
        @Override
        public int compare(InfoCiudad c1, InfoCiudad c2){
            int comparacionSuperavit = Integer.compare(c1.ganancia, c2.ganancia); //O(1)
            if (comparacionSuperavit != 0){
                return comparacionSuperavit;
            } else{
                return Integer.compare(c2.id, c1.id);//O(1)
            }
        }
    } //O(1)


    public Estadistica(int cantCiudades){
        infoCiudades = new InfoCiudad[cantCiudades]; //O(|C|)
        cantDespachados = 0;
        superavitComparator = new SuperavitComparator(); //O(1)
        mayorSuperavit = new Heap<>(superavitComparator); //O(1)
        indicesMayorSuperavit = new Integer[cantCiudades]; //O(|C|)
        ciudadesConMayorGanancia = new ArrayList<Integer>(); 
        gananciaMaxima = 0;
        ciudadesConMayorPerdida = new ArrayList<Integer>();
        perdidaMaxima = 0;
        gananciaTotal = 0;
        for (int i = 0; i < cantCiudades; i++){ 
            infoCiudades[i] = new InfoCiudad(i,0,0); //O(1)
        } // la cantidad de iteraciones es c por lo que la complejidad del for es O(1) + |C| * O(1) = O(|C|)
    } // O(|C|)

    public int ciudadConMayorSuperavit(){
        int id = -1; //si no hay informacion sobre ciudades (no hay una ciudad con mayor superavit) devuelve -1 pues un int no puede ser null
        if ( mayorSuperavit.maximo() != null){
            id =  mayorSuperavit.maximo().id; //O(1)
        } 
        return id;
    } // O(1)

    public ArrayList<Integer> ciudadesConMayorGanancia(){
        return ciudadesConMayorGanancia;
    } // O(1)

    public ArrayList<Integer> ciudadesConMayorPerdida(){
        return ciudadesConMayorPerdida;
    } // O(1)

    public void agregarDespachado(Traslado traslado){ // actualizamos la ganancia y la perdida de las ciudades involucradas en el traslado
        int ciudadOrigen = traslado.origen;
        int ciudadDestino = traslado.destino;
        int gananciaNeta = traslado.gananciaNeta;

        infoCiudades[ciudadOrigen].agregarGanancia(gananciaNeta);
        infoCiudades[ciudadDestino].agregarPerdida(gananciaNeta);

        actualizarCiudadesConMayorGanancia(ciudadOrigen); //O(1)
        actualizarCiudadesConMayorPerdida(ciudadDestino); //O(1)
        actualizarSuperavitHeap(ciudadOrigen); // O(log |C|)
        actualizarSuperavitHeap(ciudadDestino); // O(log |C|)

        gananciaTotal = gananciaTotal + gananciaNeta;
        
        cantDespachados ++;
    } // O(log |C|)

    private void actualizarCiudadesConMayorGanancia(int ciudad){
        InfoCiudad infoCiudad = infoCiudades[ciudad];
        if (infoCiudad.ganancia == gananciaMaxima){
            ciudadesConMayorGanancia.add(ciudad);
        } // O(1)
        else if (infoCiudad.ganancia > gananciaMaxima){
            ciudadesConMayorGanancia.clear();
            ciudadesConMayorGanancia.add(ciudad);
            gananciaMaxima = infoCiudad.ganancia;
        } // O(1)
    } // O(1)

    private void actualizarCiudadesConMayorPerdida(int ciudad){
        InfoCiudad infoCiudad = infoCiudades[ciudad];
        if (infoCiudad.perdida == perdidaMaxima){
            ciudadesConMayorPerdida.add(ciudad);
        } //O(1)
        else if (infoCiudad.perdida > perdidaMaxima){
            ciudadesConMayorPerdida.clear();       
            ciudadesConMayorPerdida.add(ciudad);
            perdidaMaxima = infoCiudad.perdida;
        } //O(1)
    } //O(1)

    private void actualizarSuperavitHeap(int ciudad){
        if (indicesMayorSuperavit[ciudad] == null){ // si esta ciudad aún no fue origen ni destino de ningun traslado despachado, significa que no está en el heap aún y hay que agregarla
            InfoCiudad infoCiudad = infoCiudades[ciudad]; //O(1)
            int indice = mayorSuperavit.agregar(infoCiudad); // O(log |C|)
            indicesMayorSuperavit[ciudad] = indice;
            int i = mayorSuperavit.tamaño()-1; 
            while (i > indice){ // empiezo a iterar desde el ultimo elemento del heap        
                int id_ciudad = mayorSuperavit.obtenerElemento(i).id; //ciudad_id es la posición en la lista indicesMayorSuperavit, O(1)
                indicesMayorSuperavit[id_ciudad] = i; // corrijo la referencia a la posicion del heap en la lista
                i = (i-1) / 2;  // itero subiendo al padre
            } // el while tiene complejidad O(log |C|) porque en el peor caso recorro la altura del heap que es log |C|
        }
        else{
            int indiceInicial = indicesMayorSuperavit[ciudad]; // O(1)
            int indiceFinal = mayorSuperavit.verificarPosicion(indiceInicial); // O(log |C|)
            indicesMayorSuperavit[ciudad] = indiceFinal;
            if (indiceFinal <= indiceInicial){ //si el indice final es menor significa que el elemento subió, es decir, su superavit incrementó por encima del padre
                int i = indiceInicial; 
                while (i > indiceFinal){  // reacomodo el heap con la misma logica que en el if anterior            
                    int id_ciudad = mayorSuperavit.obtenerElemento(i).id; 
                    indicesMayorSuperavit[id_ciudad] = i;
                    i = (i-1) / 2;  
                } // O(log |C|)
            } //O(log |C|)
            else if (indiceInicial < indiceFinal){//si el indice final es mayor significa que el elemento bajo, es decir, su superavit disminuyo por debajo de alguno de sus hijos
                int i = indiceInicial; 
                while (i >= indiceFinal){    // reacomodo                 
                    int id_ciudad = mayorSuperavit.obtenerElemento(i).id; 
                    indicesMayorSuperavit[id_ciudad] = i;
                    i = (i-1) / 2;  
                } // O(log |C|)
            } // O(log |C|)
        } // O(log |C|)
    } // O(log |C|)

    public int gananciaPromedioPorTraslado(){
        return gananciaTotal/cantDespachados;
    } //O(1)


}

