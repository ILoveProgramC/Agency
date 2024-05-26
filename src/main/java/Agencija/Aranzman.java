package Agencija;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Aranzman {

    private int id;
    private String putovanje;
    private String destinacija;
    private String transport;
    private LocalDate odlazakDate;
    private LocalDate dolazakDate;
    private double ukupna_cijena;
    private double cijenaAranzmana;
    private int broj_dana;

    private Smjestaj smjestajId;


    public Aranzman(int id, String putovanje, String destincija, String transport, LocalDate odlazakDate, LocalDate dolazakDate, double cijenaAranzmana, Smjestaj smjestajId) {
        this.id = id;
        this.putovanje = putovanje;
        this.destinacija = destincija;
        this.transport = transport;
        this.odlazakDate = odlazakDate;
        this.dolazakDate = dolazakDate;
        this.cijenaAranzmana = cijenaAranzmana;
        this.broj_dana = (int) ChronoUnit.DAYS.between(odlazakDate,dolazakDate);
        this.smjestajId = smjestajId;

        if(smjestajId!=null){
            this.ukupna_cijena=broj_dana*Double.parseDouble(smjestajId.getCijena_po_nocenju())+cijenaAranzmana;
        }else {
            this.ukupna_cijena=cijenaAranzmana;
        }
    }

    public Aranzman(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPutovanje() {
        return putovanje;
    }


    public String getDestinacija() {
        return destinacija;
    }


    public String getTransport() {
        return transport;
    }


    public LocalDate getOdlazakDate() {
        return odlazakDate;
    }


    public LocalDate getDolazakDate() {
        return dolazakDate;
    }


    public double getUkupna_cijena() {
        return ukupna_cijena;
    }


    public double getCijenaAranzmana() {
        return cijenaAranzmana;
    }


    public Smjestaj getSmjestajId() {
        return smjestajId;
    }


    @Override
    public String toString() {
        if(odlazakDate.equals(dolazakDate)){
            return putovanje+"  |  "+destinacija+"  |  "+ transport+"  |  "+odlazakDate+"  |  Ukupna cijena: "+ukupna_cijena;
        }
        return putovanje+"  |  "+destinacija+"  |  "+ transport+"  |  "+odlazakDate+"  |  "+ dolazakDate+"  |  Aranzman: " +cijenaAranzmana+"  |  Noćenje: "+smjestajId.getCijena_po_nocenju()+"  |  Ukupna cijena: "+ukupna_cijena;
    }
}
