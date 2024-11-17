package aed;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Comparator;

// Creamos estos test para testear Heap con un tipo más complejo. 
// Además, podemos probar la funcion verificarPosicion(indice)

public class HeapTestTupla {

    private class compararPorValor1 implements Comparator <Tupla>{
        @Override
        public int compare(Tupla t1, Tupla t2){
            return Integer.compare(t1.getValor1(), t2.getValor1());
        }
    }

    private compararPorValor1 compararPorValor1;

    
    private class compararPorValor2 implements Comparator <Tupla>{
        @Override
        public int compare(Tupla t1, Tupla t2){
            return Integer.compare(t1.getValor2(), t2.getValor2());
        }
    }

    private compararPorValor2 compararPorValor2;

    @Test
    public void testHeapConTuplasPorValor1() {
        compararPorValor1 = new compararPorValor1();
        Heap<Tupla> heap = new Heap<>(compararPorValor1);

        // Agregar elementos
        Tupla t1 = new Tupla(3, 10);
        Tupla t2 = new Tupla(1, 20);
        Tupla t3 = new Tupla(5, 5);
        Tupla t4 = new Tupla(2, 15);

        heap.agregar(t1);
        heap.agregar(t2);
        heap.agregar(t3);
        heap.agregar(t4);

        // Verificar el máximo
        assertEquals(t3, heap.maximo(), "El máximo debería ser la tupla (5, 5)");

        // Sacar el máximo y verificar
        Tupla maximo = heap.sacarMaximo();
        assertEquals(t3, maximo, "El máximo debería ser la tupla (5, 5)");

        // Verificar que el nuevo máximo sea correcto
        assertEquals(t1, heap.maximo(), "El nuevo máximo debería ser la tupla (3, 10)");
    }

    @Test
    public void testHeapConTuplasPorValor2() {
        compararPorValor2 = new compararPorValor2();
        Heap<Tupla> heap = new Heap<>(compararPorValor2);

        // Agregar elementos
        Tupla t1 = new Tupla(3, 10);
        Tupla t2 = new Tupla(1, 20);
        Tupla t3 = new Tupla(5, 5);
        Tupla t4 = new Tupla(2, 15);

        heap.agregar(t1);
        heap.agregar(t2);
        heap.agregar(t3);
        heap.agregar(t4);

        // Verificar el máximo por valor2
        assertEquals(t2, heap.maximo(), "El máximo debería ser la tupla (1, 20)");

        // Sacar el máximo y verificar
        Tupla maximo = heap.sacarMaximo();
        assertEquals(t2, maximo, "El máximo debería ser la tupla (1, 20)");

        // Verificar que el nuevo máximo sea correcto
        assertEquals(t4, heap.maximo(), "El nuevo máximo debería ser la tupla (2, 15)");
    }

    @Test
    public void testEliminacionConTuplas() {
        compararPorValor1 = new compararPorValor1();
        Heap<Tupla> heap = new Heap<>(compararPorValor1);

        // Agregar elementos
        Tupla t1 = new Tupla(3, 10);
        Tupla t2 = new Tupla(1, 20);
        Tupla t3 = new Tupla(5, 5);
        Tupla t4 = new Tupla(2, 15);

        heap.agregar(t1);
        heap.agregar(t2);
        heap.agregar(t3);
        heap.agregar(t4);

        // Eliminar un elemento y verificar el resultado
        heap.eliminar(1);  // Eliminando la tupla (1, 20)
        assertEquals(3, heap.tamaño(), "El tamaño debería ser 3 después de eliminar un elemento");

        // Verificar que el máximo sea correcto después de la eliminación
        assertEquals(t3, heap.maximo(), "El máximo debería seguir siendo la tupla (5, 5)");
    }

    @Test
    public void testRevisarConTuplas() {
        compararPorValor1 = new compararPorValor1();
        Heap<Tupla> heap = new Heap<>(compararPorValor1);

        // Agregar elementos
        Tupla t1 = new Tupla(3, 10);
        Tupla t2 = new Tupla(1, 20);
        Tupla t3 = new Tupla(5, 5);
        Tupla t4 = new Tupla(2, 15);

        heap.agregar(t1);
        heap.agregar(t2);
        heap.agregar(t3);
        heap.agregar(t4);

        // Cambio el valor de una tupla
        t2.cambiarValor1(4);
        assertEquals(1, heap.verificarPosicion(3), "t2 sube en el heap");

        
        // Cambio el valor de una tupla hasta superar el maximo
        t1.cambiarValor1(6);

        // Verificar que el máximo sea correcto después de la eliminación
        heap.verificarPosicion(2);
        assertEquals(t1, heap.maximo(), "El máximo debería ser (6,10)");
    }
}
