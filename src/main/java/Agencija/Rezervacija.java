package Agencija;

public class Rezervacija {
    private int client_id;
    private int aranzmanId;
    private double ukupna_cijena;
    private String placena_cijena;

    public Rezervacija(int client_id, int aranzmanId, double ukupna_cijena, String placena_cijena) {
        this.client_id = client_id;
        this.aranzmanId = aranzmanId;
        this.ukupna_cijena = ukupna_cijena;
        this.placena_cijena = placena_cijena;
    }

    public int getClient_id() {
        return client_id;
    }

    public int getAranzmanId() {
        return aranzmanId;
    }


    public double getUkupna_cijena() {
        return ukupna_cijena;
    }


    public String getPlacena_cijena() {
        return placena_cijena;
    }

    public void setPlacena_cijena(String placena_cijena) {
        this.placena_cijena = placena_cijena;
    }
}
