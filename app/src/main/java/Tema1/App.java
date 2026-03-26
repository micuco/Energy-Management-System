package Tema1;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.text.*;

public class App {
    private Scanner scanner;
    private GridController gridController;

    public App(InputStream input) {
        this.scanner = new Scanner(input);
        this.gridController = new GridController();
    }

    public void run() {
        while (scanner.hasNextLine()) {
            String linie = scanner.nextLine().trim();
            String[] parti = linie.split("\\s+");

            if (linie.isEmpty()) {
                continue;
            }

            try {
                int comanda = Integer.parseInt(parti[0]);
                tipComanda(comanda, parti);
            } catch (NumberFormatException e) {
                System.out.println("EROARE: Comanda necunoscuta.");
            }
        }
    }

    private void tipComanda(int comanda, String[] parti) {
        if (gridController.isEsteInBlackout()) {
            if (comanda != 5 && comanda != 6 && comanda != 7) {
                System.out.println("EROARE: Reteaua este in BLACKOUT. Simulare oprita.");
                return;
            }
        }

        if (comanda == 0) {
            adaugaProducator(parti);
        } else if (comanda == 1) {
            adaugaConsumator(parti);
        } else if (comanda == 2) {
            adaugaBaterie(parti);
        } else if (comanda == 3) {
            nextTick(parti);
        } else if (comanda == 4) {
            setDefect(parti);
        } else if (comanda == 5) {
            gridController.afiseazaStatusGrid();
        } else if (comanda == 6) {
            gridController.afiseazaIstoricEvenimente();
        } else if (comanda == 7) {
            System.out.println("Simulatorul se inchide.");
        } else {
            System.out.println("EROARE: Comanda necunoscuta.");
        }


    }

    private void adaugaProducator(String[] parti) {
        double putere;
        String tip = parti[1];
        String id = parti[2];
        ProducatorEnergie producator = null;

        if (parti.length < 4) {
            System.out.println("EROARE: Format comanda invalid");
            return;
        }

        try {
            putere = Double.parseDouble(parti[3]);
        } catch (NumberFormatException e) {
            System.out.println("EROARE: Putere invalida");
            return;
        }

        if (putere <= 0) {
            System.out.println("EROARE: Putere invalida");
            return;
        }

        if (tip.equals("solar")) {
            producator = new PanouSolar(id, putere);
        } else if (tip.equals("turbina")) {
            producator = new TurbinaEoliana(id, putere);
        } else if (tip.equals("reactor")) {
            producator = new ReactorNuclear(id, putere);
        } else {
            System.out.println("EROARE: Tip producator invalid");
            return;
        }

        if (gridController.adaugaProducator(producator)) {
            System.out.println("S-a adaugat producatorul " + id + " de tip " + tip);
        } else {
            System.out.println("EROARE: Exista deja o componenta cu id-ul " + id);
        }
    }

    private void adaugaConsumator(String[] parti) {
        double cerere;
        String tip = parti[1];
        String id = parti[2];
        ConsumatorEnergie consumator = null;

        if (parti.length < 4) {
            System.out.println("EROARE: Format comanda invalid");
            return;
        }

        try {
            cerere = Double.parseDouble(parti[3]);
        } catch (NumberFormatException e) {
            System.out.println("EROARE: Cerere putere invalida");
            return;
        }

        if (cerere <= 0) {
            System.out.println("EROARE: Cerere putere invalida");
            return;
        }

        if (tip.equals("suport_viata")) {
            consumator = new SistemSuportViata(id, cerere);
        } else if (tip.equals("laborator")) {
            consumator = new LaboratorStiintific(id, cerere);
        } else if (tip.equals("iluminat")) {
            consumator = new SistemIluminat(id, cerere);
        } else {
            System.out.println("EROARE: Tip consumator invalid");
            return;
        }

        if (gridController.adaugaConsumator(consumator)) {
            System.out.println("S-a adaugat consumatorul " + id + " de tip " + tip);
        } else {
            System.out.println("EROARE: Exista deja o componenta cu id-ul " + id);
        }
    }

    private void adaugaBaterie(String[] parti) {
        double capacitate;
        String id = parti[1];


        if (parti.length < 3) {
            System.out.println("EROARE: Format comanda invalid");
            return;
        }

        try {
            capacitate = Double.parseDouble(parti[2]);
        } catch (NumberFormatException e) {
            System.out.println("EROARE: Capacitate invalida");
            return;
        }

        if (capacitate <= 0) {
            System.out.println("EROARE: Capacitate invalida");
            return;
        }
        Baterie baterie = new Baterie(id, capacitate);

        if (gridController.adaugaBaterie(baterie)) {
            System.out.println("S-a adaugat bateria " + id + " cu capacitatea " + (int)capacitate);
        } else {
            System.out.println("EROARE: Exista deja o componenta cu id-ul " + id);
        }
    }

    private void nextTick(String[] parti) {
        double factorSoare;
        double factorVant;

        if (parti.length < 3) {
            System.out.println("EROARE: Format comanda invalid");
            return;
        }

        try {
            factorSoare = Double.parseDouble(parti[1]);
            factorVant = Double.parseDouble(parti[2]);
        } catch (NumberFormatException e) {
            System.out.println("EROARE: Factori invalizi");
            return;
        }

        gridController.simuleazaTick(factorSoare, factorVant);
    }

    private void setDefect(String[] parti) {
        String id = parti[1];
        String statusInit = parti[2];
        ComponentaRetea componenta = gridController.getComponenta(id);
        boolean status;

        if (parti.length < 3) {
            System.out.println("EROARE: Format comanda invalid");
            return;
        }

        if (componenta == null) {
            System.out.println("EROARE: Nu exista componenta cu id-ul " + id);
            return;
        }

        if (statusInit.equals("true")) {
            status = true;
        } else if (statusInit.equals("false")) {
            status = false;
        } else {
            System.out.println("EROARE: Status invalid");
            return;
        }

        componenta.setStatusOperational(status);

        if (status) {
            System.out.println("Componenta " + id + " este acum operationala");
        } else {
            System.out.println("Componenta " + id + " este acum defecta");
        }
    }

    public static void main(String[] args) {
        App app = new App(System.in);
        app.run();
    }
}