package aed;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Armamos estos tests para verificar que cada método de BestEffort funciona correctamente
// Incluimos tests que hacen varias operaciones una tras otra y tests de casos particulares como un sistema vacío o cuando disminuye el superavit de la ciudad de superavit máximo

public class BestEffortTestsPersonales {

    int cantCiudades;
    Traslado[] listaTraslados;

    @BeforeEach
    void init(){
        cantCiudades = 5;
        listaTraslados = new Traslado[] {
            new Traslado(1, 0, 1, 200, 10),
            new Traslado(2, 1, 2, 300, 20),
            new Traslado(3, 2, 3, 400, 30),
            new Traslado(4, 3, 4, 500, 40),
            new Traslado(5, 4, 0, 100, 50),
            new Traslado(6, 0, 1, 2000, 11),
            new Traslado(7, 0, 1, 600, 12)
        };
    }

    void assertSetEquals(ArrayList<Integer> s1, ArrayList<Integer> s2) {
        assertEquals(s1.size(), s2.size());
        for (int e1 : s1) {
            boolean encontrado = false;
            for (int e2 : s2) {
                if (e1 == e2) encontrado = true;
            }
            assertTrue(encontrado, "No se encontró el elemento " +  e1 + " en el arreglo " + s2.toString());
        }
    }

    @Test
    void despachar_combinado(){
        BestEffort sis = new BestEffort(this.cantCiudades, this.listaTraslados);

        sis.despacharMasRedituables(2);

        assertSetEquals(new ArrayList<>(Arrays.asList(0)), sis.ciudadesConMayorGanancia());
        assertSetEquals(new ArrayList<>(Arrays.asList(1)), sis.ciudadesConMayorPerdida());

        sis.despacharMasAntiguos(2);
        assertSetEquals(new ArrayList<>(Arrays.asList(0)), sis.ciudadesConMayorGanancia());
        assertSetEquals(new ArrayList<>(Arrays.asList(1)), sis.ciudadesConMayorPerdida());
    }

    @Test
    void ciudades_sin_cambios(){
        BestEffort sis = new BestEffort(this.cantCiudades, this.listaTraslados);

        assertSetEquals(new ArrayList<>(), sis.ciudadesConMayorGanancia());
        assertSetEquals(new ArrayList<>(), sis.ciudadesConMayorPerdida());

        sis.despacharMasAntiguos(0);
        assertSetEquals(new ArrayList<>(), sis.ciudadesConMayorGanancia());
        assertSetEquals(new ArrayList<>(), sis.ciudadesConMayorPerdida());
    }

    @Test
    void registrar_y_despachar(){
        BestEffort sis = new BestEffort(this.cantCiudades, this.listaTraslados);

        Traslado[] nuevos = new Traslado[] {
            new Traslado(8, 0, 4, 13000, 1),
            new Traslado(9, 2, 3, 15000, 3),
            new Traslado(10, 4, 1, 13000, 2),
            new Traslado(11, 4, 2, 1800, 4)

        };
        sis.registrarTraslados(nuevos);

        sis.despacharMasRedituables(5);
        sis.despacharMasAntiguos(1);
        assertSetEquals(new ArrayList<>(Arrays.asList(0)), sis.ciudadesConMayorGanancia());
        assertSetEquals(new ArrayList<>(Arrays.asList(1)), sis.ciudadesConMayorPerdida());
    }
    @Test
    void registrar_y_despachar_todo(){ // despacho todo 
        BestEffort sis = new BestEffort(this.cantCiudades, this.listaTraslados);

        Traslado[] nuevos = new Traslado[] {
            new Traslado(8, 0, 4, 13000, 1),
            new Traslado(9, 2, 3, 15000, 3),
            new Traslado(10, 4, 1, 13000, 2),
            new Traslado(11, 4, 2, 1800, 4),
            new Traslado(12, 4, 0, 900, 8), 
            new Traslado(13, 1, 3, 400, 9),

        };
        sis.registrarTraslados(nuevos);

        sis.despacharMasRedituables(6);
        sis.despacharMasAntiguos(7);
        assertSetEquals(new ArrayList<>(Arrays.asList(0,4)), sis.ciudadesConMayorGanancia());
        assertSetEquals(new ArrayList<>(Arrays.asList(1,3)), sis.ciudadesConMayorPerdida());
    }

    @Test
    void promedio_y_superavit(){ // cambia el superavit
        BestEffort sis = new BestEffort(this.cantCiudades, this.listaTraslados);

        Traslado[] nuevos = new Traslado[] {
            new Traslado(8, 0, 1, 200, 1),
            new Traslado(9, 1, 0, 300, 2),
            
        };
        sis.registrarTraslados(nuevos);

        sis.despacharMasAntiguos(1);
        assertEquals(0, sis.ciudadConMayorSuperavit());

        sis.despacharMasAntiguos(1);
        
        assertEquals(1,sis.ciudadConMayorSuperavit());
    }

    @Test
    void sistema_vacio() {
        Traslado[] trasladosVacio = new Traslado[0];
        BestEffort sis = new BestEffort(1, trasladosVacio);


        // Comparación con listas vacías
        assertEquals(new ArrayList<>(), sis.ciudadesConMayorGanancia());
        assertEquals(new ArrayList<>(), sis.ciudadesConMayorPerdida());

        assertEquals(-1, sis.ciudadConMayorSuperavit()); //da -1 porque los int no tienen null

    }
}
