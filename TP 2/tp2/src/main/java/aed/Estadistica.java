package aed;
import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.ArrayList;

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
            int comparacionSuperavit = Integer.compare(c1.ganancia, c2.ganancia);
            if (comparacionSuperavit != 0){
                return comparacionSuperavit;
            } else{
                return Integer.compare(c2.id, c1.id);
            }
        }
    }


    public Estadistica(int cantCiudades){
        infoCiudades = new InfoCiudad[cantCiudades];
        cantDespachados = 0;
        superavitComparator = new SuperavitComparator();
        mayorSuperavit = new Heap<>(superavitComparator);
        indicesMayorSuperavit = new Integer[cantCiudades];
        ciudadesConMayorGanancia = new ArrayList<Integer>();
        gananciaMaxima = 0;
        ciudadesConMayorPerdida = new ArrayList<Integer>();
        perdidaMaxima = 0;
        gananciaTotal = 0;
        for (int i = 0; i < cantCiudades; i++){
            infoCiudades[i] = new InfoCiudad(i,0,0);
        }
    }

    public int ciudadConMayorSuperavit(){
        return mayorSuperavit.maximo().id;
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
            int indiceFinal = mayorSuperavit.revisar(indiceInicial);
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
