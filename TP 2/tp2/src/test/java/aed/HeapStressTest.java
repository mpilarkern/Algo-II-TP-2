package aed;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

// La idea de este test es ver que nuestra clase Heap funcione bien cuando agramos muchos elementos y hacemos muchas operaciones

public class HeapStressTest {

    @Test
    public void stressTestHeap() {
        // Usamos un Comparator que maneje valores nulos
        Comparator<Integer> safeComparator = (a, b) -> {
            if (a == null && b == null) return 0;
            if (a == null) return -1;
            if (b == null) return 1;
            return a.compareTo(b);
        };
    
        Heap<Integer> heap = new Heap<>(safeComparator);
        
        Random rand = new Random();
        int tam = 100000;
        
        // Agregamos muchos elementos aleatorios al Heap usando Random
        for (int i = 0; i < tam; i++) {
            heap.agregar(rand.nextInt(100000));
        }
        
        // Realizamos varias operaciones
        for (int i = 0; i < 50000; i++) {
            heap.sacarMaximo();
            heap.agregar(rand.nextInt(100000));
        }
        
        // Comprobamos que la estructura del heap sigue siendo correcta
        int maxAnterior = heap.sacarMaximo();
        while (heap.tamaño() > 0) {
            int maxSiguiente = heap.sacarMaximo();
            assertTrue(maxAnterior >= maxSiguiente, "El Heap está desordenado");
            maxAnterior = maxSiguiente;
        }
    }
    
}
