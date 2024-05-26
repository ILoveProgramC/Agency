package Agencija;

public enum Prijevoz {

    AUTOBUS("Autobus"),
    AUTOMOBIL("Samostalan"),
    AVION("Avion");

    private final String tip;

    Prijevoz(String tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return tip;
    }
}
