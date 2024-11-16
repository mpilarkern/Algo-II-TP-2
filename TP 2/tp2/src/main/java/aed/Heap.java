package aed;

import java.util.ArrayList;
import java.util.Comparator;

// implementamos el Heap sobre un array

public class Heap<T> {
    private ArrayList<T> elems; 
    private Comparator<T> comparator; 
    private T ultimo;
    private int tamaño; 

    public Heap(Comparator<T> comparator){ //Implementamos el constructor de la clase
        elems = new ArrayList<T>(); //O(1)
        this.comparator = comparator; //O(1)
        tamaño = 0; //O(1) 
        ultimo = null; //O(1)
    }

    public int tamaño(){
        return tamaño; //O(1)
    }

    public int agregar(T e){
        if (elems.size() == tamaño){  //si no tengo ningún espacio de memoria vacío (null) en el heap, entonces agrego el elemento al final creando su espacio, la complejidad de la guarda es O(1) //CHEQUEAR si se puede usar SIZE() *
            elems.add(e); //O(1)
        }
        else{ // caso contrario, ubico el nuevo elemento en el primero espacio "null" de mi heap
            elems.set(tamaño,e); //O(1)
        }
        // la complejidad de este if es (O(1) + máx{O(1),O(1)})= O(1)
        int indice = siftUp(tamaño); //siftUp me devuelve el indice en donde terminó el elemento que agregué al heap luego de acomodarlo, su complejidad es O(log n)
        ultimo = elems.get(tamaño);  // modifico el atributo "último" de mi heap para actualizar correctamente mis datos //get es O(1)
        tamaño ++; // modifico el atributo tamaño de mi heap para actualizar correctamente mis datos, O(1)
        return indice; // devuelvo el índice en donde está el elemento que agregué al heap
    } // la complejidad de agregar es O(1) + O(log n) = O(log n)

    private int siftUp(int indice) {  //recibo el indice del elemento que quiero acomodar "hacia arriba" 
        while (indice > 0) {  //me aseguro de que mi elemento tenga un padre //La complejidad de nuestra guarda es O(1), la cantidad de iteraciones del ciclo es igual a la altura del heap (es decir, log n)
            int indicePadre = (indice - 1) / 2; // O(1)

            if(comparator.compare(elems.get(indice), elems.get(indicePadre)) <= 0) {  //si el padre de mi elemento tiene mayor prioridad, dejo de subir //la complejidad del comparator es O(1)? ***
                break;
            }   //la complejidad de este if es O(1)
            
            T elementoActual = elems.get(indice); // para evitar aliasing, me guardo el elemento actual, get es O(1)
            elems.set(indice, elems.get(indicePadre)); //"swapeo" el elemento con su padre en estas dos líneas
            elems.set(indicePadre, elementoActual); // set es O(1)
            indice = indicePadre; //ahora cambio el indice para seguir subiendo a los padres necesarios
        } return indice;  //cambiamos esto para no usar indexOf pues nos aumenta la complejidad a O(n), siftUp devuelve el indice final del elemento que subí
    }    //la complejidad del while es O(1) + (log n)*(O(1)+O(1)), es decir, O(log n)   
    // armamos SiftUp para no usar indexOf, pues nos aumenta la complejidad a O(n), siftUp devuelve el indice final del elemento que subí y tiene complejidad O(log n)

    public T maximo(){
        return elems.get(0);  //O(1)
    }

    public T sacarMaximo(){
        T maximo = null; //O(1)
        if (tamaño > 0){  //me aseguro de no tener un heap vacío, la complejidad de la guarda es O(1)
            maximo = elems.get(0); //O(1)
            elems.set(0, ultimo);  //subo el último elemento de mi heap a la raíz, O(1)
            elems.remove(tamaño - 1); // ya que subí el último elemento a la raíz, lo elimino de la última posición (donde originalmente estaba). Remove tiene complejidad 0(n).
            tamaño --; //ajusto el atributo tamaño, O(1)
        } //la complejidad total de este if es O(n)
        if (tamaño > 0){ { //comparo nuevamente porque ahora al tamaño le resté una unidad, O(1)
            siftDown(0);  //ahora que subí el último elemento, lo reacomodo según la prioridad de mi heap con siftDown, O(log n )
            ultimo = elems.get(tamaño-1);  //ajusto el atributo porque cambié mi último elemento del heap, O(1)
        }
        else{
            ultimo = null;  // caso en el que me quedé sin elementos en el heap, O(1)
        } ///la complejidad total de este if es O(log n)
        return maximo;    
    } // la complejidad de scarMaximo es O(n) debido al uso de Remove

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

                //aclaración: no comparamos entre hijo izquierdo y derecho porque las colas de prioridad solo requieren que el padre tenga mayor prioridad que sus hijos, no establece relaciones entre "hermanos"
                
                // Si el mayor es el mismo índice, el elemento está en su lugar correcto
                if (mayorIndice == indice) {
                    break;
                }
                //todos estos if tienen complejidad O(1)

                // Intercambio los elementos con el mismo proceso de "swap" que use en siftUp
                T temp = elems.get(indice);
                elems.set(indice, elems.get(mayorIndice));
                elems.set(mayorIndice, temp);
                //tanto get como set tienen complejidad O(1)

                // Continuar desde la posición del mayor
                indice = mayorIndice;
            }
            //la complejidad de este while es O(1) + (log n)*O(1) = O(log n) donde n es la cantidad de elementos del heap

