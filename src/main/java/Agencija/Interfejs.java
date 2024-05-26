package Agencija;

import java.util.Set;

public interface Interfejs {

    String ispis_u_listu(Client c, int broj);

    void setAgencyBalance();

    void setRezervacija(String naziv, Set<String> lista);

    double dohvatiStanjeNovca(String jmbg);
}
