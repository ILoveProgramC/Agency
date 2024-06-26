package Agencija;

public class Smjestaj {
    private int id;
    private String naziv;
    private String broj_zvjezdica;
    private String vrsta_sobe;
    private String cijena_po_nocenju;

    public Smjestaj(int id,String naziv,String broj_zvjezdica,String vrsta_sobe,String cijena_po_nocenju){
        this.id=id;
        this.naziv=naziv;
        this.broj_zvjezdica=broj_zvjezdica;
        this.vrsta_sobe=vrsta_sobe;
        this.cijena_po_nocenju=cijena_po_nocenju;
    }

    public int getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getBroj_zvjezdica() {
        return broj_zvjezdica;
    }

    public String getVrsta_sobe() {
        return vrsta_sobe;
    }

    public String getCijena_po_nocenju() {
        return cijena_po_nocenju;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "Smjestaj{" +
                "id=" + id +
                ", naziv='" + naziv + '\'' +
                ", broj_zvjezdica='" + broj_zvjezdica + '\'' +
                ", vrsta_sobe='" + vrsta_sobe + '\'' +
                ", cijena_po_nocenju='" + cijena_po_nocenju + '\'' +
                '}';
    }
}
