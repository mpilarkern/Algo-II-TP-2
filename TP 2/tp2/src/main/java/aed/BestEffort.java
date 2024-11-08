package aed;

import java.util.ArrayList;

import aed.Traslado.GananciasComparator;
import aed.Traslado.TimeComparator;

public class BestEffort {
    private Heap<Dupla> trasladosTiempo;
    private Heap<Dupla> trasladosGanancia;
    private Estadistica despachados;
    private TimeComparator comparadorTiempo;
    private GananciasComparator comparadorGanancia;


    private class Dupla {
        Traslado traslado;
        int handle;

        Dupla(Traslado t, int h) {
            traslado = t;
            handle = h;
            }

        public void CambiarHandle(int h){
            handle = h;
        }
    
}

    public BestEffort(int cantCiudades, Traslado[] traslados){
        trasladosTiempo = new Heap(comparadorTiempo);
        trasladosGanancia = new Heap(comparadorGanancia);
        ArrayList<Dupla> listaDuplasTiempo = new ArrayList<Dupla>();
        ArrayList<Dupla> listaDuplasGanancias = new ArrayList<Dupla>();

        for (int i = 0; i < traslados.length; i++ ){ //creo las duplas que guardan cada traslado y agrego cada una a una lista
            Traslado t = traslados[i];
            Dupla duplaTiempo = new Dupla(t,i); 
            listaDuplasTiempo.add(duplaTiempo);

            Dupla duplaGanancias = new Dupla(t,i); 
            listaDuplasGanancias.add(duplaGanancias);
        }

        trasladosTiempo = trasladosTiempo.conjuntoAHeap(listaDuplasTiempo);
        trasladosGanancia = trasladosGanancia.conjuntoAHeap((listaDuplasGanancias));

        for (int j = 0; j < traslados.length; j++){
            Dupla duplaTiempo = trasladosTiempo.obtenerElemento(j);
            int posicionEnLista = duplaTiempo.handle;
            Dupla duplaGanancias = listaDuplasGanancias.get(posicionEnLista);
            duplaGanancias.CambiarHandle(j);
        }

        for (int k = 0; k < traslados.length; k++){
            Dupla duplaGanancias = trasladosGanancia.obtenerElemento(k);
            int posicionEnHeapTiempo = duplaGanancias.handle;
            Dupla duplaTiempo = trasladosTiempo.obtenerElemento(posicionEnHeapTiempo);
            duplaTiempo.CambiarHandle(k);
        }

        despachados = new Estadistica(cantCiudades);
    
        
    }

    public void registrarTraslados(Traslado[] traslados){
        for (int i = 0; i < traslados.length; i++){
            Dupla dupla = new Dupla(traslados[i],i);
            int indiceGanancia = trasladosGanancia.agregar(dupla);
            int indiceTiempo = trasladosTiempo.agregar(dupla);

            int t = trasladosTiempo.tamaño()-1; 
            while (t > indiceTiempo){            
                int g = trasladosTiempo.obtenerElemento(t).handle; 
                Dupla duplaGanancia = trasladosGanancia.obtenerElemento(g);
                duplaGanancia.CambiarHandle(t);
                t = (t-1) / 2;  
            }  

            int g = trasladosGanancia.tamaño()-1; 
            while (g > indiceGanancia){            
                t = trasladosGanancia.obtenerElemento(g).handle; 
                Dupla duplaTiempo = trasladosTiempo.obtenerElemento(t);
                duplaTiempo.CambiarHandle(g);
                g = (g-1) / 2;  
            }  
        }
    }

    public int[] despacharMasRedituables(int n){
        int[] lista_ids = new int[n];

        for (int i = 0; i < n; i++){
            Dupla maximo = trasladosGanancia.sacarMaximo();
            int id = maximo.traslado.id;
            int handle = maximo.handle;
            lista_ids[i] = id;
            trasladosTiempo.eliminar(handle);

        }
        return lista_ids;
    }

    public int[] despacharMasAntiguos(int n){
        // Implementar
        return null;
    }

    public int ciudadConMayorSuperavit(){
        // Implementar
        return 0;
    }

    public ArrayList<Integer> ciudadesConMayorGanancia(){
        return null;
    }

    public ArrayList<Integer> ciudadesConMayorPerdida(){
        // Implementar
        return null;
    }

    public int gananciaPromedioPorTraslado(){
        // Implementar
        return 0;
    }
    
}
