package Tema1;

public class TurbinaEoliana extends ProducatorEnergie {
    public TurbinaEoliana(String id, double putereBaza) {
        super(id, putereBaza);
    }

    @Override
    public double calculeazaProductie(double factorExtern) {
        if (this.isStatusOperational()) {
            return this.getPutere() * factorExtern;
        }

        return 0;
    }
}
