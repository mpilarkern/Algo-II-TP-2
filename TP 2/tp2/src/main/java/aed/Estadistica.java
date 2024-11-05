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
        actualizarSuperavitHeap(); 
        
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

    private void actualizarSuperavitHeap(){
        //COMPLETAR (VER HANDLES) //en este caso, el handle es el ID de la ciudad, entonces al elem con handle id origen le sumo el costo del traslado, y al del id destino se lo resto, luego se reordena el heap
    }

    public double gananciaPromedioPorTraslado(){
        return (double) gananciaTotal/cantDespachados;
    }


}
