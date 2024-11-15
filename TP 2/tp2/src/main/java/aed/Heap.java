package aed;

import java.util.ArrayList;
import java.util.Comparator;

public class Heap<T> {
    private ArrayList<T> elems;
    private Comparator<T> comparator;
    private T ultimo;
    private int tamaño; 

    public Heap(Comparator<T> comparator){
        elems = new ArrayList<T>();
        this.comparator = comparator;
        tamaño = 0;
        ultimo = null;
    }

    public int tamaño(){
        return tamaño;
    }

    public int agregar(T e){
        if (elems.size() == tamaño){ //si no tengo ningún espacio de memoria vacío (null) en el heap, entonces agrego el elemento al final creando su espacio
            elems.add(e);
        }
        else{                        // caso contrario, ubico el nuevo elemento en el primero espacio "null" de mi heap
            elems.set(tamaño,e);
        }
        
        int indice = siftUp(tamaño); //siftUp me devuelve el indice en donde terminó el elemento que agregué al heap luego de acomodarlo
        ultimo = elems.get(tamaño); // modifico el atributo "último" de mi heap para actualizar correctamente mis datos
        tamaño ++; // modifico el atributo tamaño de mi heap para actualizar correctamente mis datos
        return indice; 
    }

    private int siftUp(int indice) { //recibo el indice del elemento que quiero acomodar "hacia arriba" 
        while (indice > 0) { //me aseguro de que mi elemento tenga un padre
            int indicePadre = (indice - 1) / 2; 

            if(comparator.compare(elems.get(indice), elems.get(indicePadre)) <= 0) { //si el padre de mi elemento tiene mayor prioridad, dejo de subir
                break;
            }
            
            T elementoActual = elems.get(indice); // para evitar aliasing, me guardo el elemento actual
            elems.set(indice, elems.get(indicePadre)); //"swapeo" el elemento con su padre en estas dos líneas
            elems.set(indicePadre, elementoActual);
            indice = indicePadre; //ahora cambio el indice para seguir subiendo a los padres necesarios
        } 
        return indice;  // armamos SiftUp para no usar indexOf, pues nos aumenta la complejidad a O(n), siftUp devuelve el indice final del elemento que subí
    } 

    public T maximo(){
        return elems.get(0);
    }

    public T sacarMaximo(){
        T maximo = null;
        if (tamaño > 0){ //me aseguro de no tener un heap vacío
            maximo = elems.get(0); 
            elems.set(0, ultimo); //subo el último elemento de mi heap a la raíz
            elems.remove(tamaño - 1); // ya que subí el último elemento a la raíz, lo elimino de la última posición (donde originalmente estaba)
            tamaño --;
        }
        if (tamaño > 0){ //comparo nuevamente porque ahora al tamaño le resté una unidad
            siftDown(0); 
            ultimo = elems.get(tamaño-1);
        }
        else{
            ultimo = null;
        }
        return maximo;    
    }

    private int siftDown(int indice) {
        int indiceIzquierdo, indiceDerecho, mayorIndice;
        mayorIndice = indice; 

            while (indice < tamaño) {
                indiceIzquierdo = 2 * indice + 1; 
                indiceDerecho = 2 * indice + 2; 
    
                // Comparar con el hijo izquierdo
                if (indiceIzquierdo < tamaño && comparator.compare(elems.get(indiceIzquierdo), elems.get(mayorIndice)) > 0) {
                    mayorIndice = indiceIzquierdo;
                }

                // Comparar con el hijo derecho
                if (indiceDerecho < tamaño && comparator.compare(elems.get(indiceDerecho), elems.get(mayorIndice)) > 0) {
                    mayorIndice = indiceDerecho;
                }

                // Si el mayor es el mismo índice, el elemento está en su lugar correcto
                if (mayorIndice == indice) {
                    break;
                }

                // Intercambiar elementos
                T temp = elems.get(indice);
                elems.set(indice, elems.get(mayorIndice));
                elems.set(mayorIndice, temp);

                // Continuar desde la posición del mayor
                indice = mayorIndice;
            }

            return indice;
    }

    public Heap<T> conjuntoAHeap(ArrayList<T> s){
        this.elems = new ArrayList<>(s);
        this.tamaño = elems.size();
        int indice = (tamaño - 2)/2; //tomo el indice del padre del ultimo elemento
        while (indice >= 0){
            siftDown(indice);
            indice --;
        }
        this.ultimo = elems.get(tamaño-1);
        return this;
    }   

    public int eliminar(int indice){
        int indiceFinal = -1;
        if (tamaño > 1 && indice < tamaño){
            tamaño --;
            elems.set(indice, ultimo);
            elems.set(tamaño,null);
            
            if (indice == tamaño) {
                indiceFinal = tamaño - 1;
            } else {
                indiceFinal = siftDown(indice);
            }
            ultimo = elems.get(tamaño-1);
        } else {
            tamaño--;
            elems.set(indice, ultimo);
            elems.set(tamaño,null);
            indiceFinal=0;
            ultimo = null;
        }
        return indiceFinal; //devuelve el indice donde queda el que antes era el ultimo elemento en el heap (excepto que eliminies el ultimo)
    }

    public int revisar(int indice){
        int res = indice;
        T elemento = elems.get(indice);

        if (indice > 0){
            T padre = elems.get((indice-1)/2);
            if (comparator.compare(elemento, padre) > 0){
                res = siftUp(indice); 
            }
        }
        else if ((2*indice + 1) < tamaño){
            T hijoIzq = elems.get(2*indice + 1);
            if (hijoIzq != null && comparator.compare(elemento, hijoIzq) < 0){
                res = siftDown(indice);
            }
        }
        else if ((2*indice + 2) < tamaño){
            T hijoDer = elems.get(2*indice + 2);
            if (hijoDer != null && comparator.compare(elemento, hijoDer) < 0){
                res = siftDown(indice);
            }
        }
        ultimo = elems.get(tamaño-1);
        return res;
    }

    public T obtenerElemento(int indice){
        T res = null;
        if (indice < tamaño){
            res = elems.get(indice);
        }
        return res;
    }

    public T obtenerPadre(int indice){
        return elems.get((indice-1)/2);
    }

    public T obtenerHijoIzq(int indice){
        return elems.get(2*indice+1);
    }

    public T obtenerHijoDer(int indice){
        return elems.get(2*indice+2);
    }

} 

