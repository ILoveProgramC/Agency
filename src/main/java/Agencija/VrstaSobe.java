package Agencija;

public enum VrstaSobe {

    JEDNOKREVETNA("Jednokrevetna"),
    DVOKREVETNA("Dvokrevetna"),
    TROKREVETNA("Trokrevetna"),
    APARTMAN("Apartman");

    private final String vrsta;

    VrstaSobe(String vrsta) {
        this.vrsta = vrsta;
    }

    @Override
    public String toString() {
        return vrsta;
    }

}

