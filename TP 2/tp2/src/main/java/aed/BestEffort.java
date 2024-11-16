package aed;

import java.util.ArrayList;
import java.util.Comparator;

public class BestEffort {
    private Heap<Dupla> trasladosTiempo;
    private Heap<Dupla> trasladosGanancia;
    private Estadistica despachados;


    private class Dupla { //Creamos la clase Dupla para relacionar ambos heaps a través de un handle, O(1)
        Traslado traslado;
        int handle;

        Dupla(Traslado t, int h) {
            traslado = t;
            handle = h;
            }

        public void CambiarHandle(int h){ //Este método nos permite corregir los handle cada vez que se modifica el orden de un heap, O(1)
            handle = h;
        }
    }
    // Construyo los comparadores que van a definir el orden de prioridad de mis heaps, O(1)?***
    public class GananciasComparator implements Comparator <Dupla>{ 
        @Override
        public int compare(Dupla d1, Dupla d2){
            if (Integer.compare(d1.traslado.gananciaNeta, d2.traslado.gananciaNeta) == 0){ //Si los traslados que comparo tiene la misma ganacia, entonces comparo sus id
                return Integer.compare(d1.traslado.id, d2.traslado.id);
            } else{
            return Integer.compare(d1.traslado.gananciaNeta, d2.traslado.gananciaNeta);
            }
            
        }
    }

    public class TimeComparator implements Comparator <Dupla>{
        @Override
        public int compare(Dupla d1, Dupla d2){
            return Integer.compare(d2.traslado.timestamp, d1.traslado.timestamp);
        }
    }

    public BestEffort(int cantCiudades, Traslado[] traslados){
        TimeComparator comparadorTiempo = new TimeComparator(); // Inicializamos los atributos de la clase *** ESTOS NO SE SI SON ATRIBUTOS(?
        GananciasComparator comparadorGanancia = new GananciasComparator();
        ArrayList<Dupla> listaDuplasTiempo = new ArrayList<Dupla>(); //inicializo listas donde haré una copia de los traslados pasados por parámetro
        ArrayList<Dupla> listaDuplasGanancias = new ArrayList<Dupla>();

        for (int i = 0; i < traslados.length; i++ ){ //creo las duplas que guardan cada traslado con la posición en la que ingresan, y agrego cada una de ellas a mis listas
            Traslado t = traslados[i]; //O(1)
            Dupla duplaTiempo = new Dupla(t,i); //O(1) 
            listaDuplasTiempo.add(duplaTiempo); //O(1)
            
            Dupla duplaGanancias = new Dupla(t,i); 
            listaDuplasGanancias.add(duplaGanancias);
        } //La complejidad del for es O(|T|), donde T es igual a la cantidad de traslados pasados por parámetro

        trasladosTiempo = new Heap<>(comparadorTiempo).conjuntoAHeap(listaDuplasTiempo); //Creo los heaps a partir de las listas que armé previamente, cada uno con su comparator correspondiente
        trasladosGanancia = new Heap<>(comparadorGanancia).conjuntoAHeap(listaDuplasGanancias); //La complejidad de conjuntoAHeap es O(n*log n), en este caso: O(|T|*log |T|)

        for (int j = 0; j < traslados.length; j++){ //En este for asigno correctamente el handle a los traslados en mi heap de Ganancias
            Dupla duplaTiempo = trasladosTiempo.obtenerElemento(j); //Obtener elemento tiene complehidad O(1)
            int posicionEnLista = duplaTiempo.handle; //El handle que tenia guardado en el heap de Tiempo es la posicion que tiene el elemento en la lista pasada por parámetro
            Dupla duplaGanancias = listaDuplasGanancias.get(posicionEnLista); // Tomo la dupla que contiene el traslado al que quiero modificar el handle a partir de la copia que hice de la lista pasada por parámetro
            duplaGanancias.CambiarHandle(j); // j es la posicion de mi traslado en el heap de Tiempo, es decir, el handle de mi traslado en el heap de Ganancia
        }

        for (int k = 0; k < traslados.length; k++){ //En este for asigno correctamente el handle a los traslados en mi heap de Tiempo, misma lógica que el for anterior
            Dupla duplaGanancias = trasladosGanancia.obtenerElemento(k);
            int posicionEnHeapTiempo = duplaGanancias.handle;
            Dupla duplaTiempo = trasladosTiempo.obtenerElemento(posicionEnHeapTiempo); //En este caso obtengo la dupla que quiero cambiar a partir del heap Tiempo, ya no uso la lista que creé
            duplaTiempo.CambiarHandle(k);
        }

        despachados = new Estadistica(cantCiudades); // inicializo mi atributo de clase
    
        
    }

