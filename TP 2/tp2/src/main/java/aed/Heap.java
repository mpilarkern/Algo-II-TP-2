package aed;

import java.util.ArrayList;
import java.util.Comparator;

// implementamos el Heap sobre un array

public class Heap<T> {
    private ArrayList<T> elems;
    private Comparator<T> comparator;
    private T ultimo;
    private int tamaño; 

    public Heap(Comparator<T> comparator){
        elems = new ArrayList<T>(); //O(1)
        this.comparator = comparator; //O(1)
        tamaño = 0; //O(1)
        ultimo = null; //O(1)
    }

    public int tamaño(){
        return tamaño; //O(1)
    }

    public int agregar(T e){
        if (elems.size() == tamaño){ //si no tengo ningún espacio de memoria vacío (null) en el heap, entonces agrego el elemento al final creando su espacio //CHEQUEAR si se puede usar SIZE() ***
            elems.add(e); //O(1)
        }
        else{                        // caso contrario, ubico el nuevo elemento en el primero espacio "null" de mi heap
            elems.set(tamaño,e); //O(1)
        }
        
        int indice = siftUp(tamaño); //siftUp me devuelve el indice en donde terminó el elemento que agregué al heap luego de acomodarlo
        ultimo = elems.get(tamaño); // modifico el atributo "último" de mi heap para actualizar correctamente mis datos //get es O(1)
        tamaño ++; // modifico el atributo tamaño de mi heap para actualizar correctamente mis datos
        return indice; 
    }

    private int siftUp(int indice) { //recibo el indice del elemento que quiero acomodar "hacia arriba" 
        while (indice > 0) { //me aseguro de que mi elemento tenga un padre //La complejidad de nuestra guarda es O(1), la cantidad de iteraciones del ciclo es igual a la altura del heap (es decir, log n)
            int indicePadre = (indice - 1) / 2; // O(1)

            if(comparator.compare(elems.get(indice), elems.get(indicePadre)) <= 0) { //si el padre de mi elemento tiene mayor prioridad, dejo de subir //la complejidad del comparator es O(1)? ***
                break;
                //la complejidad de este if es O(1)
            }
            
            T elementoActual = elems.get(indice); // para evitar aliasing, me guardo el elemento actual, get es O(1)
            elems.set(indice, elems.get(indicePadre)); //"swapeo" el elemento con su padre en estas dos líneas
            elems.set(indicePadre, elementoActual); // set es O(1)
            indice = indicePadre; //ahora cambio el indice para seguir subiendo a los padres necesarios
        } 
        return indice;  // armamos SiftUp para no usar indexOf, pues nos aumenta la complejidad a O(n), siftUp devuelve el indice final del elemento que subí
    } 
    //la complejidad del while es O(1) + (log n)*(O(1)+O(1)), es decir, O(log n)
    public T maximo(){
        return elems.get(0); //O(1)
    }

    public T sacarMaximo(){
        T maximo = null; //O(1)
        if (tamaño > 0){ //me aseguro de no tener un heap vacío, la complejidad de la guarda es O(1)
            maximo = elems.get(0); //O(1)
            elems.set(0, ultimo); //subo el último elemento de mi heap a la raíz, O(1)
            elems.remove(tamaño - 1); // ya que subí el último elemento a la raíz, lo elimino de la última posición (donde originalmente estaba). Remove tiene complejidad 0(n).
            tamaño --; //ajusto el atributo tamaño, O(1)
        } //la complejidad total de este if es O(n)
        if (tamaño > 0){ //comparo nuevamente porque ahora al tamaño le resté una unidad
            siftDown(0); //ahora que subí el último elemento, lo reacomodo según la prioridad de mi heap con siftDown
            ultimo = elems.get(tamaño-1); //ajusto el atributo porque cambié mi último elemento del heap
        }
        else{
            ultimo = null; // caso en el que me quedé sin elementos en el heap
        }
        return maximo;    
    }

