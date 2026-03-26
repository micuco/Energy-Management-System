package Tema1;

public class PanouSolar extends ProducatorEnergie {
    public PanouSolar(String id, double putereMaxima) {
        super(id, putereMaxima);
    }

    @Override
    public double calculeazaProductie(double factorExtern) {
        if (this.isStatusOperational()) {
            return this.getPutere() * factorExtern;
        }
        return 0;
    }
}
