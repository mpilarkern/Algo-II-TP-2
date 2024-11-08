package aed;
import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.ArrayList;

import aed.InfoCiudad.GananciasComparator;
import aed.InfoCiudad.PerdidasComparator;
import aed.InfoCiudad.SuperavitComparator;

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
    private GananciasComparator ganaciasComparator; //lo uso?
    private PerdidasComparator perdidasComparator;// lo uso?


    public Estadistica(int cantCiudades){
        infoCiudades = new InfoCiudad[cantCiudades];
        cantDespachados = 0;
        mayorSuperavit = new Heap(superavitComparator);
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
            ArrayList<Integer> listaNueva = new ArrayList<Integer>(); //es eficiente en terminos de memoria?? complejidad? podriamos usar clear()?
            listaNueva.add(ciudad);
            ciudadesConMayorGanancia = listaNueva;
        }
    }

    private void actualizarCiudadesConMayorPerdida(int ciudad){
        InfoCiudad infoCiudad = infoCiudades[ciudad];
        if (infoCiudad.perdida == perdidaMaxima){
            ciudadesConMayorPerdida.add(ciudad);
        }
        else if (infoCiudad.perdida > perdidaMaxima){
            ArrayList<Integer> listaNueva = new ArrayList<Integer>(); //es eficiente en terminos de memoria?? complejidad?
            listaNueva.add(ciudad);
            ciudadesConMayorPerdida = listaNueva;
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
            if (indiceFinal < indiceInicial){ //el elemento subió pues su superavit incrementó por encima del del padre
                int i = indiceInicial; 
                while (i > indiceFinal){                    
                    int id_ciudad = mayorSuperavit.obtenerElemento(i).id; 
                    indicesMayorSuperavit[id_ciudad] = i;
                    i = (i-1) / 2;  
                }
            } 
            else if (indiceInicial < indiceFinal){ //el elemento bajó pues su superavit disminuyó por debajo del de alguno de sus hijos
                int i = indiceInicial; 
                while (i > indiceFinal){                    
                    int id_ciudad = mayorSuperavit.obtenerElemento(i).id; 
                    indicesMayorSuperavit[id_ciudad] = i;
                    i = (i-1) / 2;  
                }
            } 

        }
    }

    public double gananciaPromedioPorTraslado(){
        return (double) gananciaTotal/cantDespachados;
    }


}


/*
 *             else if (indiceInicial < indiceFinal){ //el elemento bajó pues su superavit disminuyó por debajo del de alguno de sus hijos
                int i = indiceInicial; 
                int indiceMayorHijo = indiceInicial;    

                while (i < indiceFinal){                
                    int id_ciudad = mayorSuperavit.obtenerElemento(i).id;
                    InfoCiudad infoCiudadIzq = mayorSuperavit.obtenerElemento(2*i+1); 
                    InfoCiudad infoCiudadDer = mayorSuperavit.obtenerElemento(2*i+2);
                    int id_ciudadIzq = infoCiudadIzq.id; 
                    int id_ciudadDer = infoCiudadDer.id;

                    if (superavitComparator.compare(infoCiudadIzq, infoCiudadDer) > 0){
                        id_ciudad = id_ciudadIzq;
                        indiceMayorHijo = 2*i+1;
                    } 
                    else{
                        id_ciudad = id_ciudadDer;
                        indiceMayorHijo = 2*i+2;
                    }
                    i = indiceMayorHijo;
                    indicesMayorSuperavit[id_ciudad] = i; //REVISAR BIEN!!!!!
                }
 */
