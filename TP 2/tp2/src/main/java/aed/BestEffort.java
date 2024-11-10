package aed;

import java.util.ArrayList;
import java.util.Comparator;

public class BestEffort {
    private Heap<Dupla> trasladosTiempo;
    private Heap<Dupla> trasladosGanancia;
    private Estadistica despachados;


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

    public class GananciasComparator implements Comparator <Dupla>{
        @Override
        public int compare(Dupla d1, Dupla d2){
            return Integer.compare(d1.traslado.gananciaNeta, d2.traslado.gananciaNeta);
        }
    }

    public class TimeComparator implements Comparator <Dupla>{
        @Override
        public int compare(Dupla d1, Dupla d2){
            return Integer.compare(d1.traslado.timestamp, d2.traslado.timestamp);
        }
    }

    public BestEffort(int cantCiudades, Traslado[] traslados){
        TimeComparator comparadorTiempo = new TimeComparator();
        GananciasComparator comparadorGanancia = new GananciasComparator();
        trasladosTiempo = new Heap<>(comparadorTiempo);
        trasladosGanancia = new Heap<>(comparadorGanancia);
        ArrayList<Dupla> listaDuplasTiempo = new ArrayList<Dupla>();
        ArrayList<Dupla> listaDuplasGanancias = new ArrayList<Dupla>();

        for (int i = 0; i < traslados.length; i++ ){ //creo las duplas que guardan cada traslado y agrego cada una a una lista
            Traslado t = traslados[i];
            Dupla duplaTiempo = new Dupla(t,i); 
            listaDuplasTiempo.add(duplaTiempo);

            Dupla duplaGanancias = new Dupla(t,i); 
            listaDuplasGanancias.add(duplaGanancias);
        }

        trasladosTiempo = new Heap<>(comparadorTiempo).conjuntoAHeap(listaDuplasTiempo);
        trasladosGanancia = new Heap<>(comparadorGanancia).conjuntoAHeap(listaDuplasGanancias);

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
            Dupla maximo = trasladosGanancia.maximo();
            int id = maximo.traslado.id;
            int handle = maximo.handle;
            lista_ids[i] = id;

            int g = trasladosGanancia.eliminar(0);

            while (g >= 0){            
                int k = trasladosGanancia.obtenerElemento(g).handle; 
                Dupla duplaTiempo = trasladosTiempo.obtenerElemento(k);
                duplaTiempo.CambiarHandle(g);
                if (g-1<0) {
                    break;
                }
                g = (g-1) / 2;  
            }  

            int t = trasladosTiempo.eliminar(handle);

            while (t >= handle){            
                int m = trasladosTiempo.obtenerElemento(t).handle; 
                Dupla duplaGanancia = trasladosGanancia.obtenerElemento(m);
                duplaGanancia.CambiarHandle(t);
                t = (t-1) / 2;  
            }  

            despachados.agregarDespachado(maximo.traslado);
        }

        return lista_ids;
    }

    public int[] despacharMasAntiguos(int n){
        int[] lista_ids = new int[n];

        for (int i = 0; i < n; i++){
            Dupla maximo = trasladosTiempo.maximo();
            int id = maximo.traslado.id;
            int handle = maximo.handle;
            lista_ids[i] = id;

            int t = trasladosTiempo.eliminar(0);
            

            while (t >= 0){            
                int k = trasladosTiempo.obtenerElemento(t).handle; 
                Dupla duplaGanancia = trasladosGanancia.obtenerElemento(k);
                duplaGanancia.CambiarHandle(t);
                if (t-1<0) { //cuando llegaba a la raiz entraba en un bucle infinito, asi si me paso salgo del while
                    break;
                }
                t = (t-1) / 2;  
            } 

            int g = trasladosGanancia.eliminar(handle);


            while (g >= handle){            
                int m = trasladosGanancia.obtenerElemento(g).handle; 
                Dupla duplaTiempo = trasladosTiempo.obtenerElemento(m);
                duplaTiempo.CambiarHandle(g);
                g = (g-1) / 2;  
            }  

            despachados.agregarDespachado(maximo.traslado);
        }

        return lista_ids;
    }

    public int ciudadConMayorSuperavit(){
        return despachados.ciudadConMayorSuperavit();
    }

    public ArrayList<Integer> ciudadesConMayorGanancia(){
        return despachados.ciudadesConMayorGanancia();
    }

    public ArrayList<Integer> ciudadesConMayorPerdida(){
        return despachados.ciudadesConMayorPerdida();
    }

    public int gananciaPromedioPorTraslado(){
        return despachados.gananciaPromedioPorTraslado();
    }
    
}
