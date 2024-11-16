package aed;
import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.ArrayList;

// esta clase guarda la información sobre todo lo que ha sido despachado
// tiene varios atributos para poder modularizar facilmente y reducir las complejidades

//Las operaciones elementales, las operaciones sobre arrayList (constructor, add, set, get, clear, size) y
//los comparadores que creamos son O(1) así que, para facilitar la lectura, no aclararemos esto en cada aparición.

// llamamos c a la cantidad de ciudades

public class Estadistica {
    private InfoCiudad[] infoCiudades;
    private int cantDespachados;
    private Heap<InfoCiudad> mayorSuperavit;
    private Integer[] indicesMayorSuperavit;
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
        infoCiudades = new InfoCiudad[cantCiudades]; //O(c)
        cantDespachados = 0;
        superavitComparator = new SuperavitComparator(); //O(1)
        mayorSuperavit = new Heap<>(superavitComparator); //O(1)
        indicesMayorSuperavit = new Integer[cantCiudades]; //O(c)
        ciudadesConMayorGanancia = new ArrayList<Integer>(); 
        gananciaMaxima = 0;
        ciudadesConMayorPerdida = new ArrayList<Integer>();
        perdidaMaxima = 0;
        gananciaTotal = 0;
        for (int i = 0; i < cantCiudades; i++){ 
            infoCiudades[i] = new InfoCiudad(i,0,0); //O(1)
        } // la cantidad de iteraciones es c por lo que la complejidad del for es O(1) + c * O(1) = O(c)
    }

    public int ciudadConMayorSuperavit(){
        int id = -1; //si no hay informacion sobre ciudades (no hay una ciudad con mayor superavit) devuelve -1 pues un int no puede ser null
        if ( mayorSuperavit.maximo() != null){
            id =  mayorSuperavit.maximo().id;
        }
        return id;
    }

    public ArrayList<Integer> ciudadesConMayorGanancia(){
        return ciudadesConMayorGanancia;
    }

    public ArrayList<Integer> ciudadesConMayorPerdida(){
        return ciudadesConMayorPerdida;
    }

    public void agregarDespachado(Traslado traslado){
        int ciudadOrigen = traslado.origen;
        int ciudadDestino = traslado.destino;
        int gananciaNeta = traslado.gananciaNeta;

        infoCiudades[ciudadOrigen].agregarGanancia(gananciaNeta);
        infoCiudades[ciudadDestino].agregarPerdida(gananciaNeta);

        actualizarCiudadesConMayorGanancia(ciudadOrigen);
        actualizarCiudadesConMayorPerdida(ciudadDestino);
        actualizarSuperavitHeap(ciudadOrigen); 
        actualizarSuperavitHeap(ciudadDestino); 

        gananciaTotal = gananciaTotal + gananciaNeta;
        
        cantDespachados ++;
    }

    private void actualizarCiudadesConMayorGanancia(int ciudad){
        InfoCiudad infoCiudad = infoCiudades[ciudad];
        if (infoCiudad.ganancia == gananciaMaxima){
            ciudadesConMayorGanancia.add(ciudad);
        }
        else if (infoCiudad.ganancia > gananciaMaxima){
            ciudadesConMayorGanancia.clear();
            ciudadesConMayorGanancia.add(ciudad);
            gananciaMaxima = infoCiudad.ganancia;

        }
    }

    private void actualizarCiudadesConMayorPerdida(int ciudad){
        InfoCiudad infoCiudad = infoCiudades[ciudad];
        if (infoCiudad.perdida == perdidaMaxima){
            ciudadesConMayorPerdida.add(ciudad);
        }
        else if (infoCiudad.perdida > perdidaMaxima){
            ciudadesConMayorPerdida.clear();          //se puede usar clear()??
            ciudadesConMayorPerdida.add(ciudad);
            perdidaMaxima = infoCiudad.perdida;
        }
    }

    //fijarse heapify si esto no funca
    private void actualizarSuperavitHeap(int ciudad){
        if (indicesMayorSuperavit[ciudad] == null){ //Si no hay posición en el heap guardada para esta ciudad significa que no está en el heap aún y hay que agregarla
            InfoCiudad infoCiudad = infoCiudades[ciudad];
            int indice = mayorSuperavit.agregar(infoCiudad);
            indicesMayorSuperavit[ciudad] = indice;
            int i = mayorSuperavit.tamaño()-1; //falta actualizar las demas posiciones en el heap guardadas en la lista indicesMayorSuperavit para aquellas que hayan cambiado de posicion en el heap
            while (i > indice){              // i (e indice) refieren a posiciones en el heap (posiciones en el array que es atributo del heap)        
                int id_ciudad = mayorSuperavit.obtenerElemento(i).id; //ciudad_id es la posición en la lista indicesMayorSuperavit
                indicesMayorSuperavit[id_ciudad] = i;
                i = (i-1) / 2;  //como el algoritmo que usa agregar usa siftUp, hago un procedimiento similar pero que solo me devuelva el id de la ciudad en cada posicion del heap modficada
            }

        }
        else{
            int indiceInicial = indicesMayorSuperavit[ciudad];
            int indiceFinal = mayorSuperavit.verificarPosicion(indiceInicial);
            indicesMayorSuperavit[ciudad] = indiceFinal;
            if (indiceFinal <= indiceInicial){ //el elemento subió pues su superavit incrementó por encima del del padre
                int i = indiceInicial; 
                while (i > indiceFinal){                    
                    int id_ciudad = mayorSuperavit.obtenerElemento(i).id; 
                    indicesMayorSuperavit[id_ciudad] = i;
                    i = (i-1) / 2;  
                }
            } 
            else if (indiceInicial < indiceFinal){ //el elemento bajó pues su superavit disminuyó por debajo del de alguno de sus hijos
                int i = indiceInicial; 
                while (i >= indiceFinal){                    
                    int id_ciudad = mayorSuperavit.obtenerElemento(i).id; 
                    indicesMayorSuperavit[id_ciudad] = i;
                    i = (i-1) / 2;  
                }
            } 

        }
    }

    public int gananciaPromedioPorTraslado(){
        return gananciaTotal/cantDespachados;
    }


}

