package Tema1;

public class Baterie extends ComponentaRetea {
    private double capacitateMaxima;
    private double energieStocata;

    public Baterie(String id, double capacitateMaxima) {
        super(id);
        this.capacitateMaxima = capacitateMaxima;
        this.energieStocata = 0;
    }

    public double incarca(double energieDisponibila) {
        double spatiuDisponibil = capacitateMaxima - energieStocata;
        double surplus = 0;
        if (!this.isStatusOperational()) {
            return energieDisponibila;
        }

        if (energieDisponibila <= spatiuDisponibil) {
            energieStocata += energieDisponibila;
        } else {
            energieStocata = capacitateMaxima;
            surplus = energieDisponibila - spatiuDisponibil;
        }

        return surplus;
    }

    public double descarca(double energieCeruta) {
        double energie;

        if (!this.isStatusOperational()) {
            return 0;
        }

        if (energieCeruta <= energieStocata) {
            energieStocata -= energieCeruta;
            energie = energieCeruta;
        } else {
            energie = energieStocata;
            energieStocata = 0;
        }

        return energie;
    }

    public double getCapacitateMaxima() {
        return capacitateMaxima;
    }

    public double getEnergieStocata() {
        return energieStocata;
    }
}