            return indice; //siftDown devuelve el indice final del elemento que baje
    } //la complejidad de siftDown es O(log n)

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

    public int eliminar(int indice){ //recibo como parámetro el índice del elemento a eliminar
        int indiceFinal = -1; //inicializo mi variable
        if (tamaño == 1){ //caso en el que tengo un solo elemento en el heap, la guarda tiene complejidad O(1)
            tamaño = 0; //acomodo el tamaño, O(1)
            elems.set(0,null); //elimino el elemento, O(1)
            ultimo = null; //me queda un heap vacío, O(1)
        }
        else if (indice == tamaño - 1){ //caso en que borro el ultimo elemento pero mi heap tiene más de un solo elemento
            elems.set(tamaño-1,null); //elimino el elemento, O(1)
            tamaño --; //acomodo el tamaño, O(1)
            ultimo = elems.get(tamaño-1); //acomodo el último, O(1)
        }
        else if (tamaño > 1 && indice < tamaño){ //caso en que tengo más de un elemento en el heap pero el que elimino no es el último elemento
            tamaño --;  //acomodo el tamaño, O(1)
            elems.set(indice, ultimo);//reemplazo al elemento a eliminar por mi último elemento del heap, O(1)
            elems.set(tamaño,null); //elimino el último elemento en la ubcación final, O(1)
            indiceFinal = siftDown(indice); //acomodo el elemento que subí para respetar el orden de prioridad, O(log n)
            ultimo = elems.get(tamaño-1); //acomodo el último, O(1)

        } //la complejidad total de este if es O(log n) por el uso de siftDown
        return indiceFinal; //devuelve el indice donde queda el que antes era el ultimo elemento en el heap (excepto que eliminies el ultimo)
    } //la complejidad de eliminar es O(log n) por el uso de siftDown

    public int revisar(int indice){ // CAMBIAR NOMBRE, les gusta verificarPosicion? porfi * //recibo el índice del elemento que quiero revisar
        int res = indice;  //inicializo variables O(1)
        T elemento = elems.get(indice);

        if (indice > 0){ 
            T padre = elems.get((indice-1)/2); 
            if (comparator.compare(elemento, padre) > 0){ //chequeo que cumpla la relación de prioridad con su padre
                res = siftUp(indice);  // si no era correcta, lo acomodo
            }
        }
        else if ((2*indice + 1) < tamaño){
            T hijoIzq = elems.get(2*indice + 1); //chequeo que cumpla la relación de prioridad con su hijo izquierdo
            if (hijoIzq != null && comparator.compare(elemento, hijoIzq) < 0){
                res = siftDown(indice);
            }
        }
        else if ((2*indice + 2) < tamaño){
            T hijoDer = elems.get(2*indice + 2);  //chequeo que cumpla la relación de prioridad con su hijo derecho
            if (hijoDer != null && comparator.compare(elemento, hijoDer) < 0){
                res = siftDown(indice);
            }
        }
        //la complejidad de las guardas de estos ifs, el uso de elems.get y las comparaciones es O(1), siftDown tiene complejidad O(log n)
        ultimo = elems.get(tamaño-1);
        return res; // devuelvo la posición correcta del elemento 
    } //la complejidad de revisar es O(log n) debido al uso de siftDown

    public T obtenerElemento(int indice){
        T res = null;
        if (indice < tamaño){
            res = elems.get(indice);
        }
        return res;
    } //todas estas operaciones tienen complejidad O(1)

    public T obtenerPadre(int indice){
        return elems.get((indice-1)/2);
    } //O(1)

    public T obtenerHijoIzq(int indice){
        return elems.get(2*indice+1);
    } //O(1)

    public T obtenerHijoDer(int indice){
        return elems.get(2*indice+2);
    } //O(1)

}  