    private int siftDown(int indice) { //recibo el índice del elemento que voy a acomodar hacia abajo
        int indiceIzquierdo, indiceDerecho, mayorIndice; // inicializo mis variables a utilizar
        mayorIndice = indice; 

            while (indice < tamaño) { 
                indiceIzquierdo = 2 * indice + 1; 
                indiceDerecho = 2 * indice + 2; 
    
                // Comparar con el hijo izquierdo
                if (indiceIzquierdo < tamaño && comparator.compare(elems.get(indiceIzquierdo), elems.get(mayorIndice)) > 0) {
                    mayorIndice = indiceIzquierdo; // si el hijo izquierdo es mayor, me lo guardo como el índice de mi mayor elemento
                }

                // Comparar con el hijo derecho
                if (indiceDerecho < tamaño && comparator.compare(elems.get(indiceDerecho), elems.get(mayorIndice)) > 0) {
                    mayorIndice = indiceDerecho; // si el hijo derecho es mayor, me lo guardo como el índice de mi mayor elemento
                }

                // aclaración: no comparamos entre hijo izquierdo e hijo derecho porque las colas de prioridad solo requieren que el padre tenga mayor prioridad que sus hijos
                
                // Si el mayor es el mismo índice, el elemento está en su lugar correcto
                if (mayorIndice == indice) {
                    break;
                }

                // Intercambio los elementos con el mismo proceso de "swap" que usé en siftUp
                T temp = elems.get(indice);
                elems.set(indice, elems.get(mayorIndice));
                elems.set(mayorIndice, temp);

                // Continuar desde la posición del mayor
                indice = mayorIndice;
            }

            return indice; // SiftDown devuelve el indice final del elemento que bajé
    }

    public Heap<T> conjuntoAHeap(ArrayList<T> s){
        this.elems = new ArrayList<>(s); // copio los elementos del conjunto que me dan como entrada
        this.tamaño = elems.size(); 
        int indice = (tamaño - 2)/2; //tomo el indice del padre del ultimo elemento
        while (indice >= 0){ //me aseguro de no irme de rango
            siftDown(indice); //acomodo cada elemento 
            indice --;
        }
        this.ultimo = elems.get(tamaño-1); //
        return this; 
    }   

    public int eliminar(int indice){ // recibo el índice del elemento que quiero eliminar
        int indiceFinal = -1; // inicializo la variable con un valor que luego se pisa
        if (tamaño > 1 && indice < tamaño){ // verifico que el heap tenga más de un elemento y que el índice esté dentro del rango
            tamaño --; //ajusto el tamaño
            elems.set(indice, ultimo); // coloco el último elemento del heap en la posición del elemento que eliminé
            elems.set(tamaño,null); // donde estaba el último, asigno null
            
            if (indice == tamaño) { // si el elemento que eliminé era el último de mi heap, no es necesario reacomodar al resto
                indiceFinal = tamaño - 1; // mi índice final pasa a ser el de mi nuevo último elemento
            } else {
                indiceFinal = siftDown(indice); // si no era el último del heap, sí es necesario reacomodarlo, y siftDown me devuelve el índice en donde terminó posicionado mi elemento
            }
            ultimo = elems.get(tamaño-1); // ajusto mi atributo último acorde a los cambios
        } else if (tamaño == 1 && indice == 0) { //caso en que tengo un solo elemento en el heap 
            tamaño--; //tamaño queda en 0
            elems.set(indice, ultimo); // INNECESARIO? LO BORRAMOS? ***
            elems.set(tamaño,null); // para eliminar el elemento, seteo un null
            indiceFinal=0; 
            ultimo = null; // ajusto mi atributo 
        }
        return indiceFinal; //devuelve el indice donde queda el elemento que antes era el ultimo en el heap (excepto que eliminies el ultimo)
    }

    public int revisar(int indice){ // CAMBIAR NOMBRE, les gusta verificarPosicion? porfi *** //recibo el índice del elemento que quiero revisar
        int res = indice; //inicializo variables
        T elemento = elems.get(indice);

        if (indice > 0){
            T padre = elems.get((indice-1)/2);
            if (comparator.compare(elemento, padre) > 0){ //chequeo que cumpla la relación de prioridad con su padre
                res = siftUp(indice); // si no era correcta, lo acomodo
            }
        }
        else if ((2*indice + 1) < tamaño){
            T hijoIzq = elems.get(2*indice + 1);
            if (hijoIzq != null && comparator.compare(elemento, hijoIzq) < 0){ //chequeo que cumpla la relación de prioridad con su hijo izquierdo
                res = siftDown(indice);
            }
        }
        else if ((2*indice + 2) < tamaño){
            T hijoDer = elems.get(2*indice + 2);
            if (hijoDer != null && comparator.compare(elemento, hijoDer) < 0){ //chequeo que cumpla la relación de prioridad con su hijo derecho
                res = siftDown(indice);
            }
        }
        ultimo = elems.get(tamaño-1); 
        return res; // devuelvo la posición correcta del elemento 
    }

    public T obtenerElemento(int indice){ 
        T res = null; // inicializo res
        if (indice < tamaño){ // me aseguro de estar en rango
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

