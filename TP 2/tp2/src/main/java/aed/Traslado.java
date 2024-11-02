package aed;
import java.util.Comparator;

public class Traslado {
    
    int id;
    int origen;
    int destino;
    int gananciaNeta;
    int timestamp;

    public Traslado(int id, int origen, int destino, int gananciaNeta, int timestamp){
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.gananciaNeta = gananciaNeta;
        this.timestamp = timestamp;
    }

//ver si los comparators van acá o en otro lado
    public class GananciasComparator implements Comparator <Traslado>{
        @Override
        public int compare(Traslado t1, Traslado t2){
            return Integer.compare(t1.gananciaNeta, t2.gananciaNeta);
        }
    }

    public class TimeComparator implements Comparator <Traslado>{
        @Override
        public int compare(Traslado t1, Traslado t2){
            return Integer.compare(t1.timestamp, t2.timestamp);
        }
    }
}
