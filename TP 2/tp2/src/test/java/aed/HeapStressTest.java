package aed;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class HeapStressTest {

    @Test
    public void stressTestHeap() {
        // Usar un Comparator que maneje valores nulos
        Comparator<Integer> safeComparator = (a, b) -> {
            if (a == null && b == null) return 0;
            if (a == null) return -1;
            if (b == null) return 1;
            return a.compareTo(b);
        };
    
        Heap<Integer> heap = new Heap<>(safeComparator);
        
        Random rand = new Random();
        int maxSize = 100000;
        
        // Agregar muchos elementos
        for (int i = 0; i < maxSize; i++) {
            heap.agregar(rand.nextInt(100000));
        }
        
        // Realizar varias operaciones
        for (int i = 0; i < 50000; i++) {
            heap.sacarMaximo();
            heap.agregar(rand.nextInt(100000));
        }
        
        // Comprobar que la estructura del heap sigue siendo correcta
        int lastMax = heap.sacarMaximo();
        while (heap.tamaño() > 0) {
            int nextMax = heap.sacarMaximo();
            assertTrue(lastMax >= nextMax, "El Heap está desordenado");
            lastMax = nextMax;
        }
    }
    
}
