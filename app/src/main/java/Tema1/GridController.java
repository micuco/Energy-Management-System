package Tema1;

import java.util.*;

public class GridController {
    private List<ConsumatorEnergie> consumatori;
    private List<ProducatorEnergie> producatori;
    private List<Baterie> baterii;
    private Map<String, ComponentaRetea> componenteById;
    private boolean esteInBlackout;
    private int tickCurent;
    private List<String> istoricEvenimente;

    public GridController() {
        this.consumatori = new ArrayList<>();
        this.producatori = new ArrayList<>();
        this.baterii = new ArrayList<>();
        this.componenteById = new HashMap<>();
        this.esteInBlackout = false;
        this.tickCurent = 0;
        this.istoricEvenimente = new ArrayList<>();
    }

    public boolean adaugaConsumator(ConsumatorEnergie consumator) {
        if (!componenteById.containsKey(consumator.getId())) {
            consumatori.add(consumator);
            componenteById.put(consumator.getId(), consumator);
            return true;
        }

        return false;
    }

    public boolean adaugaProducator(ProducatorEnergie producator) {
        if (!componenteById.containsKey(producator.getId())) {
            producatori.add(producator);
            componenteById.put(producator.getId(), producator);
            return true;
        }

        return false;
    }

    public boolean adaugaBaterie(Baterie baterie) {
        if (!componenteById.containsKey(baterie.getId())) {
            baterii.add(baterie);
            componenteById.put(baterie.getId(), baterie);
            return true;
        }

        return false;
    }

    public ComponentaRetea getComponenta(String id) {
        return componenteById.get(id);
    }

    public boolean isEmpty() {
        return componenteById.isEmpty();
    }

    public boolean isEsteInBlackout() {
        return esteInBlackout;
    }

    public void simuleazaTick(double factorSoare, double factorVant) {
        double productieTotala = 0;
        double cerereTotala = 0;
        List<ConsumatorEnergie> consumatoriSortati = new ArrayList<>(consumatori);

        tickCurent++;

        for (ConsumatorEnergie consumator : consumatori) {
            consumator.cupleazaLaRetea();
        }

        for (ProducatorEnergie producator : producatori) {
            if (producator instanceof PanouSolar) {
                productieTotala += producator.calculeazaProductie(factorSoare);
            } else if (producator instanceof TurbinaEoliana) {
                productieTotala += producator.calculeazaProductie(factorVant);
            } else {
                productieTotala += producator.calculeazaProductie(0);
            }
        }

        for (ConsumatorEnergie consumator : consumatori) {
            cerereTotala += consumator.getCerereCurenta();
        }

        double delta = productieTotala - cerereTotala;

        if (delta > 0) {
            for (Baterie baterie : baterii) {
                delta = baterie.incarca(delta);

                if (delta == 0) {
                    break;
                }
            }
        } else if (delta < 0) {
            double deficit = -delta;
            for (Baterie baterie : baterii) {
                double energiaFurnizata = baterie.descarca(deficit);
                deficit -= energiaFurnizata;

                if (deficit == 0) {
                    break;
                }
            }

            if (deficit > 0) {
                consumatoriSortati.sort((c1, c2) -> Integer.compare(c2.getPrioritate(), c1.getPrioritate()));

                List<String> decuplati = new ArrayList<>();

                for (ConsumatorEnergie consumator : consumatoriSortati) {
                    if (consumator.getPrioritate() > 1 && deficit > 0) {
                        if (consumator.isEsteAlimentat() && consumator.isStatusOperational()) {
                            consumator.decupleazaDeLaRetea();
                            decuplati.add(consumator.getId());
                            deficit -= consumator.getCerereEnergie();
                        }
                    }
                }

                if (deficit > 0) {
                    double energieBaterii = 0;

                    esteInBlackout = true;

                    for (Baterie baterie : baterii) {
                        energieBaterii += baterie.getEnergieStocata();
                    }

                    System.out.printf("TICK: Productie %.2f, Cerere %.2f. Baterii: %.2f MW. Decuplati: %s\n",
                            productieTotala, cerereTotala, energieBaterii, decuplati);
                    System.out.println("BLACKOUT! SIMULARE OPRITA.");
                    istoricEvenimente.add("Tick " + tickCurent + ": BLACKOUT! SIMULARE OPRITA.");
                    return;
                }
            }
        }

        List<ConsumatorEnergie> consumatoriSortatiAfisare = new ArrayList<>(consumatori);
        consumatoriSortatiAfisare.sort((c1, c2) -> Integer.compare(c2.getPrioritate(), c1.getPrioritate()));

        List<String> decuplati = new ArrayList<>();
        for (ConsumatorEnergie consumator : consumatoriSortatiAfisare) {
            if (!consumator.isEsteAlimentat()) {
                decuplati.add(consumator.getId());
            }
        }

        double energieBaterii = 0;
        for (Baterie baterie : baterii) {
            energieBaterii += baterie.getEnergieStocata();
        }

        System.out.printf("TICK: Productie %.2f, Cerere %.2f. Baterii: %.2f MW. Decuplati: %s\n",
                productieTotala, cerereTotala, energieBaterii, decuplati);

        if (!decuplati.isEmpty()) {
            istoricEvenimente.add("Tick " + tickCurent + ": Deficit - Decuplat " + String.join(", ", decuplati));
        }
    }

    public void afiseazaStatusGrid() {
        if (isEmpty()) {
            System.out.println("Reteaua este goala");
            return;
        } else if (esteInBlackout) {
            System.out.println("Stare Retea: BLACKOUT");
        } else {
            System.out.println("Stare Retea: STABILA");
        }

        for (ProducatorEnergie producator : producatori) {
            String tip = producator.getClass().getSimpleName();
            String status;

            if (producator.isStatusOperational()) {
                status = "Operational";
            } else {
                status = "Defect";
            }

            System.out.printf("Producator %s (%s) - PutereBaza: %.2f - Status: %s\n",
                    producator.getId(), tip, producator.getPutere(), status);
        }

        for (ConsumatorEnergie consumator : consumatori) {
            String tip = consumator.getClass().getSimpleName();
            String status;

            if (consumator.isStatusOperational()) {
                if (consumator.isEsteAlimentat()) {
                    status = "Alimentat";
                } else {
                    status = "Decuplat";
                }
            } else {
                status = "Defect";
            }
            System.out.printf("Consumator %s (%s) - Cerere: %.2f - Prioritate: %d - Status: %s\n",
                    consumator.getId(), tip, consumator.getCerereEnergie(), consumator.getPrioritate(), status);
        }

        for (Baterie baterie : baterii) {
            String status;

            if (baterie.isStatusOperational()) {
                status = "Operational";
            } else {
                status = "Defect";
            }

            System.out.printf("Baterie %s - Stocare: %.2f/%.2f - Status: %s\n",
                    baterie.getId(), baterie.getEnergieStocata(), baterie.getCapacitateMaxima(), status);
        }
    }

    public void afiseazaIstoricEvenimente() {
        if (!istoricEvenimente.isEmpty()) {
            for (String eveniment : istoricEvenimente) {
                System.out.println(eveniment);
            }
        } else {
            System.out.println("Istoric evenimente gol");
        }
    }
}
