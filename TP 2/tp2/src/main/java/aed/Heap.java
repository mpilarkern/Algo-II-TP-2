package aed;

import java.util.ArrayList;
import java.util.Comparator;

//para tener a mano:
//hijo_izq(i) = 2*i + 1
//hijo_der(i) = 2*i + 2
//padre(i) = (i-1) / 2

public class Heap<T> {
    private ArrayList<T> elems;
    private Comparator<T> comparator;
    private T ultimo;
    private int tamaño; //la ñ nos hará problema?

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
        elems.add(e);
        int indice = siftUp(tamaño); //no hace falta usar indexOf pues el indice inicial de e es tamaño
        ultimo = elems.get(tamaño); //el ultimo elemento tiene indice "tamaño" pues aun no aumenté el tamaño
        tamaño ++;
        return indice;
    }

    private int siftUp(int indice) {
        while (indice > 0) {
            int indicePadre = (indice - 1) / 2;

            if(comparator.compare(elems.get(indice), elems.get(indicePadre)) <= 0) {
                break;
            }
            
            T elementoActual = elems.get(indice);
            elems.set(indice, elems.get(indicePadre));
            elems.set(indicePadre, elementoActual);
            indice = indicePadre;
        } return indice;  //cambiamos esto para no usar indexOf pues nos aumenta la complejidad a O(n), siftUp devuelve el indice final del elemento que subí
    } //fijarse que no haya aliasing (por el elems.set) y que funcione para los dos comparators (porque para ganancias g1 > g2 y para tiempos t1 < t2)

    public T maximo(){
        return elems.get(0);
    }

    public T sacarMaximo(){
        T maximo = elems.get(0);
        elems.set(0, elems.get(tamaño - 1)); //fijarse lo del aliasing por el elems.set
        elems.remove(tamaño - 1);
        this.siftDown(0); 
        ultimo = elems.get(tamaño-1);
        tamaño --;
        return maximo;        
    }

    private int siftDown(int indice) {
        if (indice < tamaño){
            while (true) {
                int indiceIzquierdo = 2 * indice + 1; 
                int indiceDerecho = 2 * indice + 2; 
                int mayorIndice = indice; 
    
                if (indiceIzquierdo < tamaño && comparator.compare(elems.get(indiceIzquierdo), elems.get(mayorIndice)) > 0) {
                    mayorIndice = indiceIzquierdo;
                }
    
                if (indiceDerecho < tamaño && comparator.compare(elems.get(indiceDerecho), elems.get(mayorIndice)) > 0) {
                    mayorIndice = indiceDerecho;
                }
    
                if (mayorIndice == indice) {
                    break;
                }
    
                T temp = elems.get(indice);
                elems.set(indice, elems.get(mayorIndice));
                elems.set(mayorIndice, temp);
    
                indice = mayorIndice;
            } 
        }
        return indice; //agrego return
    }

    public Heap<T> conjuntoAHeap(ArrayList s){
        this.elems = s;
        this.tamaño = elems.size();
        int indice = (tamaño - 1)/2; //tomo el indice del padre del ultimo elemento
        while (indice >= 0){
            siftDown(indice);
            indice --;
        }
        this.ultimo = elems.get(tamaño-1);
        return this;
    }    //preguntar si la complejidad está bien

    public int eliminar(int indice){
        tamaño --;
        elems.set(indice, ultimo);
        elems.set(tamaño,null);
        int indiceFinal = siftDown(indice);
        ultimo = elems.get(tamaño-1);
        return indiceFinal; //devuelve el indice donde queda el que antes era el ultimo elemento en el heap (excepto que eliminies el ultimo)
    }

    public int revisar(int indice){
        int res = indice;
        T elemento = elems.get(indice);
        T padre = elems.get((indice-1)/2);
        T hijoIzq = elems.get(2*indice + 1);
        T hijoDer = elems.get(2*indice + 2);
        if (comparator.compare(elemento, padre) > 0){
            res = siftUp(indice); 
        }
        else if ((hijoIzq != null && comparator.compare(elemento, hijoIzq) < 0) || (hijoDer != null && comparator.compare(elemento, hijoDer) < 0)){
            res = siftDown(indice);
        }
        ultimo = elems.get(tamaño-1);
        return res;
    }

    public T obtenerElemento(int indice){
        return elems.get(indice);
    }

    public T obtenerPadre(int indice){
        return elems.get((indice-1)/2);
    }

    public T obtenerHijoIzq(int indice){
        return elems.get((2*indice+1)/2);
    }

    public T obtenerHijoDer(int indice){
        return elems.get((2*indice+2)/2);
    }

} 
