package Tema1;

public class ReactorNuclear extends ProducatorEnergie {
    public ReactorNuclear(String id, double putereConstanta) {
        super(id, putereConstanta);
    }

    @Override
    public double calculeazaProductie(double factorExtern) {
        if (this.isStatusOperational()) {
            return this.getPutere();
        }

        return 0;
    }
}