    public void registrarTraslados(Traslado[] traslados){
        for (int i = 0; i < traslados.length; i++){
            Dupla nuevaDuplaTiempo = new Dupla(traslados[i],i);
            Dupla nuevaDuplaGanancia = new Dupla(traslados[i],i);
            int indiceTiempo = trasladosTiempo.agregar(nuevaDuplaTiempo);

            nuevaDuplaGanancia.CambiarHandle(indiceTiempo);

            int t = trasladosTiempo.tamaño()-1; 
            while (t > indiceTiempo){            
                int g = trasladosTiempo.obtenerElemento(t).handle; 
                Dupla duplaGanancia = trasladosGanancia.obtenerElemento(g);
                duplaGanancia.CambiarHandle(t);

                if (t-1<0) {
                    break;
                }

                t = (t-1) / 2;  
            }  

            int indiceGanancia = trasladosGanancia.agregar(nuevaDuplaGanancia);
            nuevaDuplaTiempo.CambiarHandle(indiceGanancia);

            int g = trasladosGanancia.tamaño()-1; 
            while (g > indiceGanancia){            
                t = trasladosGanancia.obtenerElemento(g).handle; 
                Dupla duplaTiempo = trasladosTiempo.obtenerElemento(t);
                duplaTiempo.CambiarHandle(g);

                if (g-1<0) {
                    break;
                }
                g = (g-1) / 2;  
            }  
        }
    }

    public int[] despacharMasRedituables(int n){
        if (n >= trasladosGanancia.tamaño()){
            n = trasladosGanancia.tamaño();
        }

        int[] lista_ids = new int[n];

        if (trasladosGanancia.tamaño()==1) {
            Dupla maximo = trasladosGanancia.maximo();
            int id = maximo.traslado.id;
            lista_ids[0]=id;
            trasladosGanancia.eliminar(0);
            trasladosTiempo.eliminar(0);
            despachados.agregarDespachado(maximo.traslado);
        }

        for (int i = 0; i < n; i++){
            Dupla maximo = trasladosGanancia.maximo();
            int id = maximo.traslado.id;
            int handle = maximo.handle;
            lista_ids[i] = id;

            int g = trasladosGanancia.eliminar(0);
            if (trasladosGanancia.tamaño() > 0){
                while (g >= 0){            
                    int k = trasladosGanancia.obtenerElemento(g).handle; 
                    Dupla duplaTiempo = trasladosTiempo.obtenerElemento(k);
                    duplaTiempo.CambiarHandle(g);
                    if (g-1<0) {
                        break;
                    }
                    g = (g-1) / 2;  
                }
            }  

            int t = trasladosTiempo.eliminar(handle);

            if (trasladosTiempo.tamaño() > 0){
                while (t >= 0){            
                    int m = trasladosTiempo.obtenerElemento(t).handle; 
                    Dupla duplaGanancia = trasladosGanancia.obtenerElemento(m);
                    duplaGanancia.CambiarHandle(t);
                    if (t-1<0) {
                        break;
                    }
                    t = (t-1) / 2;  
                }
            }  

            despachados.agregarDespachado(maximo.traslado);
        }

        return lista_ids;
    }

    public int[] despacharMasAntiguos(int n){
        if (n >= trasladosGanancia.tamaño()){
            n = trasladosGanancia.tamaño();
        }
        int[] lista_ids = new int[n];

        if (trasladosTiempo.tamaño()==1) {
            Dupla maximo = trasladosTiempo.maximo();
            int id = maximo.traslado.id;
            lista_ids[0]=id;
            trasladosGanancia.eliminar(0);
            trasladosTiempo.eliminar(0);
            despachados.agregarDespachado(maximo.traslado);
        } else {
        
        for (int i = 0; i < n; i++){
            Dupla maximo = trasladosTiempo.maximo();
            int id = maximo.traslado.id;
            int handle = maximo.handle;
            lista_ids[i] = id;

            int t = trasladosTiempo.eliminar(0);
            
            if (trasladosTiempo.tamaño() > 0){
                while (t >= 0){            
                    int k = trasladosTiempo.obtenerElemento(t).handle; 
                    Dupla duplaGanancia = trasladosGanancia.obtenerElemento(k);
                    duplaGanancia.CambiarHandle(t);
                    if (t-1<0) { //cuando llegaba a la raiz entraba en un bucle infinito, asi si me paso salgo del while
                        break;
                    }
                    t = (t-1) / 2;  
                } 
            }

            int g = trasladosGanancia.eliminar(handle);

            if (trasladosGanancia.tamaño() > 0){
                while (g >= 0){            
                    int m = trasladosGanancia.obtenerElemento(g).handle; 
                    Dupla duplaTiempo = trasladosTiempo.obtenerElemento(m);
                    duplaTiempo.CambiarHandle(g);
                    if (g-1<0) { //cuando llegaba a la raiz entraba en un bucle infinito, asi si me paso salgo del while
                        break;
                    }
                    g = (g-1) / 2;  
                }
            }  

            despachados.agregarDespachado(maximo.traslado);
        }
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

