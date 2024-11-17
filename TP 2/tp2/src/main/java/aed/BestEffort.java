package aed;

import java.util.ArrayList;
import java.util.Comparator;

//Las operaciones elementales, las operaciones sobre arrayList (constructor, add, set, get, clear, size) y
//los comparadores que creamos son O(1) así que, para facilitar la lectura, no aclararemos esto en cada aparición.

// llamamos C al conjunto de ciudades y T al conjunto de traslados

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
    // Construyo los comparadores que van a definir el orden de prioridad de mis heaps, O(1)
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
        TimeComparator comparadorTiempo = new TimeComparator(); 
        GananciasComparator comparadorGanancia = new GananciasComparator();
        ArrayList<Dupla> listaDuplasTiempo = new ArrayList<Dupla>(); //inicializo listas donde haré una copia de los traslados pasados por parámetro
        ArrayList<Dupla> listaDuplasGanancias = new ArrayList<Dupla>(); // O(1)

        for (int i = 0; i < traslados.length; i++ ){ //creo las duplas que guardan cada traslado con la posición en la que ingresan, y agrego cada una de ellas a mis listas
            Traslado t = traslados[i]; 
            Dupla duplaTiempo = new Dupla(t,i); 
            listaDuplasTiempo.add(duplaTiempo); 
            
            Dupla duplaGanancias = new Dupla(t,i); 
            listaDuplasGanancias.add(duplaGanancias);
        } //La complejidad del for es O(|T|) porque itera |T| veces y las operaciones en el interior del for tienen complejidad total O(1)

        trasladosTiempo = new Heap<>(comparadorTiempo).conjuntoAHeap(listaDuplasTiempo); //Creo los heaps a partir de las listas que armé previamente, cada uno con su comparator correspondiente
        trasladosGanancia = new Heap<>(comparadorGanancia).conjuntoAHeap(listaDuplasGanancias); //La complejidad de conjuntoAHeap es O(|T|)

        for (int j = 0; j < traslados.length; j++){ //En este for asigno correctamente el handle a los traslados en mi heap de Ganancias
            Dupla duplaTiempo = trasladosTiempo.obtenerElemento(j); //Obtener elemento tiene complejidad O(1)
            int posicionEnLista = duplaTiempo.handle; //El handle que tenia guardado en el heap de Tiempo es la posicion que tiene el elemento en la lista de duplas
            Dupla duplaGanancias = listaDuplasGanancias.get(posicionEnLista); // Tomo la dupla que contiene el traslado al que quiero modificar el handle de listaDuplasGanacia
            duplaGanancias.CambiarHandle(j); // j es la posicion de mi traslado en el heap de Tiempo, es decir, el handle de mi traslado en el heap de Ganancia, cambiarHandle tiene complejidad O(1)
        } 

        for (int k = 0; k < traslados.length; k++){ //En este for asigno correctamente el handle a los traslados en mi heap de Tiempo, misma lógica que el for anterior
            Dupla duplaGanancias = trasladosGanancia.obtenerElemento(k);
            int posicionEnHeapTiempo = duplaGanancias.handle;
            Dupla duplaTiempo = trasladosTiempo.obtenerElemento(posicionEnHeapTiempo); //En este caso obtengo la dupla que quiero cambiar a partir del heap Tiempo, ya no uso la lista que creé
            duplaTiempo.CambiarHandle(k);
        } 
        //ambos for tienen complejidad O(|T|) ya que itera |T| veces
        despachados = new Estadistica(cantCiudades); // O(|C|)
    
        
    } //La complejidad de BestEffort es O(|C|+|T|)

    public void registrarTraslados(Traslado[] traslados){ // para cada traslado que me pasan armo dos duplas, una para cada heap
        for (int i = 0; i < traslados.length; i++){
            Dupla nuevaDuplaTiempo = new Dupla(traslados[i],i);
            Dupla nuevaDuplaGanancia = new Dupla(traslados[i],i);
            
            int indiceTiempo = trasladosTiempo.agregar(nuevaDuplaTiempo); // primero lo agrego al heapTiempo y me guardo su posicion

            nuevaDuplaGanancia.CambiarHandle(indiceTiempo); // esa posicion guardada pasa a ser el handle del traslado en el heapGanancias

            int t = trasladosTiempo.tamaño()-1; 
            while (t > indiceTiempo){ //itero desde el ultimo elemento del heapTiempos para corregir los handle en heapGanancias en la rama de elementos que se reacomodaron           
                int g = trasladosTiempo.obtenerElemento(t).handle; 
                Dupla duplaGanancia = trasladosGanancia.obtenerElemento(g);
                duplaGanancia.CambiarHandle(t);
                if (t-1<0) { //evito un bucle infinito
                    break;
                }
                t = (t-1) / 2;  //subo al padre
            }  //O(log |T|) porque itero a lo sumo la altura del heap, es decir, log |T|

            int indiceGanancia = trasladosGanancia.agregar(nuevaDuplaGanancia); // repito el procedimiento con el otro heap
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
            }  //O(log |T|) 
        } // el for itera |traslados| veces, es decir, la longitud de la secuencia pasada por parametro
    } //O(|traslados|*log |T|) 

    public int[] despacharMasRedituables(int n){ 
        if (n >= trasladosGanancia.tamaño()){ // me aseguro de que no me pidan despachar mas traslados de los que tengo
            n = trasladosGanancia.tamaño();
        }

        int[] lista_ids = new int[n]; // armo una lista para ir registrando los id de traslados despachados, O(n), donde n es la cantidad de despachos solicitados por parametro

        if (trasladosGanancia.tamaño()==1) { // caso en que tengo un solo traslado para despachar
            Dupla maximo = trasladosGanancia.maximo(); //O(1)
            int id = maximo.traslado.id;
            lista_ids[0]=id;
            trasladosGanancia.eliminar(0); //O(1) porque tengo un solo traslado para despachar
            trasladosTiempo.eliminar(0); //O(1) porque tengo un solo traslado para despachar 
            despachados.agregarDespachado(maximo.traslado); //O(log |C|)
        } //O(log |C|)

        for (int i = 0; i < n; i++){ // itera n veces
            Dupla maximo = trasladosGanancia.maximo();
            int id = maximo.traslado.id;
            int handle = maximo.handle;
            lista_ids[i] = id;

            int g = trasladosGanancia.eliminar(0); // O(log |T|)
            if (trasladosGanancia.tamaño() > 0){
                while (g >= 0){                 // corrijo los handle comenzando por la nueva posición del que antes era el último traslado y subiendo por esa rama hasta la raiz para modificar todos los handles que pudieron haber cambiado
                    int k = trasladosGanancia.obtenerElemento(g).handle; 
                    Dupla duplaTiempo = trasladosTiempo.obtenerElemento(k);
                    duplaTiempo.CambiarHandle(g);
                    if (g-1<0) { //evito bucle infinito
                        break;
                    }
                    g = (g-1) / 2;  //subo al padre
                } // O(log |T|), porque itero la altura del heap (log |T|)
            } 

            int t = trasladosTiempo.eliminar(handle); // O(log |T|)

            if (trasladosTiempo.tamaño() > 0){
                while (t >= 0){            // corrijo los handle en el otro heap
                    int m = trasladosTiempo.obtenerElemento(t).handle; 
                    Dupla duplaGanancia = trasladosGanancia.obtenerElemento(m);
                    duplaGanancia.CambiarHandle(t);
                    if (t-1<0) {
                        break;
                    }
                    t = (t-1) / 2;  
                } // O(log |T|)
            }  
            
            despachados.agregarDespachado(maximo.traslado); // O(log |C|)
        }  // O(n*(log|T|+log|C|)) porque en el for estoy iterando n veces este proceso de eliminar traslados, cambiar handles y agregar despachados

        return lista_ids;
    } // O(n*(log|T|+log|C|)) 

    public int[] despacharMasAntiguos(int n){ //realizo el procedimiento con la misma logica que despacharMasRedituable
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
                    if (t-1<0) {
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
                    if (g-1<0) { 
                        break;
                    }
                    g = (g-1) / 2;  
                }
            }  

            despachados.agregarDespachado(maximo.traslado);
        }
        }

        return lista_ids;
    }  // O(n*(log|T|+log|C|)) 

    public int ciudadConMayorSuperavit(){
        return despachados.ciudadConMayorSuperavit();
    } //O(1)

    public ArrayList<Integer> ciudadesConMayorGanancia(){
        return despachados.ciudadesConMayorGanancia();
    } //O(1)

    public ArrayList<Integer> ciudadesConMayorPerdida(){
        return despachados.ciudadesConMayorPerdida();
    } //O(1)

    public int gananciaPromedioPorTraslado(){
        return despachados.gananciaPromedioPorTraslado();
    } //O(1)
    
}

