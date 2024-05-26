package Agencija;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static String DB_user = "root";
    private static String DB_password = "";
    private static String connectionUrl;
    private static int port = 3306;
    private static String DB_name = "agencija";
    private static Connection connection;
    static int id;

    //POKRETANJE KONEKCIJE
    public Database() {
        try {
            DBConnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void DBConnect() throws SQLException /*, ClassNotFoundException*/ {
        //Class.forName("com.mysql.cj.jdbc.Driver");
        connectionUrl = "jdbc:mysql://localhost" + ":" + port + "/" + DB_name;
        connection = DriverManager.getConnection(connectionUrl, DB_user, DB_password);
    }

    //UZIMANJE POSLEDNJEG DODANOG KLIJENTA U BAZI
    public Client getLastAddedClient() throws SQLException {
        Client lastAddedClient = null;

        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "SELECT * FROM klijent ORDER BY id DESC LIMIT 1";
            ResultSet resultSet = statement.executeQuery(SQLQuery);

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("ime");
                String lname = resultSet.getString("prezime");
                String phone = resultSet.getString("broj_telefona");
                String JMBG = resultSet.getString("jmbg");
                String accNUM = resultSet.getString("broj_racuna");
                String user = resultSet.getString("korisnicko_ime");
                String password = resultSet.getString("lozinka");

                lastAddedClient = new Client(id, name, lname, phone, JMBG, accNUM, user, password);


            }
            else {
                lastAddedClient = new Client(0, "", "", "", "", "", "", "");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return lastAddedClient;
    }

    //LOGIN da li je klijent ili admin u pitanju
    public String[] login(String username, String password) throws SQLException {
        String[] result = null;

        try {
            connection = DriverManager.getConnection(connectionUrl, DB_user, DB_password);
            // da li je korisnik admin
            String adminQuery = "SELECT * FROM admin WHERE korisnicko_ime = ?";
            PreparedStatement adminStatement = connection.prepareStatement(adminQuery);
            adminStatement.setString(1, username);
            ResultSet resultSetAdmin = adminStatement.executeQuery();

            // da li je korisnik klijent
            String clientQuery = "SELECT * FROM klijent WHERE korisnicko_ime = ?";
            PreparedStatement clientStatement = connection.prepareStatement(clientQuery);
            clientStatement.setString(1, username);
            ResultSet resultSetClient = clientStatement.executeQuery();

            if (resultSetAdmin.next()) {
                if (resultSetAdmin.getString("lozinka").equals(password)) {
                    String ime = resultSetAdmin.getString("ime");
                    String prezime = resultSetAdmin.getString("prezime");
                    result = new String[] {"admin", username, ime, prezime};
                } else {
                    // Ako je sifra netačna
                    return null;
                }
            } else if (resultSetClient.next()) {
                if (resultSetClient.getString("lozinka").equals(password)) {
                    String id = resultSetClient.getString("id");
                    String ime = resultSetClient.getString("ime");
                    String prezime = resultSetClient.getString("prezime");
                    String jmbg = resultSetClient.getString("jmbg");
                    String lozinka = resultSetClient.getString("lozinka");
                    result = new String[] {"user", username,ime,prezime,jmbg,lozinka,id};
                } else {
                    // Ako je sifra netačna
                    return null;
                }
            } else {
                // Ako korisnik nije pronađen ni u jednoj tabeli
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    //REGISTRACIJA KLIJENTA
    public void register(Client client) throws SQLException{
        try {
            connection = DriverManager.getConnection(connectionUrl, DB_user, DB_password);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO klijent (id, ime, prezime, broj_telefona, jmbg, broj_racuna, korisnicko_ime, lozinka) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            id = client.getId()+1;
            preparedStatement.setInt(1, client.getId());
            preparedStatement.setString(2, client.getName());
            preparedStatement.setString(3, client.getLname());
            preparedStatement.setString(4, client.getPhone());
            preparedStatement.setString(5, client.getJMBG());
            preparedStatement.setString(6, client.getAccNUM());
            preparedStatement.setString(7, client.getUser());
            preparedStatement.setString(8, client.getPassword());

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //DA LI JE JMBG VEC REGISTROVAN U BAZI klijent
    public boolean isJMBGExists(String jmbg) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "SELECT * FROM klijent WHERE jmbg = '" + jmbg + "'";
            ResultSet resultSet = statement.executeQuery(SQLQuery);

            return resultSet.next();  // Ako resultSet ima rezultat, JMBG već postoji
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    //DA LI JE BANKOVNI RACUN VEC REGISTROVAN U BAZI klijent
    public boolean isAccountExists(String account) throws SQLException {
        try {
            // Provera da li postoji u tabeli 'klijent'
            Statement clientStatement = connection.createStatement();
            String clientSQLQuery = "SELECT * FROM klijent WHERE broj_racuna = '" + account + "'";
            ResultSet clientResultSet = clientStatement.executeQuery(clientSQLQuery);

            return clientResultSet.next();  // Vraća true ako postoji u tabeli 'klijent'
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    //POTPUNA VALIDACIJA BANKOVNOG RACUNA I JMBG
    public String validateJMBGAndAccount(String jmbg, String account) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "SELECT * FROM bankovni_racun WHERE jmbg = '" + jmbg + "' AND broj_racuna = '" + account + "'";
            ResultSet resultSet = statement.executeQuery(SQLQuery);

            if (resultSet.next()) {
                // Ako kombinacija JMBG-a i broja računa postoji
                return "Validno";
            } else {
                // Ako kombinacija JMBG-a i broja računa ne postoji
                return "Nevažeća kombinacija JMBG-a i bankovnog računa.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }



    //UKUPAN BROJ KLIJENATA U BAZI
    public int getClientCount() throws SQLException {
        int clientCount = 0;

        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "SELECT COUNT(*) FROM klijent";
            ResultSet resultSet = statement.executeQuery(SQLQuery);

            if (resultSet.next()) {
                clientCount = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return clientCount;
    }

    //UKUPAN BROJ ADMINA U BAZI
    public int getAdminCount() throws SQLException {
        int admintCount = 0;

        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "SELECT COUNT(*) FROM admin";
            ResultSet resultSet = statement.executeQuery(SQLQuery);

            if (resultSet.next()) {
                admintCount = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return admintCount;
    }

    //DODAVANJE ADMINA
    public void addAdmin(Admin admin) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(connectionUrl, DB_user, DB_password);
            if (isAdminExists(admin.getUsername())) {
                return;
            }

            Admin lastAddedAdmin = getLastAddedAdmin();
            int newID = lastAddedAdmin.getId()+1;

            String query = "INSERT INTO admin (id, ime, prezime, korisnicko_ime, lozinka) VALUES (?, ?, ?, ?, ?)";
            id = admin.getId()+1;
            statement = connection.prepareStatement(query);
            statement.setInt(1, newID);
            statement.setString(2, admin.getName());
            statement.setString(3, admin.getLastName());
            statement.setString(4, admin.getUsername());
            statement.setString(5, admin.getPassword());

            statement.executeUpdate();
        } finally {
            //
        }
    }

    //PROVJERA DA LI ADMIN POSTOJI VEC U BAZI
    public boolean isAdminExists(String adminUsername) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(connectionUrl, DB_user, DB_password);
            String query = "SELECT COUNT(*) FROM admin WHERE korisnicko_ime = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, adminUsername);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }

            return false;
        } finally {
            //
        }
    }

    // Da li vec postoji klijent
    public boolean isClientExists(String clientUsername) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT COUNT(*) FROM klijent WHERE korisnicko_ime = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, clientUsername);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }

            return false;
        } finally {
            //
        }
    }

    //DOHVACANJE ZADNJEG DODANOG ADMINA U BAZI
    public Admin getLastAddedAdmin() throws SQLException {
        Admin lastAddedAdmin = null;

        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "SELECT * FROM admin ORDER BY id DESC LIMIT 1";
            ResultSet resultSet = statement.executeQuery(SQLQuery);

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("ime");
                String lname = resultSet.getString("prezime");
                String username = resultSet.getString("korisnicko_ime");

                lastAddedAdmin = new Admin(id, name, lname, username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return lastAddedAdmin;
    }

    // PROMJENA LOZINKE KLIJENTU
    public void changeClientPassword(String username, String newPassword) throws SQLException {
        String query = "UPDATE klijent SET lozinka = ? WHERE korisnicko_ime = ?";

        try{
             PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, username);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // PROMJENA LOZINKE ADMINU
    public void changeAdminPassword(String username, String newPassword) throws SQLException {
        String query = "UPDATE admin SET lozinka = ? WHERE korisnicko_ime = ?";

        try{
            connection = DriverManager.getConnection(connectionUrl, DB_user, DB_password);
             PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, username);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //UZIMANJE STANJA NA RACUNU OD KLIJENTA
    public double getClientBalance(String jmb) throws SQLException {
        double balance = 0;

        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "SELECT stanje FROM bankovni_racun WHERE jmbg = '" + jmb + "'";
            ResultSet resultSet = statement.executeQuery(SQLQuery);

            if (resultSet.next()) {
                balance = resultSet.getDouble("stanje");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return balance;
    }


    // Dodavanje putovanja
    public static void addTrip(int id, String nazivPutovanja, String destinacija, String prevoz, LocalDate datumPolaska, LocalDate datumDolaska, double cijenaAranzmana, Smjestaj smjestajId)
    {
        try {
            DBConnect();
            String SQLinsert = "INSERT INTO aranzman VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(SQLinsert)) {
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, nazivPutovanja);
                preparedStatement.setString(3, destinacija);
                preparedStatement.setString(4, prevoz);
                preparedStatement.setDate(5, Date.valueOf(datumPolaska));
                preparedStatement.setDate(6, Date.valueOf(datumDolaska));
                preparedStatement.setDouble(7, cijenaAranzmana);
                preparedStatement.setInt(8, smjestajId.getId());

                preparedStatement.executeUpdate();
                listaAranzmana().add(new Aranzman(id, nazivPutovanja, destinacija, prevoz, datumPolaska, datumDolaska, cijenaAranzmana, smjestajId));
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                //connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Uzimanje id poslednjeg dodanog putovanja (aranzmana)
    public int getLastAddedTripId() throws SQLException {
        int lastTripId = 0;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(connectionUrl, DB_user, DB_password);
            statement = connection.createStatement();

            // Izvršavanje SQL upita za dobijanje maksimalnog ID-a kao string
            String sql = "SELECT MAX(CAST(id AS UNSIGNED)) AS maxId FROM aranzman";
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                // Dobijanje maksimalnog ID-a kao string
                String maxIdAsString = resultSet.getString("maxId");

                // Konverzija stringa u integer
                if (maxIdAsString!=null)
                    lastTripId = Integer.parseInt(maxIdAsString) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Zatvaranje resursa
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        // Ako nema aranžmana u bazi, vraćamo 1 kao podrazumijevanu vrijednost ID-a
        return (lastTripId == 0) ? 1 : lastTripId;
    }

    //Uzimanje ID poslednjeg dodanog smjestaja
    public int getLastAddedAccomo() throws SQLException {
        int lastAccomoId = 0;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(connectionUrl, DB_user, DB_password);
            statement = connection.createStatement();
            String sql = "SELECT MAX(id) AS id FROM smjestaj";
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                lastAccomoId = resultSet.getInt("id")+1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        // Ako nema aranžmana u bazi, vraćamo 1 kao podrazumijevanu vrijednost ID-a
        return (lastAccomoId == 0) ? 1 : lastAccomoId;
    }

    //Lista smjestaja
    public static ArrayList<Smjestaj> listaSmjestaja() {
        ArrayList<Smjestaj> listaSmjestaja = new ArrayList<>();
        try {
            DBConnect();
            Statement statement = connection.createStatement();
            String SQLQuery = "SELECT * FROM smjestaj";
            ResultSet resultSet = statement.executeQuery(SQLQuery);

            while (resultSet.next()) {
                Smjestaj smjestaj = new Smjestaj(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5));
                listaSmjestaja.add(smjestaj);
            }
            statement.close();
            //connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return listaSmjestaja;
    }

    // Lista aranzmana
    public static ArrayList<Aranzman> listaAranzmana() {
        ArrayList<Aranzman> listaAranzmana = new ArrayList<>();
        try {
            DBConnect();
            Statement statement = connection.createStatement();
            String SQLQuery = "SELECT * FROM aranzman";
            ResultSet resultSet = statement.executeQuery(SQLQuery);
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String nazivPutovanja = resultSet.getString(2);
                String destinacija = resultSet.getString(3);
                String prevoz = resultSet.getString(4);
                LocalDate datumPolaska = resultSet.getDate(5).toLocalDate();
                LocalDate datumDolaska = resultSet.getDate(6).toLocalDate();
                double cijenaAranzmana = resultSet.getDouble(7);
                int smjestajId = resultSet.getInt(8);
                Smjestaj s = smjestaj_iz_id(smjestajId);
                Aranzman a = new Aranzman(id, nazivPutovanja, destinacija, prevoz, datumPolaska, datumDolaska, cijenaAranzmana, s);
                listaAranzmana.add(a);
            }
            statement.close();
            connection.close();
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return listaAranzmana;
    }

    //Lista rezervacija
    public static ArrayList<Rezervacija> listaRezervacija() {
        ArrayList<Rezervacija> listaRezervacija = new ArrayList<>();
        try {
            DBConnect();
            Statement statement = connection.createStatement();
            String SQLQuery = "SELECT * FROM rezervacija";
            ResultSet resultSet = statement.executeQuery(SQLQuery);
            while (resultSet.next()) {
                Rezervacija r = new Rezervacija(resultSet.getInt(1), resultSet.getInt(2), resultSet.getDouble(3),resultSet.getString(4));
                listaRezervacija.add(r);
            }
            statement.close();
            //connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return listaRezervacija;
    }


    //ID smjestaja
    public static Smjestaj smjestaj_iz_id(int smjestajId) throws SQLException, ClassNotFoundException {
        Smjestaj smjestaj = null;
        String sqlQuery = "SELECT * FROM smjestaj WHERE id = " + smjestajId;
        try {
            DBConnect();
            Statement statement = connection.createStatement();
            String SQLQuery = "SELECT * FROM smjestaj";
            ResultSet resultSet = statement.executeQuery(SQLQuery);

            if (resultSet.next()) {
                String naziv = resultSet.getString("naziv");
                String brojZvjezdica = resultSet.getString("broj_zvjezdica");
                String vrstaSobe = resultSet.getString("vrsta_sobe");
                String cijenaPoNocenju = resultSet.getString("cjena_po_nocenju");

                smjestaj = new Smjestaj(smjestajId, naziv, brojZvjezdica, vrstaSobe, cijenaPoNocenju);
            }
            //connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return smjestaj;
    }

    // Lista bankovnih racuna
    public static ArrayList<BankAcc> listaBankovnihRacuna(){
        ArrayList<BankAcc> BankList = new ArrayList<>();
        try {
            DBConnect();
            Statement statement = connection.createStatement();
            String SQLQuery = "SELECT * FROM bankovni_racun";
            ResultSet resultSet = statement.executeQuery(SQLQuery);
            while (resultSet.next()) {
                BankAcc acc = new BankAcc(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),Double.parseDouble(resultSet.getString(4)));
                BankList.add(acc);
            }
            statement.close();
            //connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return BankList;
    }

    //Lista klijenata

    public static ArrayList<Client> listaKlijenata(){
        ArrayList<Client> ClientList = new ArrayList<>();
        try {
            DBConnect();
            Statement statement = connection.createStatement();
            String SQLQuery = "SELECT * FROM klijent";

            ResultSet resultSet = statement.executeQuery(SQLQuery);
            resultSet = statement.executeQuery(SQLQuery);

            while (resultSet.next()) {
                Client client = new Client(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),resultSet.getString(8));
                ClientList.add(client);
            }


            statement.close();
            //connection.close();

        } catch (SQLException e){
            e.printStackTrace();

        }
        return ClientList;
    }

    //Lista klijenata trenutnog aranzmana
    public static List<Client> listaKlijenataZaAranzman(Aranzman aranzman) {
        List<Client> rezervisaniKlijenti = new ArrayList<>();

        for (Rezervacija rezervacija : listaRezervacija()) {
            if (rezervacija.getAranzmanId() == aranzman.getId()) {
                for(Client c: listaKlijenata()){
                    if(c.getId()==rezervacija.getClient_id()){
                        rezervisaniKlijenti.add(c);
                    }
                }
            }
        }

        return rezervisaniKlijenti;
    }

    //Otkazivanje trenutnog aranzmana
    public static void otkaziAranzman(int aranzman_id, int klijent_id) throws SQLException, ClassNotFoundException {
        DBConnect();
        Statement statement = connection.createStatement();
        String SQLupdate = "DELETE FROM rezervacija WHERE klijent_id=" + klijent_id + " AND aranzman_id=" + aranzman_id;
        statement.executeUpdate(SQLupdate);
        statement.close();
        for(Rezervacija r:listaRezervacija()){
            if(r.getAranzmanId()==aranzman_id && r.getClient_id()==klijent_id)
                listaRezervacija().remove(r);
        }
    }

    // dodavanje obrisanog araznmana koji je rezervisan od strane klijenta u obrisani_aranzmani
    public void dodajObrisaniAranzman(String klijentJMBG, String aranzmanIme) {
        try {
            String putanjaDatoteke = "obrisani_aranzmani.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(putanjaDatoteke, true));
            writer.write(klijentJMBG + " " + aranzmanIme);
            writer.newLine();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // uzimanje liste aranzmana koji se nalaze u tabeli obrisane_rezervacije
    public ArrayList<String> getObrisaniAranzmani(String jmbg) throws SQLException {
        ArrayList<String> obrisaniAranzmani = new ArrayList<>();
        try {
            DBConnect();
            PreparedStatement statement = connection.prepareStatement("SELECT aranzman_ime FROM obrisane_rezervacije WHERE klijent_jmbg = ?");
            statement.setString(1, jmbg);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                obrisaniAranzmani.add(resultSet.getString("aranzman_ime"));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obrisaniAranzmani;
    }

    // Brisanje aranzmana iz tabele obrisane_rezervacije
    public void ukloniObrisaniAranzman(String jmbg) throws SQLException {
        try {
            DBConnect();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM obrisane_rezervacije WHERE klijent_jmbg = ?");
            statement.setString(1, jmbg);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Brisanje aranzmana
    public static void obrisiAranzman(int aranzman_id) throws SQLException, ClassNotFoundException {
        DBConnect();
        Statement statement = connection.createStatement();
        String SQLupdate = "DELETE FROM aranzman WHERE id=" + aranzman_id;
        statement.executeUpdate(SQLupdate);
        statement.close();
        for(Aranzman a:listaAranzmana()){
            if(a.getId()==aranzman_id)
                listaAranzmana().remove(a);
        }
    }

    //Dodavanje izleta
    public static void addIzlet(int id, String nazivPutovanja, String destinacija, LocalDate datumPolaska, double cijenaAranzmana) {
        try {
            DBConnect();
            Statement statement = connection.createStatement();
            String SQLinsert = "INSERT INTO aranzman VALUES('"+id+"','"+nazivPutovanja+"','"+destinacija+"','Autobus','"+datumPolaska+"','"+datumPolaska+"','"+cijenaAranzmana+"',"+null+")";
            statement.executeUpdate(SQLinsert);
            statement.close();
            //connection.close();
            listaAranzmana().add(new Aranzman(id, nazivPutovanja, destinacija, "Autobus", datumPolaska, datumPolaska, cijenaAranzmana, null));
        } catch (SQLException e){
            e.printStackTrace();

        }
    }

    //Dodavanje smjestaja u bazu
    public static void addSmjestaj(int id,String naziv,String broj_zvjezdica,String vrsta_sobe,String cijena_po_nocenju){
        try {
            DBConnect();
            Statement statement = connection.createStatement();
            String SQLinsert = "INSERT INTO smjestaj VALUES(" + id + ",'" + naziv + "','"+broj_zvjezdica+"','"+vrsta_sobe+"','"+cijena_po_nocenju+"')";
            statement.executeUpdate(SQLinsert);
            statement.close();
            //connection.close();
            listaSmjestaja().add(new Smjestaj(id, naziv, broj_zvjezdica, vrsta_sobe, cijena_po_nocenju));
        } catch (SQLException e){
            e.printStackTrace();

        }
    }

    // Uzimanje stanje bankovnog racuna same agencije
    public static double getAgencyBalance() {
        try {
            DBConnect();
            String query = "SELECT stanje FROM bankovni_racun WHERE LENGTH(jmbg) = 10";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("stanje");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // ERROR
        return 0.0;
    }

    // Azuriranje placene cijene rezervacije
    public static void updatePlacenaRezervacija(int klijentId, int aranzmanId, double cijena) {
        try {
            DBConnect();

            String updateQuery = "UPDATE rezervacija SET placena_cijena = placena_cijena + ? WHERE Klijent_id = ? AND Aranzman_id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setDouble(1, cijena);
                updateStatement.setInt(2, klijentId);
                updateStatement.setInt(3, aranzmanId);
                updateStatement.executeUpdate();
            }

            for(Rezervacija r:listaRezervacija()){
                if(r.getClient_id()==klijentId && r.getAranzmanId()==aranzmanId){
                    r.setPlacena_cijena(String.valueOf(Double.parseDouble(r.getPlacena_cijena())+cijena));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateStanjeRacuna(String jmbg, double iznos1, int id2, double iznos2){
        try {
            DBConnect();
            Statement statement = connection.createStatement();
            String SQLupdate = "UPDATE bankovni_racun set stanje  = "+iznos1+" WHERE jmbg = "+jmbg;
            statement.executeUpdate(SQLupdate);
            String SQLupdate2 = "UPDATE bankovni_racun set stanje  = "+iznos2+" WHERE id = "+id2;
            statement.executeUpdate(SQLupdate2);
            statement.close();
            //connection.close();
            for (BankAcc b:listaBankovnihRacuna()) {
                if(b.getJmb().equals(jmbg)){
                    b.setBalance(iznos1);
                }
                if(b.getId()==id2){
                    b.setBalance(iznos2);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    //Dodavanje rezervacije u bazi
    public static void dodajRezervaciju(int klijentId,int aranzmanId,String ukupnaCijena,String placenaCijena) {
        try {
            DBConnect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT placena_cijena FROM rezervacija WHERE aranzman_id = " + aranzmanId + " AND placena_cijena = '-1' AND Klijent_id = " + klijentId);
            if (resultSet.next()) {
                String SQLUpdate = "UPDATE rezervacija SET placena_cijena = '" + placenaCijena + "' WHERE aranzman_id = " + aranzmanId + " AND Klijent_id = " + klijentId;
                statement.executeUpdate(SQLUpdate);
                return;
            }

            String SQLinsert = "INSERT INTO rezervacija VALUES(" + klijentId + ",'" + aranzmanId + "','"+ukupnaCijena+"','"+placenaCijena+"')";
            statement.executeUpdate(SQLinsert);
            statement.close();
            connection.close();
            listaRezervacija().add(new Rezervacija(klijentId, aranzmanId, Double.parseDouble(ukupnaCijena), placenaCijena));
        } catch (SQLException e){
            e.printStackTrace();

        }
    }

    public static void otkaziRezervaciju(int klijentId, int aranzmanId) {
        try {
            DBConnect();
            Statement statement = connection.createStatement();
            String SQLupdate = "UPDATE rezervacija SET placena_cijena = -1 WHERE klijent_id=" + klijentId + " AND aranzman_id=" + aranzmanId;
            statement.executeUpdate(SQLupdate);
            statement.close();
            //connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
