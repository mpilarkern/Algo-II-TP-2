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

    public void agregarGanancia(int nuevaGanancia){
        this.ganancia = ganancia + nuevaGanancia;
        this.superavit = superavit + nuevaGanancia;
    } //O(1)

    public void agregarPerdida(int nuevaPerdida){
        this.perdida = perdida + nuevaPerdida;
        this.superavit = superavit - nuevaPerdida;
    } //O(1)



    public class GananciasComparator implements Comparator <InfoCiudad>{
        @Override
        public int compare(InfoCiudad c1, InfoCiudad c2){
            return Integer.compare(c1.ganancia, c2.ganancia);
        }
    } //O(1)

    public class PerdidasComparator implements Comparator <InfoCiudad>{
        @Override
        public int compare(InfoCiudad c1, InfoCiudad c2){
            return Integer.compare(c1.perdida, c2.perdida);
        }
    } //O(1)


}
