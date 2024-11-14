package aed;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class HeapTests {
    private Heap<Integer> heap; 
    private Comparator<Integer> comparator; 

    @BeforeEach 
    void setUp() { 
        comparator = Integer::compareTo; // Comparator for integers 
        heap = new Heap<>(comparator); 
    } 
    @Test 
    void testConjuntoAHeap() { 
        ArrayList<Integer> elementos = new ArrayList<>(Arrays.asList(3, 1, 6, 5, 2, 4)); 
        heap.conjuntoAHeap(elementos); 
        
        // Verificar el tamaño del heap 
        assertEquals(6, heap.tamaño(), "El tamaño del heap debe ser 6"); 
        
        // Verificar la estructura del heap 
        // En un max-heap, el primer elemento debe ser el mayor 
        assertEquals(6, heap.maximo(), "El elemento máximo debe ser 6"); 
        
        // Verificar los elementos ordenados según las propiedades del heap 
        assertEquals(6, heap.sacarMaximo(), "El máximo extraído debe ser 6"); 
        assertEquals(5, heap.sacarMaximo(), "El máximo extraído debe ser 5"); 
        assertEquals(4, heap.sacarMaximo(), "El máximo extraído debe ser 4"); 
        assertEquals(3, heap.sacarMaximo(), "El máximo extraído debe ser 3"); 
        assertEquals(2, heap.sacarMaximo(), "El máximo extraído debe ser 2"); 
        assertEquals(1, heap.sacarMaximo(), "El máximo extraído debe ser 1"); 
        
        // Verificar que el heap está vacío después de extraer todos los elementos
        assertEquals(0, heap.tamaño(), "El tamaño del heap debe ser 0 después de extraer todos los elementos");
    }
}

