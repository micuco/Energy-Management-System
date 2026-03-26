package Tema1;

public abstract class ProducatorEnergie extends ComponentaRetea {
    private double putere;

    public ProducatorEnergie(String id, double putere) {
        super(id);
        this.putere = putere;
    }

    public abstract double calculeazaProductie(double factorExtern);

    public double getPutere() {
        return putere;
    }
}
