package aed;

import java.util.ArrayList;
import java.util.Comparator;

// implementamos el Heap sobre un array

//Las operaciones elementales, las operaciones sobre arrayList (constructor, add, set, get, clear, size) y
//los comparadores que creamos son O(1) así que, para facilitar la lectura, no aclararemos esto en cada aparición.

//llamamos n a la cantidad de elementos en el Heap

//Las funciones que reciben por parámetro "indice", reciben la posición en el Heap del elemento (el indice en el ArrayList elems)

public class Heap<T> {
    private ArrayList<T> elems; 
    private Comparator<T> comparator; 
    private T ultimo;
    private int tamaño; 

    public Heap(Comparator<T> comparator){ //Implementamos el constructor de la clase
        elems = new ArrayList<T>();
        this.comparator = comparator;
        tamaño = 0;
        ultimo = null;
    } //O(1)

    public int tamaño(){
        return tamaño;
    } //O(1)

    public int agregar(T e){
        if (elems.size() == tamaño){  //si no tengo ningún espacio de memoria vacío (null) en el heap, entonces agrego el elemento al final creando su espacio
            elems.add(e); 
        }
        else{ // caso contrario, ubico el nuevo elemento en el primero espacio "null" de mi heap
            elems.set(tamaño,e);
        }
        // la complejidad de este if es (O(1) + máx{O(1),O(1)})= O(1)
        int indice = siftUp(tamaño); //siftUp me devuelve el indice en donde terminó el elemento que agregué al heap luego de acomodarlo, su complejidad es O(log n)
        ultimo = elems.get(tamaño);  // modifico el atributo "último" de mi heap para actualizar correctamente mis datos
        tamaño ++; // modifico el atributo tamaño de mi heap para actualizar correctamente mis datos
        return indice; // devuelvo el índice en donde está el elemento que agregué al heap
    } // la complejidad de agregar es O(1) + O(log n) = O(log n)

    public T maximo(){
        T maximo = null; 
        if (tamaño > 0 ){ //solo quiero devolver el primer elemento cuando el Heap no es vacío
           maximo = elems.get(0);
        } 
        return maximo;
    } //O(1)

    public T sacarMaximo(){
        T maximo = null;
        if (tamaño > 0){  //me aseguro de no tener un heap vacío
            maximo = elems.get(0);
            elems.set(0, ultimo);  //subo el último elemento de mi heap a la raíz
            elems.set(tamaño - 1, null); // ya que subí el último elemento a la raíz, lo elimino de la última posición (donde originalmente estaba).
            tamaño --; 
        } // la complejidad de este if es O(1)

        if (tamaño > 0){ // solo si aún quedan elementos en el Heap quiero reacomodar el elemento que subí según la prioridad del Heap
            siftDown(0);  // O(log n )
            ultimo = elems.get(tamaño-1); 
        }
        else{
            ultimo = null;  // caso en el que me quedé sin elementos en el heap
        } // la complejidad total de este if es O(log n)
        return maximo;    
    } // la complejidad de sacarMaximo es O(log n)


    public Heap<T> conjuntoAHeap(ArrayList<T> s){
        this.elems = new ArrayList<>(s); //O(n)
        this.tamaño = elems.size();
        if (tamaño != 0){
            int indice = (tamaño - 2)/2; //tomo el indice del padre del ultimo elemento
            while (indice >= 0){
                siftDown(indice); // la complejidad de siftDown es O(log n) pero en cada llamado, como mucho hace dos swaps por lo que se vuelve O(1)
                indice --;
            }
            this.ultimo = elems.get(tamaño-1);
        }
        else{
            ultimo = null;
        }
        return this;
    } // usamos el algoritmo de Floyd que tiene complejidad O(n)

    public int eliminar(int indice){ 
        int indiceFinal = -1;
        if (tamaño == 1){ //caso en el que tengo un solo elemento en el heap
            tamaño = 0;
            elems.set(0,null);
            ultimo = null; 
        }
        else if (indice == tamaño - 1){ //caso en que borro el ultimo elemento pero mi heap tiene más de un solo elemento
            elems.set(tamaño-1,null);
            tamaño --; 
            ultimo = elems.get(tamaño-1); 
        }
        else if (tamaño > 1 && indice < tamaño){
            tamaño --;
            elems.set(indice, ultimo);//reemplazo al elemento a eliminar por mi último elemento del heap
            elems.set(tamaño,null); //elimino el último elemento en la ubcación final
            indiceFinal = siftDown(indice); //acomodo el elemento que subí para respetar el orden de prioridad, O(log n)
            ultimo = elems.get(tamaño-1);

        } //la complejidad total de este if es O(log n) por el uso de siftDown
        return indiceFinal; //devuelve el indice donde queda el que antes era el ultimo elemento en el heap (excepto que eliminies el ultimo)
    } //la complejidad de eliminar es O(log n)

