package aed;

import java.util.ArrayList;
import java.util.Comparator;

// implementamos el Heap sobre un array

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
        if (elems.size() == tamaño){
            elems.add(e);
        }
        else{
            elems.set(tamaño,e);
        }
        
        int indice = siftUp(tamaño);
        ultimo = elems.get(tamaño);
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
        T maximo = null;
        if (tamaño > 0){
            maximo = elems.get(0);
            elems.set(0, ultimo); //fijarse lo del aliasing por el elems.set
            elems.remove(tamaño - 1);
            tamaño --;
        }
        if (tamaño > 0){
            siftDown(0); 
            ultimo = elems.get(tamaño-1);
        }
        else{
            ultimo = null;
        }
        return maximo;    
    }

    private int siftDown(int indice) { //recibo el indice del elemento que voy a acomodar hacia abajo
        int indiceIzquierdo, indiceDerecho, mayorIndice; //incializo mis variables
        mayorIndice = indice; 

            while (indice < tamaño) { //la guarda tiene complejidad O(1) y hay log n  iteraciones porque sube de nivel a través de los padres
                indiceIzquierdo = 2 * indice + 1; 
                indiceDerecho = 2 * indice + 2; 
    
                // Comparo con el hijo izquierdo
                if (indiceIzquierdo < tamaño && comparator.compare(elems.get(indiceIzquierdo), elems.get(mayorIndice)) > 0) {
                    mayorIndice = indiceIzquierdo; //si el hijo izquierdo es mayor me guardo su indice como el indice de mi mayor elemento
                }

                // Comparo con el hijo derecho
                if (indiceDerecho < tamaño && comparator.compare(elems.get(indiceDerecho), elems.get(mayorIndice)) > 0) {
                    mayorIndice = indiceDerecho; //si el hijo derecho es mayor me guardo su indice como el indice de mi mayor elemento
                }

                //aclaración: no comparamos entre hijo izquierdo y derecho porque las colas de prioridad solo requieren que el padre tenga mayor prioridad que sus hijos
                
                // Si el mayor es el mismo índice, el elemento está en su lugar correcto
                if (mayorIndice == indice) {
                    break;
                }
                //todos los if tienen complejidad O(1)

                // Intercambio los elementos con el mismo proceso de "swap" que use en siftUp
                T temp = elems.get(indice);
                elems.set(indice, elems.get(mayorIndice));
                elems.set(mayorIndice, temp);
                //tanto get como set tienen complejidad O(1)

                // Continuar desde la posición del mayor
                indice = mayorIndice;
            }
            //la complejidad del while es O(1) + (log n)*O(1) = O(log n) donde n es la cantidad de elementos del heap

            return indice; //siftDown devuelve el indice final del elemento que baje
    } //si sumamos las complejidades de las operaciones realizadas en siftDown, podemos concluir que la complejidad de siftDown es O(log n)

    public Heap<T> conjuntoAHeap(ArrayList<T> s){
        this.elems = new ArrayList<>(s); //copio los elementos del conjunto que me dan como entrada, O(1)
        this.tamaño = elems.size(); // O(1) *** PREGUNTAR SI SE PUEDE USAR ELEMS.SIZE()
        int indice = (tamaño - 2)/2; //tomo el indice del padre del ultimo elemento, O(1)
        while (indice >= 0){ //me aseguro de no irme de rango, este ciclo itera sobre todos los elementos del heap excluyendo sus hojas
            siftDown(indice); //acomodo cada elemento, tiene complejidad O(log n)
            indice --; //O(1)
        } // la complejidad de este while es O(n*log n), 
        this.ultimo = elems.get(tamaño-1); //O(1)
        return this;
    }   //la complejidad de conjuntoAHeap es O(n*log n), donde n es la cantidad de elementos del heap

    public int eliminar(int indice){
        int indiceFinal = -1;
        if (tamaño == 1){ //caso tamanio = 1
            tamaño = 0;
            elems.set(0,null);
            ultimo = null;
        }
        else if (indice == tamaño - 1){ //caso en que borro el ultimo elemento
            elems.set(tamaño-1,null);
            tamaño --;
            ultimo = elems.get(tamaño-1);
        }
        else if (tamaño > 1 && indice < tamaño){
            tamaño --;
            elems.set(indice, ultimo);
            elems.set(tamaño,null);
            indiceFinal = siftDown(indice);
            ultimo = elems.get(tamaño-1);

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

