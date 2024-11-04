package aed;
import java.util.Comparator;

public class InfoCiudad {
    int id;
    int ganancia;
    int perdida;
    int superavit;

    public InfoCiudad(int id, int ganancia, int perdida){
        this.id = id;
        this.ganancia = ganancia;
        this.perdida = perdida;
        this.superavit = ganancia - perdida;
    }

/* Probablemente haya que quitar estas funcioncitas
    public int id(){
        return this.id;
    }

    public int ganancia(){
        return this.ganancia;
    }

    public int perdida(){
        return this.perdida;
    }

    public int superavit(){
        return this.superavit;
    }
*/

    public void agregarGanancia(int nuevaGanancia){
        this.ganancia = ganancia + nuevaGanancia;
        this.superavit = superavit + nuevaGanancia;
    }

    public void agregarPerdida(int nuevaPerdida){
        this.perdida = perdida + nuevaPerdida;
        this.superavit = superavit - nuevaPerdida;
    }




    //ver si los comparators van acá o en otro lado
    public class GananciasComparator implements Comparator <InfoCiudad>{
        @Override
        public int compare(InfoCiudad c1, InfoCiudad c2){
            return Integer.compare(c1.ganancia, c2.ganancia);
        }
    }

    public class PerdidasComparator implements Comparator <InfoCiudad>{
        @Override
        public int compare(InfoCiudad c1, InfoCiudad c2){
            return Integer.compare(c1.perdida, c2.perdida);
        }
    }

    public class SuperavitComparator implements Comparator <InfoCiudad>{
        @Override
        public int compare(InfoCiudad c1, InfoCiudad c2){
            int comparacionSuperavit = Integer.compare(c1.ganancia, c2.ganancia);
            if (comparacionSuperavit != 0){
                return comparacionSuperavit;
            } else{
                return Integer.compare(c2.id, c1.id);
            }
        }
    }
}