    public int verificarPosicion(int indice){
        int res = indice;
        T elemento = elems.get(indice);

        if (indice > 0){ //caso el elemento tiene padre
            T padre = elems.get((indice-1)/2); 
            if (comparator.compare(elemento, padre) > 0){ //chequeo que cumpla la relación de prioridad con su padre
                res = siftUp(indice);  // si no era correcta, lo acomodo
            }
        }
        else if ((2*indice + 1) < tamaño){ // caso tiene hijo izquierdo
            T hijoIzq = elems.get(2*indice + 1);
            if (hijoIzq != null && comparator.compare(elemento, hijoIzq) < 0){ //chequeo que cumpla la relación de prioridad con su hijo izquierdo
                res = siftDown(indice);
            }
        }
        else if ((2*indice + 2) < tamaño){  // caso tiene hijo derecho
            T hijoDer = elems.get(2*indice + 2);  //chequeo que cumpla la relación de prioridad con su hijo derecho
            if (hijoDer != null && comparator.compare(elemento, hijoDer) < 0){
                res = siftDown(indice);
            }
        }
        // estos ifs tienen complejidad O(log n) por siftDown y/o siftUp
        ultimo = elems.get(tamaño-1);
        return res; // devuelvo la posición correcta del elemento 
    } //la complejidad de revisar es O(log n)

    public T obtenerElemento(int indice){
        T res = null;
        if (indice < tamaño){
            res = elems.get(indice);
        }
        return res;
    } //O(1)
    
    private int siftUp(int indice) {  
        while (indice > 0) {  //me aseguro de que mi elemento tenga un padre //la cantidad de iteraciones del ciclo es igual a la altura del heap (es decir, log n)
            int indicePadre = (indice - 1) / 2;

            if(comparator.compare(elems.get(indice), elems.get(indicePadre)) <= 0) {  //si el padre de mi elemento tiene mayor prioridad, dejo de subir 
                break;
            }  
            
            T elementoActual = elems.get(indice); // me guardo el elemento actual
            elems.set(indice, elems.get(indicePadre)); //"swapeo" el elemento con su padre en estas dos líneas
            elems.set(indicePadre, elementoActual); 
            indice = indicePadre; // ahora cambio el indice para seguir subiendo a los padres necesarios
        } return indice;  // siftUp devuelve el indice final del elemento que subí
    }    //la complejidad del while es O(1) + (log n)*(O(1)+O(1)), es decir, O(log n)  

    // armamos SiftUp para no usar indexOf, pues nos aumenta la complejidad a O(n), siftUp devuelve el indice final del elemento que subí y tiene complejidad O(log n)

    
    private int siftDown(int indice){
        int indiceIzquierdo, indiceDerecho, mayorIndice;
        mayorIndice = indice; 

            while (indice < tamaño) { //hay log n  iteraciones porque sube de nivel a través de los padres
                indiceIzquierdo = 2 * indice + 1; 
                indiceDerecho = 2 * indice + 2; 
    
                // Comparo con el hijo izquierdo
                if (indiceIzquierdo < tamaño && comparator.compare(elems.get(indiceIzquierdo), elems.get(mayorIndice)) > 0) {
                    mayorIndice = indiceIzquierdo;
                }

                // Comparo con el hijo derecho
                if (indiceDerecho < tamaño && comparator.compare(elems.get(indiceDerecho), elems.get(mayorIndice)) > 0) {
                    mayorIndice = indiceDerecho;
                }
                
                // Si el mayor es el mismo índice, el elemento está en su lugar correcto
                if (mayorIndice == indice) {
                    break;
                }
                //todos estos if tienen complejidad O(1)

                // Intercambio los elementos con el mismo proceso de "swap" que use en siftUp
                T temp = elems.get(indice);
                elems.set(indice, elems.get(mayorIndice));
                elems.set(mayorIndice, temp);

                // Continuar desde la posición del mayor
                indice = mayorIndice;
            }
            //la complejidad de este while es O(1) + (log n)*O(1) = O(log n)

            return indice; //siftDown devuelve el indice final del elemento que bajé
    } //la complejidad de siftDown es O(log n)
}  
