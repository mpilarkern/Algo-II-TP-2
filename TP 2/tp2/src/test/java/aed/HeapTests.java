package aed;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

//Estos son tests básicos para ver que cada método de nuestra clase Heap funcione bien

public class HeapTests {
    private Heap<Integer> heap; 
    private Comparator<Integer> comparator; 


    private Heap<Integer> crearMaxHeap() {
        return new Heap<Integer>(Comparator.naturalOrder()); //usamos este comparador para naturales
    }

    @Test
    public void testAgregar() {
        Heap<Integer> heap = crearMaxHeap();
        assertEquals(0, heap.tamaño());

        heap.agregar(10);
        heap.agregar(20);
        heap.agregar(5);

        assertEquals(3, heap.tamaño());
        assertEquals(20, heap.maximo());
    }

    @Test
    public void testMaximo() {
        Heap<Integer> heap = crearMaxHeap();
        heap.agregar(10);
        heap.agregar(30);
        heap.agregar(20);

        assertEquals(30, heap.maximo());

        heap.sacarMaximo();
        assertEquals(20, heap.maximo());
    }

    @Test
    public void testSacarMaximo() {
        Heap<Integer> heap = crearMaxHeap();
        heap.agregar(15);
        heap.agregar(10);
        heap.agregar(20);
        heap.agregar(25);

        assertEquals(25, heap.sacarMaximo());
        assertEquals(20, heap.sacarMaximo());
        assertEquals(15, heap.sacarMaximo());
        assertEquals(10, heap.sacarMaximo());
        assertNull(heap.sacarMaximo());
    }

    @Test
    public void testConjuntoAHeap() {
        Heap<Integer> heap = crearMaxHeap();
        ArrayList<Integer> lista = new ArrayList<>();
        lista.add(5);
        lista.add(15);
        lista.add(10);
        lista.add(20);

        heap.conjuntoAHeap(lista);
        assertEquals(4, heap.tamaño());
        assertEquals(20, heap.maximo());
    }

    @Test
    public void testEliminar() {
        Heap<Integer> heap = crearMaxHeap();
        heap.agregar(10);
        heap.agregar(20);
        heap.agregar(30);
        heap.agregar(40);
        heap.agregar(50);

        int indiceEliminado = heap.eliminar(2);
        assertEquals(4, heap.tamaño());
        assertEquals(50, heap.maximo());
        assertEquals(2, indiceEliminado);  // Indice final donde quedó el elemento movido

        assertNull(heap.obtenerElemento(4));  // Debe ser null porque removimos un elemento
    }

    @Test
    public void testObtenerElemento() {
        Heap<Integer> heap = crearMaxHeap();
        heap.agregar(30);
        heap.agregar(20);
        heap.agregar(10);

        assertEquals(30, heap.obtenerElemento(0));
        assertEquals(20, heap.obtenerElemento(1));
        assertEquals(10, heap.obtenerElemento(2));
        assertNull(heap.obtenerElemento(3));  // Fuera de rango
    }
}
