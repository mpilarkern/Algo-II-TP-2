package aed;

public class Tupla {
    private int valor1;
    private int valor2;

    public Tupla(int valor1, int valor2) {
        this.valor1 = valor1;
        this.valor2 = valor2;
    }

    public int getValor1() {
        return valor1;
    }

    public int getValor2() {
        return valor2;
    }

    public void  cambiarValor1(int valor){
        this.valor1 = valor;
    }

    public void  cambiarValor2(int valor){
        this.valor2 = valor;
    }

    @Override
    public String toString() {
        return "(" + valor1 + ", " + valor2 + ")";
    }
}

