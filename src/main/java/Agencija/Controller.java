package Agencija;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;


public class Controller implements Interfejs {

    private Database database = new Database();
    private Stage stage;
    private Scene scene;
    private Parent root;

    //FXML elementi
    @FXML
    private Button filterIzlet;
    @FXML
    private Button filterUkloni;

    @FXML
    private PasswordField pass;

    @FXML
    private TextField user;

    @FXML
    private Label status;

    @FXML
    private TextField acc;

    @FXML
    private TextField jmb;

    @FXML
    private TextField lname;

    @FXML
    private TextField name;

    @FXML
    private PasswordField pass_conf;

    @FXML
    private PasswordField pass_reg;

    @FXML
    private TextField phone;

    @FXML
    private Label status_reg;

    @FXML
    private TextField user_reg;

    @FXML
    private TextField admin_lname;

    @FXML
    private TextField admin_name;

    @FXML
    private TextField admin_user;

    @FXML
    private Label admin_status;
    @FXML
    private Label change_status;

    @FXML
    private PasswordField newpass_conf;

    @FXML
    private PasswordField newpass;

    @FXML
    private Text admin_ukupno = new Text(); //AKO SE NE INICIJALIZUJE OVAKO, VRACA NULL

    @FXML
    private Text ukupno_users = new Text(); //AKO SE NE INICIJALIZUJE OVAKO, VRACA NULL

    @FXML
    private Text client_name = new Text();  //AKO SE NE INICIJALIZUJE OVAKO, VRACA NULL

    @FXML
    private Text client_user = new Text();   // AKO SE NE INICIJALIZUJE OVAKO, VRACA NULL
    @FXML
    private Text admin_ime = new Text();  // AKO SE NE INICIJALIZUJE OVAKO, VRACA NULL
    @FXML
    private Text balans = new Text(); //AKO SE NE INICIJALIZUJE OVAKO, VRACA NULL

    @FXML
    private Text admin_username = new Text(); // AKO SE NE INICIJALIZUJE OVAKO, VRACA NULL

    @FXML
    private TabPane izletiTable;

    @FXML
    private TabPane rezervacijeTable;
    @FXML
    private Text agency_balance = new Text();

    @FXML
    private Text arange_potroseno = new Text();

    @FXML
    private Text arange_potroseno_text;

    @FXML
    private Text doplata_text;

    @FXML
    private Text doplata = new Text();

    @FXML
    private Text iznosDoplate_text;

    @FXML
    private TextField iznos_doplate;

    @FXML
    private ListView<String> lista_klijenata = new ListView<>();
    @FXML
    private ListView<Client> admin_lista_klijenata = new ListView<>();

    @FXML
    private ListView<String> jednodnevna_lista = new ListView<>(), visednevna_lista = new ListView<>();

    @FXML
    private ListView<String> klijent_aktivne = new ListView<>();

    @FXML
    private ListView<String> klijent_protekle = new ListView<>();

    @FXML
    private ListView<String> klijent_otkazane = new ListView<>();
    @FXML
    private ListView<Aranzman> a_klijent_aktivne = new ListView<>();
    @FXML
    private ListView<Aranzman> a_klijent_protekle = new ListView<>();
    @FXML
    private ListView<Aranzman> a_klijent_otkazane = new ListView<>();
    @FXML
    private ListView<Aranzman> jednoAranzman = new ListView<>();
    @FXML
    private ListView<Aranzman> viseAranzman = new ListView<>();

    @FXML private Label naziv_aranzmana = new Label();

    @FXML
    private TextField AccomoPrice;

    @FXML
    private DatePicker Accomo_departureDate;

    @FXML
    private TextField Accomo_destName;

    @FXML
    private TextField accomoName;

    @FXML
    private TextField arangePrice;

    @FXML
    private DatePicker backDate;

    @FXML
    private DatePicker departureDate;

    @FXML
    private TextField destName;

    @FXML
    private TextField izletName;

    @FXML
    private TextField nightPrice;

    @FXML
    private ComboBox<VrstaSobe> roomType = new ComboBox<>();

    @FXML
    private Button doplata_button;

    @FXML
    private Button otkazi_button;

    @FXML
    private ComboBox<Integer> stars = new ComboBox<>();

    @FXML
    private ComboBox<Prijevoz> transportName = new ComboBox<>();

    @FXML
    private Spinner<Integer> cijenaBroj = new Spinner<>();

    @FXML
    private TextField tripName;

    @FXML
    private Label izletGreska = new Label();

    @FXML
    private Label tripstatus;

   //Staticke i nestaticke promjenljive

    private static String admin_pass;   //U poredjenju sa novom sifrom
   private static String ime_prezime;
   private static String klijent_password;
   private static String static_user;
   private static String user_type;
   private static double balance;
   private static int client_id;
   private static String jmbg;
   private static String s_jmbg;
   private boolean isReservationOpen = false;
   private boolean isIzletOpen = false;
   private boolean isTripOpen = false;
   private boolean isAddAdminOpen = false;
   private boolean isPassOpen = false;
   private static boolean deadline = false;
   private static boolean deadline2 = false;
   private static int indeksRez;
   private static String s_lista = "";
   private static boolean izlet_admin;
   private static boolean putovanje_admin;
   private static Stage currentAdminStage;
   private static String s_naziv_aranzmana = "";
   private static String s_istekao_aranzman = "";
   private static Set<String> lista = new HashSet<>();

   ButtonType buttonTypeOk = new ButtonType("Uredu", ButtonBar.ButtonData.OK_DONE);
   ButtonType buttonTypeCancel = new ButtonType("Nazad", ButtonBar.ButtonData.CANCEL_CLOSE);





    @FXML
    public void initialize() throws SQLException{ //Inicijalizacija vrijednosti
        client_name.setText(ime_prezime);
        client_user.setText(static_user);
        admin_username.setText(static_user);
        admin_ime.setText(ime_prezime);
        s_jmbg = jmbg;
        ukupno_users.setText(String.valueOf(database.getClientCount()));
        admin_ukupno.setText(String.valueOf(database.getAdminCount()));
        DecimalFormat df = new DecimalFormat("#.##");
        balans.setText(df.format(balance).replace(",", "."));
        roomType.getItems().addAll(VrstaSobe.values());
        transportName.getItems().addAll(Prijevoz.values());
        stars.getItems().addAll(1, 2, 3, 4, 5);
        cijenaBrojacOnlyNumbers();
        setAgencyBalance();
        postavi_rezervacije();
        postavi_aranzmane();
        setRezervacija(s_naziv_aranzmana,lista);


    }


    // Namjestanje brojaca za cijenu u filteru
    private void cijenaBrojacOnlyNumbers(){
        Pattern pattern = Pattern.compile("\\d*");
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (pattern.matcher(change.getControlNewText()).matches()) {
                return change;
            } else {
                return null;
            }
        };

        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), 0, filter);
        cijenaBroj.getEditor().setTextFormatter(textFormatter);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 1);
        cijenaBroj.setValueFactory(valueFactory);


            cijenaBroj.setOnScroll(event -> {
                if (cijenaBroj.getValue() == null) {
                    cijenaBroj.getValueFactory().setValue(1);
                } else {
                    if (event.getDeltaY() > 0) {
                        cijenaBroj.increment(1);
                    } else {
                        cijenaBroj.decrement(1);
                    }
                }
            });
        }


    // Registracija
    @FXML
    void register(ActionEvent event) throws SQLException {
        String ime = name.getText();
        String prezime = lname.getText();
        String jmbg = jmb.getText();
        String telefon = phone.getText();
        String racun = acc.getText();
        String username = user_reg.getText();
        String password = pass_reg.getText();
        String password_conf = pass_conf.getText();

        if (username.equalsIgnoreCase("") || password.equalsIgnoreCase("") || ime.equalsIgnoreCase("") || prezime.equalsIgnoreCase("") || jmbg.equalsIgnoreCase("") || telefon.equalsIgnoreCase("") || racun.equalsIgnoreCase("") || password_conf.equalsIgnoreCase("")){
            status_reg.setText("Popunite sva polja!");
            status_reg.setOpacity(1);
            return;
        }
        // Validacija JMBG
        if (jmbg.length() != 13 || !jmbg.matches("\\d+")) {
            status_reg.setText("JMBG mora imati 13 cifara.");
            status_reg.setOpacity(1);
            return;
        }
        if (!telefon.matches("\\d+") || telefon.length()<6){
            status_reg.setText("Nevazeci broj telefona");
            status_reg.setOpacity(1);
            return;
        }
        if (database.isClientExists(username)){
            status_reg.setText("Klijent sa korisnickim imenom vec postoji.");
            status_reg.setOpacity(1);
            return;
        }

        // Provera da li JMBG već postoji u bazi
        if (database.isJMBGExists(jmbg)) {
            status_reg.setText("JMBG već postoji. Unesite validan JMBG.");
            status_reg.setOpacity(1);
            return;
        }

        // Validacija bankovnog računa
        if (database.isAccountExists(racun)) {
            status_reg.setText("Bankovni račun već postoji. Unesite validan broj računa.");
            status_reg.setOpacity(1);
            return;
        }

        // Validacija lozinke
        if (!password.equals(password_conf)) {
            status_reg.setText("Lozinke se ne poklapaju. Unesite iste lozinke.");
            status_reg.setOpacity(1);
            return;
        }

        String validationMessage = database.validateJMBGAndAccount(jmbg, racun);

        if (!validationMessage.equals("Validno")) {
            status_reg.setText(validationMessage);
            status_reg.setOpacity(1);
            return;
        }
        else{
            Client lastAddedClient = database.getLastAddedClient();
            Client client = new Client(lastAddedClient.getId() + 1, ime, prezime, telefon, jmbg, racun, username, password);
            try {
                database.register(client);
                switchToScene1(event);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        }


        // Login metoda
    @FXML
    void login(ActionEvent event) {
        String username = user.getText();
        String password = pass.getText();

        if (username.equalsIgnoreCase("") || password.equalsIgnoreCase("")){
            status.setText("Popunite sva polja!");
            status.setOpacity(1);
        } else {
            try {
                String[] result = database.login(username, password);
                if (result != null) {

                    if (result[0].equals("admin")) {
                        user_type = result[0];
                        static_user = result[1];
                        ime_prezime = result[2] + " " + result[3];
                        admin_pass = password;
                        ukupno_users.setText(String.valueOf(database.getClientCount()));

                        switchToScene4(event);
                        if (password.equals("12345678"))
                            changePassWindow(event);

                    } else if (result[0].equals("user")) {
                        user_type = result[0];
                        static_user = result[1];
                        ime_prezime = result[2] + " " + result[3];
                        jmbg = result[4];
                        s_jmbg = jmbg;
                        klijent_password = result[5];
                        client_id = Integer.parseInt(result[6]);
                        balance = database.getClientBalance(jmbg);
                        switchToScene3(event);
                         if (deadline){
                             notification(s_lista, Alert.AlertType.WARNING);
                         }
                         if (deadline2){
                             notification("Rok za uplatu rezervacije sljedećeg aranžmana: \n"+ s_istekao_aranzman +"\nje istekao, niste skroz doplatili rezervaciju te je agencija" +
                                     " zadržala 50% cijene aranžmana, dodatne uplate od strane Vas je refundiran", Alert.AlertType.INFORMATION);
                         }
                        try {
                            boolean flag = false;
                            File file = new File("obrisani_aranzmani.txt");
                            if (file.exists()) {
                                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                                    String line;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("Admin agencije je otkazao i obrisao sljedeći aranžman:\n");
                                    while ((line = reader.readLine()) != null) {
                                        String[] parts = line.split(" ");
                                        if (parts[0].equals(s_jmbg)) {
                                            flag = true;
                                            // Dodaj naziv aranžmana u StringBuilder
                                            sb.append("- ").append(parts[1]).append("\n");
                                        }
                                    }
                                    sb.append("\nNovac je refundiran!");
                                    if (flag) {
                                        // Ispis notifikacije s JMBG-om i nazivom aranžmana
                                        notification(sb.toString(), Alert.AlertType.INFORMATION);

                                        // Uklanjanje svih linija s JMBG-om iz datoteke
                                        List<String> lines = Files.readAllLines(file.toPath());
                                        lines.removeIf(l -> l.startsWith(s_jmbg));
                                        Files.write(file.toPath(), lines);
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    status.setOpacity(1);
                    status.setText("Pogrešno korisničko ime ili lozinka");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Postavljanje aranzmana u tabelu
    private void postavi_aranzmane(){
        jednodnevna_lista.getItems().clear();
        jednoAranzman.getItems().clear();
        visednevna_lista.getItems().clear();
        viseAranzman.getItems().clear();
        for (Aranzman a:database.listaAranzmana()) {
            if(a.getOdlazakDate().equals(a.getDolazakDate())){
                String izlet = a.toString();
                jednodnevna_lista.getItems().add(izlet);
                jednoAranzman.getItems().add(a);
            }else {
                for(Smjestaj s:database.listaSmjestaja()){
                    if(s.getId()==a.getSmjestajId().getId()){
                        String putovanje = a+"  |  "+s.getNaziv()+"  |  "+s.getVrsta_sobe()+"  |  "+s.getBroj_zvjezdica()+"☆";
                        visednevna_lista.getItems().add(putovanje);
                        viseAranzman.getItems().add(a);
                    }
                }
            }
        }
    }

    // Prikazivanje izleta u izlet kategoriji
    @FXML
    private void prikazi_izlet(MouseEvent event) throws IOException{
        lista_klijenata.getItems().clear();
        admin_lista_klijenata.getItems().clear();
        izlet_admin = true;
        putovanje_admin = false;

        lista.clear();

            int indeks = jednodnevna_lista.getSelectionModel().getSelectedIndex();
            indeksRez = indeks;
            if(indeks!=-1){
                Aranzman izabrani = jednoAranzman.getItems().get(indeks);
                boolean zastava = false;
                for(Rezervacija r:database.listaRezervacija()){
                    if(r.getAranzmanId()==izabrani.getId()){
                        int klijent_id = r.getClient_id();
                        for(Client c:database.listaKlijenata()){
                            if(c.getId()==klijent_id){
                                s_naziv_aranzmana = izabrani.getPutovanje();
                                if(r.getPlacena_cijena().equals("-1")){
                                    lista_klijenata.getItems().add(ispis_u_listu(c,-1));
                                    lista.add(ispis_u_listu(c,-1));
                                } else if (r.getPlacena_cijena().equals("-2")) {
                                    lista_klijenata.getItems().add(ispis_u_listu(c,-2));
                                    lista.add(ispis_u_listu(c,-2));
                                } else {
                                    if ((r.getUkupna_cijena() - Double.parseDouble(r.getPlacena_cijena()))==0){
                                        lista_klijenata.getItems().add(ispis_u_listu(c,0));
                                        lista.add(ispis_u_listu(c,0));


                                    }
                                    else{
                                        lista_klijenata.getItems().add(ispis_u_listu(c,5) +"  |  Plaćeno: "+r.getPlacena_cijena()+"  |  Preostalo: "+ (r.getUkupna_cijena() - Double.parseDouble(r.getPlacena_cijena())));
                                        lista.add(ispis_u_listu(c,5) +"  |  Plaćeno: "+r.getPlacena_cijena()+"  |  Preostalo: "+ (r.getUkupna_cijena() - Double.parseDouble(r.getPlacena_cijena())));
                                    }

                                }
                                admin_lista_klijenata.getItems().add(c);
                                zastava = true;

                            }
                        }
                    }
                }
                if (zastava){
                    setRezervacija(s_naziv_aranzmana, lista);
                    listReservation(event);
                }
                if(lista_klijenata.getItems().isEmpty()){
                    s_naziv_aranzmana = izabrani.getPutovanje();
                    setRezervacija(s_naziv_aranzmana, lista);
                    listReservation(event);
                }
            }else {
                notification("Izaberite aranžman.", Alert.AlertType.INFORMATION);
            }



    }

    // Prikazivanje putovanja u putovanja kategoriji
    @FXML
    private void prikazi_putovanje(MouseEvent event) throws IOException{
        lista_klijenata.getItems().clear();
        admin_lista_klijenata.getItems().clear();
        izlet_admin = false;
        putovanje_admin = true;
        lista.clear();
            int indeks = visednevna_lista.getSelectionModel().getSelectedIndex();
            indeksRez = indeks;
            if(indeks!=-1){
                Aranzman izabrani = viseAranzman.getItems().get(indeks);
                boolean zastava = false;
                for(Rezervacija r:database.listaRezervacija()){
                    if(r.getAranzmanId()==izabrani.getId()){
                        int klijent_id = r.getClient_id();
                        for(Client c:database.listaKlijenata()){
                            if(c.getId()==klijent_id){
                                s_naziv_aranzmana = izabrani.getPutovanje();
                                if(r.getPlacena_cijena().equals("-1")){
                                    lista_klijenata.getItems().add(ispis_u_listu(c,-1));
                                    lista.add(ispis_u_listu(c,-1));
                                }else {
                                    if ((r.getUkupna_cijena() - Double.parseDouble(r.getPlacena_cijena()))==0){
                                        lista_klijenata.getItems().add(ispis_u_listu(c,0));
                                        lista.add(ispis_u_listu(c,0));

                                    }
                                    else{
                                        lista_klijenata.getItems().add(ispis_u_listu(c,5) +"  |  Plaćeno: "+r.getPlacena_cijena()+"  |  Preostalo: "+ (r.getUkupna_cijena() - Double.parseDouble(r.getPlacena_cijena())));
                                        lista.add(ispis_u_listu(c,5) +"  |  Plaćeno: "+r.getPlacena_cijena()+"  |  Preostalo: "+ (r.getUkupna_cijena() - Double.parseDouble(r.getPlacena_cijena())));
                                    }

                                }
                                admin_lista_klijenata.getItems().add(c);
                                zastava = true;

                            }
                        }

                    }
                }
                if (zastava){
                    setRezervacija(s_naziv_aranzmana, lista);
                    listReservation(event);
                }
                if(lista_klijenata.getItems().isEmpty()){
                    s_naziv_aranzmana = izabrani.getPutovanje();
                    setRezervacija(s_naziv_aranzmana, lista);
                    listReservation(event);
                }
            }else {
                notification("Izaberite aranžman.", Alert.AlertType.INFORMATION);
            }


    }

    // Doplata rezervacije
    @FXML
    private void doplati() {
        int indeks = klijent_aktivne.getSelectionModel().getSelectedIndex();
        if (indeks != -1) {
            Aranzman aranzman = a_klijent_aktivne.getItems().get(indeks);
            String iznosDoplate = iznos_doplate.getText();

            if (aranzman == null) {
                notification("Izaberite rezervaciju", Alert.AlertType.ERROR);
                return;
            }

            if (!iznosDoplate.matches("^\\d+(\\.\\d{1,2})?$")) {
                notification("Neispravan unos.", Alert.AlertType.ERROR);
                return;
            }

            TextInputDialog dialog = kreirajPotvrdniDialog();
            dialog.showAndWait().ifPresent(lozinka -> {
                if (!lozinka.equals(klijent_password)) {
                    notification("Lozinka nije tačna!", Alert.AlertType.ERROR);
                } else {
                    try {
                        double iznos = Double.parseDouble(iznosDoplate);
                        // Formatiranje iznosa na dvije decimale
                        DecimalFormat df = new DecimalFormat("#.##");
                        String formatiraniIznos = df.format(iznos);

                        for (Rezervacija r : database.listaRezervacija()) {
                            if (client_id == r.getClient_id() && aranzman.getId() == r.getAranzmanId()) {
                                double preostaliIznos = (r.getUkupna_cijena() - Double.parseDouble(r.getPlacena_cijena()));
                                double zaokruzenPreostaliIznos = Double.parseDouble(String.format("%.2f", preostaliIznos).replace(",", "."));


                                if (Double.parseDouble(formatiraniIznos.replace(",", ".")) <= zaokruzenPreostaliIznos) {
                                    for (BankAcc b : database.listaBankovnihRacuna()) {
                                        if (s_jmbg.equals(b.getJmb())) {
                                            if (Double.parseDouble(r.getPlacena_cijena()) == 0) {
                                                notification("Rezervacija je već potpuno plaćena", Alert.AlertType.INFORMATION);
                                                return;
                                            }
                                            if (iznos <= 0) {
                                                notification("Unesite validan broj", Alert.AlertType.ERROR);
                                                return;
                                            }
                                            if (iznos <= b.getBalance()) {
                                                iznos_doplate.setText("");
                                                BankAcc banka = database.listaBankovnihRacuna().getLast();
                                                balans.setText(String.valueOf(Double.parseDouble(balans.getText().replace(",", ".")) - Double.parseDouble(formatiraniIznos.replace(",", "."))));
                                                balance -= Double.parseDouble(formatiraniIznos.replace(",", "."));
                                                arange_potroseno.setText(String.valueOf(Double.parseDouble(arange_potroseno.getText().replace(",", ".")) + iznos));
                                                doplata.setText(String.valueOf(Double.parseDouble(doplata.getText().replace(",", ".")) - iznos));
                                                database.updatePlacenaRezervacija(client_id, aranzman.getId(), iznos);
                                                database.updateStanjeRacuna(s_jmbg, b.getBalance() - iznos, banka.getId(), banka.getBalance() + iznos);
                                                // Formatiranje preostalog iznosa na dvije decimale
                                                String formatiraniPreostaliIznos = df.format(preostaliIznos - iznos);
                                                // Kreiranje formatiranog stringa aranzmana
                                                String aranzman_string = aranzman + "  |  Plaćeno: " + formatiraniIznos + "  |  Preostalo: " + formatiraniPreostaliIznos;
                                                klijent_aktivne.getItems().remove(indeks);
                                                klijent_aktivne.getItems().add(indeks, aranzman_string);
                                                postavi_rezervacije();
                                                notification("Doplaćeno " + formatiraniIznos + " za " + aranzman.getPutovanje() + "\nVaše trenutno stanje na računu je: " + balans.getText(), Alert.AlertType.CONFIRMATION);
                                                return;
                                            } else {
                                                notification("Nemate dovoljno novca na računu!", Alert.AlertType.ERROR);
                                                return;
                                            }
                                        }
                                    }
                                } else {
                                    notification("Iznos za uplatu je veći nego što je preostalo za plaćanje.", Alert.AlertType.ERROR);
                                    iznos_doplate.requestFocus();
                                    return;
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        notification("Iznos nije validan", Alert.AlertType.ERROR);
                        iznos_doplate.requestFocus();
                    }
                }
            });
        } else {
            notification("Izaberite rezervaciju.", Alert.AlertType.ERROR);
        }
    }



    // Otkazivanje rezervacije na admin strani
    @FXML
    private void otkazi() throws SQLException {
        int indeks = klijent_aktivne.getSelectionModel().getSelectedIndex();
        if (indeks != -1) {
            Aranzman aranzman = a_klijent_aktivne.getItems().get(indeks);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setStyle("-fx-background-color: #D7F2FA; -fx-font-size: 14px;");
            alert.setTitle("Potvrda");
            alert.setHeaderText("Da li ste sigurni da želite otkazati rezervaciju?");
            alert.setContentText("Otkazivanjem rezervacije biće vam vraćen novac na račun.");
            dialogPane.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeOk) {
                for (Rezervacija r : database.listaRezervacija()) {
                    if (client_id == r.getClient_id() && aranzman.getId() == r.getAranzmanId()) {
                        double uplaceniIznos = Double.parseDouble(r.getPlacena_cijena());
                        BankAcc banka = database.listaBankovnihRacuna().getLast();

                        BankAcc racun_klijent = new BankAcc(banka.getId(), banka.getAccNUM(), banka.getJmb(), banka.getBalance());
                        database.updateStanjeRacuna(s_jmbg, Double.parseDouble(balans.getText().replace("," , ".")) + uplaceniIznos, banka.getId(), banka.getBalance()-uplaceniIznos);
                        racun_klijent.setBalance(Double.parseDouble(balans.getText().replace(",", ".")) + uplaceniIznos);
                        balans.setText(String.valueOf(racun_klijent.getBalance()));
                        balance = racun_klijent.getBalance();

                        if(uplaceniIznos!=-1){
                            doplata.setText(String.valueOf((Double.parseDouble(doplata.getText().replace(",", ".")) - (r.getUkupna_cijena()-Double.parseDouble(r.getPlacena_cijena())))));
                        }else {
                            doplata.setText(String.valueOf((Double.parseDouble(doplata.getText()) - r.getUkupna_cijena())));
                        }

                        klijent_aktivne.getItems().remove(indeks);
                        a_klijent_aktivne.getItems().remove(indeks);
                        database.otkaziRezervaciju(client_id,aranzman.getId());
                        a_klijent_otkazane.getItems().add(aranzman);
                        klijent_otkazane.getItems().add(aranzman.toString());
                        postavi_rezervacije();
                        notification("Rezervacija je uspješno otkazana, novac je vraćen na račun.\nVaše trenutno stanje je: "+balans.getText(), Alert.AlertType.INFORMATION);
                        return;
                    }
                }
            }
        } else {
            notification("Izaberite rezervaciju za otkazivanje.", Alert.AlertType.ERROR);
        }
    }



    //Dodavanje admina u bazu
    @FXML
    void addAdmin(ActionEvent event) {

        String adminIme = admin_name.getText();
        String adminPrezime = admin_lname.getText();
        String adminUsername = admin_user.getText();
        admin_status.setTextFill(Color.RED);

        if (adminIme.isEmpty() || adminPrezime.isEmpty() || adminUsername.isEmpty()) {
            admin_status.setText("Popunite sva polja.");
            admin_status.setOpacity(1);
            return;
        }
        try {
            if (database.isAdminExists(adminUsername)) {
                admin_status.setText("Admin sa korisničkim imenom već postoji.");
                admin_status.setOpacity(1);
                return;
            }

            Admin admin = new Admin(5, adminIme, adminPrezime, adminUsername);
            database.addAdmin(admin);
            admin_status.setText("Novi admin uspješno dodan");
            admin_name.setText("");
            admin_lname.setText("");
            admin_user.setText("");
            admin_status.setTextFill(Color.GREEN);
            admin_status.setOpacity(1);



        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //Promjena sifre
    @FXML
    private void PassChange(ActionEvent event) throws SQLException {
            String nova_lozinka = newpass.getText();
            String potvrda_nove = newpass_conf.getText();

            if (nova_lozinka.equalsIgnoreCase("") || potvrda_nove.equalsIgnoreCase("")){
                change_status.setText("Popunite sva polja.");
                change_status.setOpacity(1);
                return;
            }
            if (!nova_lozinka.equals(potvrda_nove)) {
                change_status.setText("Lozinke se ne poklapaju.");
                change_status.setOpacity(1);
                return;
            }
            if (  (nova_lozinka.equals(klijent_password) && user_type.equals("user") )  || (nova_lozinka.equals(admin_pass) && user_type.equals("admin")) ){
                change_status.setText("Lozinka je ista kao i trenutna");
                change_status.setOpacity(1);
                return;
            }

        try {
            if (user_type.equals("admin"))
                database.changeAdminPassword(static_user, nova_lozinka);
            else
                database.changeClientPassword(static_user,nova_lozinka);


            change_status.setTextFill(Color.GREEN);
            change_status.setText("Lozinka uspješno promjenjena.");
            change_status.setOpacity(1);
        } catch (SQLException e) {
            e.printStackTrace();
            change_status.setText("Greška prilikom promjene lozinke.");
            change_status.setOpacity(1);
        }

    }

    //dodavanje putovanja
    @FXML
    public void addTrip() throws SQLException {
        String naziv = tripName.getText();
        String destinacija = destName.getText();
        String cijena = arangePrice.getText();
        String smjestaj = accomoName.getText();
        String nocenje = nightPrice.getText();
        LocalDate polazak = departureDate.getValue();
        LocalDate dolazak = backDate.getValue();
        String soba = String.valueOf(roomType.getValue());
        String prijevoz = String.valueOf(transportName.getValue());
        String zvjezdice = String.valueOf(stars.getValue());
        int id = database.getLastAddedTripId();
        int broj_dana = 1;

        if (naziv.equalsIgnoreCase("") || destinacija.equalsIgnoreCase("") || cijena.equalsIgnoreCase("") ||
            smjestaj.equalsIgnoreCase("") || nocenje.equalsIgnoreCase("") || polazak == null || dolazak == null ||
            soba.equalsIgnoreCase("") || prijevoz.equalsIgnoreCase("") || zvjezdice.equalsIgnoreCase("")){
            tripstatus.setText("Popunite sva polja");
            tripstatus.setOpacity(1);
            return;
        }

        if(!naziv.matches("^[a-zA-Z\\s]+$")){
            tripstatus.setText("Naziv nije važeći.");
            tripstatus.setOpacity(1);
            return;
        }
        if(!destinacija.matches("^[a-zA-Z\\s]+$")){
            tripstatus.setText("Destinacija nije važeća.");
            tripstatus.setOpacity(1);
            return;
        }
        try{
            double c = Double.parseDouble(cijena);
            if(!smjestaj.matches("^[a-zA-Z\\s]+$")){
                tripstatus.setText("Nepravilan unos smještaja.");
                tripstatus.setOpacity(1);
                return;
            }
            try {
                double cijena_n = Double.parseDouble(nocenje);
                if(polazak==null || dolazak==null || polazak.isAfter(dolazak)){
                    tripstatus.setText("Datumi nisu važeći.");
                    tripstatus.setOpacity(1);
                    return;
                }

                try{
                    broj_dana = (int) ChronoUnit.DAYS.between(polazak,dolazak);
                }catch (Exception e){
                    tripstatus.setText("Datumi nisu važeći.");
                    tripstatus.setOpacity(1);
                    return;
                }
                if(roomType.getValue()==null){
                    tripstatus.setText("Izaberite vrstu sobe.");
                    tripstatus.setOpacity(1);
                    return;
                }
                if(transportName.getValue()==null){
                    tripstatus.setText("Izaberite tip prijevoza.");
                    tripstatus.setOpacity(1);
                    return;
                }
                if(stars.getValue()==null){
                    tripstatus.setText("Unesite zvjezdice.");
                    tripstatus.setOpacity(1);
                    return;
                }

                if(polazak.isAfter(dolazak) || polazak.isBefore(LocalDate.now().plusDays(15))){
                    tripstatus.setText("Unesite važeće datume.");
                    tripstatus.setOpacity(1);
                    return;
                }

                String putovanje =naziv+" | "+destinacija+" | "+ prijevoz+" | "+polazak+" | "+ dolazak+" | Aranzman: " +c+" | Noćenje: "+cijena_n+" | Ukupna cijena: "+(c+cijena_n*broj_dana)+" | "+smjestaj+" | "+soba+" | "+zvjezdice+"☆";
                int idd = database.getLastAddedTripId();
                visednevna_lista.getItems().add(putovanje);
                tripstatus.setText("Uspješno dodano putovanje");
                tripstatus.setTextFill(Color.GREEN);
                tripstatus.setOpacity(1);
                int id_smjestaj = database.getLastAddedAccomo();
                Smjestaj s = new Smjestaj(id_smjestaj,smjestaj,zvjezdice,soba,String.valueOf(cijena_n));
                Aranzman aranzman = new Aranzman(idd,naziv,destinacija,"Autobus",polazak,dolazak,Double.parseDouble(cijena),s);
                viseAranzman.getItems().add(aranzman);
                Database.addSmjestaj(id_smjestaj,naziv,zvjezdice,soba,String.valueOf(cijena_n));
                Database.addTrip(idd,naziv,destinacija,prijevoz,polazak,dolazak,c,s);
                tripName.setText("");
                destName.setText("");
                arangePrice.setText("");
                accomoName.setText("");
                nightPrice.setText("");
                departureDate.setValue(null);
                backDate.setValue(null);
                roomType.setValue(null);
                transportName.setValue(null);
                stars.setValue(null);
                postavi_aranzmane();


            }catch (Exception e){
                tripstatus.setText("Cijena nocenja nije validna");
                tripstatus.setTextFill(Color.RED);
                tripstatus.setOpacity(1);
            }


        }catch (Exception e){
            tripstatus.setText("Cijena nije validna");
            tripstatus.setTextFill(Color.RED);
            tripstatus.setOpacity(1);
        }
    }

    //dodavanje izleta
    @FXML
    private void addIzlet() throws IOException {
        String naziv  = izletName.getText();
        String destinacija = Accomo_destName.getText();
        String cijena = AccomoPrice.getText();
        LocalDate datum = Accomo_departureDate.getValue();

        if(!naziv.matches("^[a-zA-Z\\s]+$")){
            izletGreska.setTextFill(Color.RED);
            izletGreska.setText("Naziv izleta nije važeći.");
            izletGreska.setOpacity(1);
            return;
        }
        if(!destinacija.matches("^[a-zA-Z\\s]+$")){
            izletGreska.setTextFill(Color.RED);
            izletGreska.setText("Destinacija izleta nije važeća.");
            izletGreska.setOpacity(1);
            return;
        }
        if(datum==null || datum.isBefore(LocalDate.now().plusDays(15))){
            izletGreska.setTextFill(Color.RED);
            izletGreska.setText("Datum izleta nije važeći.");
            izletGreska.setOpacity(1);
            return;
        }
        try {
            double ccijena = Double.parseDouble(cijena);
            String izlet = naziv+" | "+destinacija+" | Autobus | "+datum+" | Ukupna cijena: "+cijena;
            int idd = database.getLastAddedTripId();
            Aranzman aranzman = new Aranzman(idd,naziv,destinacija,"Autobus",datum,datum,Double.parseDouble(cijena),null);
            jednodnevna_lista.getItems().add(izlet);
            jednoAranzman.getItems().add(aranzman);
            izletGreska.setOpacity(1);
            izletGreska.setTextFill(Color.GREEN);
            izletGreska.setText("Uspješno dodan izlet.");
            postavi_aranzmane();
            izletName.setText("");
            Accomo_destName.setText("");
            AccomoPrice.setText("");
            Accomo_departureDate.setValue(null);


            int id = database.getLastAddedTripId();
            database.addIzlet(id,naziv,destinacija,datum,ccijena);

        }catch (NumberFormatException e){
            e.printStackTrace();
            izletGreska.setTextFill(Color.RED);
            izletGreska.setText("Cijena nije validna");
            izletGreska.setOpacity(1);
            return;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    //lista rezervacija sekcija
    @FXML
    private void lista_rezervacije_click(ActionEvent event){
        otkazi_button.setVisible(false);
        doplata_button.setVisible(false);
        arange_potroseno_text.setVisible(false);
        arange_potroseno.setVisible(false);
        iznosDoplate_text.setVisible(false);
        iznos_doplate.setVisible(false);
        doplata_text.setVisible(false);
        doplata.setVisible(false);
        rezervacijeTable.setVisible(false);
        izletiTable.setVisible(true);
        destName.setVisible(true);
        stars.setVisible(true);
        departureDate.setVisible(true);
        backDate.setVisible(true);
        roomType.setVisible(true);
        transportName.setVisible(true);
        cijenaBroj.setVisible(true);
        filterIzlet.setVisible(true);
        filterUkloni.setVisible(true);

    }

    //rezervacija sekcija
    @FXML
    private void rezervacije_klik(ActionEvent event){
        izletiTable.setVisible(false);
        destName.setVisible(false);
        stars.setVisible(false);
        departureDate.setVisible(false);
        backDate.setVisible(false);
        roomType.setVisible(false);
        transportName.setVisible(false);
        filterUkloni.setVisible(false);
        filterIzlet.setVisible(false);
        cijenaBroj.setVisible(false);
        rezervacijeTable.setVisible(true);
        arange_potroseno_text.setVisible(true);
        arange_potroseno.setVisible(true);
        iznosDoplate_text.setVisible(true);
        iznos_doplate.setVisible(true);
        doplata_text.setVisible(true);
        doplata.setVisible(true);
        doplata_button.setVisible(true);
        otkazi_button.setVisible(true);


    }

    // Filtracija izleta/putovanja
    @FXML
    void filterIzletPutovanje(ActionEvent event) {
        String filterText = destName.getText();
        String filterCijena = cijenaBroj.getEditor().getText();
        String filterZvjezdice = stars.getValue() != null ? String.valueOf(stars.getValue()) : "";
        String filterPrijevoz = transportName.getValue() != null ? transportName.getValue().toString() : "";
        String filterSoba = roomType.getValue() != null ? roomType.getValue().toString() : "";
        LocalDate filterDatumPolaska = departureDate.getValue();
        LocalDate filterDatumPovratka = backDate.getValue();

        if (filterText.isEmpty() && filterCijena.isEmpty() && filterZvjezdice.isEmpty() && filterPrijevoz.isEmpty() && filterSoba.isEmpty() && filterDatumPolaska == null && filterDatumPovratka == null) {
            postavi_aranzmane();
        } else {
            jednodnevna_lista.getItems().clear();
            jednoAranzman.getItems().clear();
            visednevna_lista.getItems().clear();
            viseAranzman.getItems().clear();
            for (Aranzman a : database.listaAranzmana()) {
                if (a.getOdlazakDate().equals(a.getDolazakDate())) {
                    if ((filterText.isEmpty() || a.getDestinacija().toLowerCase().contains(filterText.toLowerCase())) &&
                            (filterCijena.isEmpty() || a.getUkupna_cijena() <= Integer.parseInt(filterCijena)) &&
                            (filterPrijevoz.isEmpty() || a.getTransport().equalsIgnoreCase(filterPrijevoz))) {
                        String izlet = a.toString();
                        jednodnevna_lista.getItems().add(izlet);
                        jednoAranzman.getItems().add(a);
                    }
                } else {
                    for (Smjestaj s : database.listaSmjestaja()) {
                        if (s.getId() == a.getSmjestajId().getId()) {
                            if ((filterText.isEmpty() || a.getDestinacija().toLowerCase().contains(filterText.toLowerCase())) &&
                                    (filterCijena.isEmpty() || a.getUkupna_cijena() <= Integer.parseInt(filterCijena)) &&
                                    (filterZvjezdice.isEmpty() || s.getBroj_zvjezdica().equals(filterZvjezdice)) &&
                                    (filterPrijevoz.isEmpty() || a.getTransport().equalsIgnoreCase(filterPrijevoz)) &&
                                    (filterSoba.isEmpty() || s.getVrsta_sobe().equalsIgnoreCase(filterSoba)) &&
                                    (filterDatumPolaska == null || a.getOdlazakDate().isEqual(filterDatumPolaska)) &&
                                    (filterDatumPovratka == null || a.getDolazakDate().isEqual(filterDatumPovratka))) {
                                String putovanje = a + " | " + s.getNaziv() + " | " + s.getVrsta_sobe() + " | " + s.getBroj_zvjezdica() + "☆";
                                visednevna_lista.getItems().add(putovanje);
                                viseAranzman.getItems().add(a);
                            }
                        }
                    }
                }
            }
        }
    }


    // Uklanjanje svih filtera
    @FXML
    void UkloniFilter(ActionEvent event) {
        destName.clear();
        cijenaBroj.getEditor().clear();
        stars.setValue(null);
        transportName.setValue(null);
        roomType.setValue(null);
        departureDate.setValue(null);
        backDate.setValue(null);

        postavi_aranzmane();
    }

    // Rezervacija izleta ---------------------------------------
    @FXML
    public void rezervacija_izleta(MouseEvent event) {
        String aranzman_string = jednodnevna_lista.getSelectionModel().getSelectedItem();
        int indeks = jednodnevna_lista.getSelectionModel().getSelectedIndex();

        if (indeks != -1) {
            Aranzman aranzman = jednoAranzman.getItems().get(indeks);
            TextInputDialog dialog = kreirajPotvrdniDialog();

            dialog.showAndWait().ifPresent(lozinka -> {
                if (lozinka.equals(klijent_password)) {
                    handlePotvrdaLozinkeIzlet(aranzman, aranzman_string);
                } else {
                    notification("Lozinka nije tačna!", Alert.AlertType.ERROR);
                }
            });
        }
    }

    private TextInputDialog kreirajPotvrdniDialog() {
        TextInputDialog dialog = new TextInputDialog();
        DialogPane textpane = dialog.getDialogPane();
        textpane.setStyle("-fx-background-color: #D7F2FA; -fx-font-size: 14px;");
        dialog.setTitle("Potvrda rezervacije");
        dialog.setHeaderText("Unesite lozinku za potvrdu rezervacije:");
        dialog.setContentText("Lozinka:");
        textpane.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);
        return dialog;
    }

    // Nakon potvrde lozinke provjeravamo izlet
    private void handlePotvrdaLozinkeIzlet(Aranzman aranzman, String aranzman_string) {
        int indikator = 0;
        for (Aranzman a : a_klijent_aktivne.getItems()) {
            if (a.getId() == aranzman.getId()) {
                indikator++;
            }
        }

        if (indikator > 0) {
            notification("Već imate ovu rezervaciju", Alert.AlertType.ERROR);
            return;
        }

        if (aranzman.getOdlazakDate().isBefore(LocalDate.now().plusDays(15))) {
            notification("Rok za rezervaciju je istekao.", Alert.AlertType.INFORMATION);
            return;
        }

        if (balance < aranzman.getCijenaAranzmana() / 2) {
            notification("Nedovoljno količine novca za uplatu", Alert.AlertType.ERROR);
            return;
        }

        double potroseno_trenutni = aranzman.getCijenaAranzmana() / 2;
        double potroseno = Double.parseDouble(arange_potroseno.getText().replace(",", ".")) + (aranzman.getCijenaAranzmana() / 2);

        // Zaokruživanje na dvije decimale
        potroseno_trenutni = Math.round(potroseno_trenutni * 100.0) / 100.0;
        potroseno = Math.round(potroseno * 100.0) / 100.0;


        a_klijent_aktivne.getItems().add(aranzman);
        klijent_aktivne.getItems().add(aranzman_string + " | Plaćeno: " + potroseno_trenutni + " | Preostalo: " + potroseno_trenutni);
        notification("Uspješno rezervisano!", Alert.AlertType.CONFIRMATION);

        arange_potroseno.setText(String.valueOf(potroseno));
        doplata.setText(String.valueOf(potroseno));
        balans.setText(String.valueOf(Double.parseDouble(balans.getText()) - potroseno_trenutni));
        balance -= potroseno_trenutni;


        BankAcc bank = database.listaBankovnihRacuna().getLast();
        database.updateStanjeRacuna(s_jmbg, dohvatiStanjeNovca(jmbg) - potroseno_trenutni, bank.getId(), bank.getBalance() + potroseno_trenutni);

        database.dodajRezervaciju(client_id, aranzman.getId(), String.valueOf(potroseno_trenutni * 2), String.valueOf(potroseno_trenutni));


        postavi_rezervacije();
    }
    // ---------------------------------------------------------------

    //Rezervacija putovanja --------------------------------

    @FXML
    public void rezervacija_putovanja(MouseEvent event) {
        String aranzman_string = visednevna_lista.getSelectionModel().getSelectedItem();
        int indeks = visednevna_lista.getSelectionModel().getSelectedIndex();

        if (indeks != -1) {
            Aranzman aranzman = viseAranzman.getItems().get(indeks);
            TextInputDialog dialog = kreirajPotvrdniDialog();

            dialog.showAndWait().ifPresent(lozinka -> {
                if (lozinka.equals(klijent_password)) {
                    handlePotvrdaLozinkeTrip(aranzman, aranzman_string);
                } else {
                    notification("Lozinka nije tačna!", Alert.AlertType.ERROR);
                }
            });
        }
    }


    // Nakon potvrde lozinke provjeravamo putovanje

    private void handlePotvrdaLozinkeTrip(Aranzman aranzman, String aranzman_string){

        int indikator = 0;
        for (Aranzman a : a_klijent_aktivne.getItems()) {
            if (a.getId() == aranzman.getId()) {
                indikator++;
            }
        }

        if (indikator > 0) {
            notification("Već imate ovu rezervaciju", Alert.AlertType.ERROR);
            return;
        }

        if (aranzman.getOdlazakDate().isBefore(LocalDate.now().plusDays(15))) {
            notification("Rok za rezervaciju je istekao.", Alert.AlertType.INFORMATION);
            return;
        }

        if (balance < aranzman.getCijenaAranzmana() / 2) {
            notification("Nedovoljno količine novca za uplatu", Alert.AlertType.ERROR);
            return;
        }


        double potroseno_trenutni = aranzman.getUkupna_cijena() / 2;
        double potroseno = Double.parseDouble(arange_potroseno.getText().replace(",", ".")) + aranzman.getUkupna_cijena() / 2;

        potroseno_trenutni = Math.round(potroseno_trenutni * 100.0) / 100.0;
        potroseno = Math.round(potroseno * 100.0) / 100.0;

        a_klijent_aktivne.getItems().add(aranzman);
        klijent_aktivne.getItems().add(aranzman_string + " | Plaćeno: " + potroseno_trenutni + " | Preostalo: " + potroseno_trenutni);
        notification("Uspješno rezervisano!", Alert.AlertType.CONFIRMATION);

        arange_potroseno.setText(String.valueOf(potroseno));
        doplata.setText(String.valueOf(potroseno));
        balans.setText(String.valueOf(Double.parseDouble(balans.getText()) - potroseno_trenutni));
        balance -= potroseno_trenutni;


        BankAcc bank = database.listaBankovnihRacuna().getLast();
        database.updateStanjeRacuna(s_jmbg, dohvatiStanjeNovca(jmbg) - potroseno_trenutni, bank.getId(), bank.getBalance() + potroseno_trenutni);


        database.dodajRezervaciju(client_id, aranzman.getId(), String.valueOf(potroseno_trenutni * 2), String.valueOf(potroseno_trenutni));

        postavi_rezervacije();


    }
    // -------------------------------------------

    @FXML
    private void otkaziAranzman(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        Aranzman izabrani = new Aranzman();

        if (izlet_admin) {
            if (indeksRez != -1) {
                izabrani = jednoAranzman.getItems().get(indeksRez);
            }

            Alert potvrda = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPotrvda = potvrda.getDialogPane();
            dialogPotrvda.setStyle("-fx-background-color: #D7F2FA; -fx-font-size: 14px;");
            potvrda.setTitle("Potvrda otkazivanja i brisanja");
            potvrda.setHeaderText("Jeste li sigurni da želite otkazati aranžman?");
            potvrda.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);
            Optional<ButtonType> rezultat = potvrda.showAndWait();

            if (rezultat.isPresent() && rezultat.get() == buttonTypeOk) {
                List<Client> rezervisani_klijenti = database.listaKlijenataZaAranzman(izabrani);
                double placeno = 0;
                    for (Client c : rezervisani_klijenti) {
                        boolean indikator = false;
                        for(Rezervacija r:database.listaRezervacija()){
                            if(r.getClient_id()==c.getId() && r.getAranzmanId()== izabrani.getId()){
                                if (Double.parseDouble(r.getPlacena_cijena())==-1){
                                    indikator = true;
                                    continue;
                                }
                                if (Double.parseDouble(r.getPlacena_cijena())==-2){
                                    continue;
                                }
                                placeno = Double.parseDouble(r.getPlacena_cijena());
                                break;
                            }
                        }
                        if (!indikator){
                            database.dodajObrisaniAranzman(c.getJMBG(), izabrani.getPutovanje());
                        }
                        database.otkaziAranzman(izabrani.getId(), c.getId());
                        Database.updateStanjeRacuna(c.getJMBG(),placeno+dohvatiStanjeNovca(c.getJMBG()),11,dohvatiStanjeNovca("1102541293")-placeno);
                    }


                database.obrisiAranzman(izabrani.getId());
                jednoAranzman.getItems().remove(izabrani);
                jednodnevna_lista.getItems().remove(izabrani);
                notification("Uspješno otkazan i obrisan aranžman", Alert.AlertType.INFORMATION);
                postavi_aranzmane();

                // Zatvaranje rezervacijske liste
                Stage oldStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                oldStage.close();
                // Refreshovanje admin prozora
                currentAdminStage.close();
                switchToScene4(event);

            }
        }
        else if (putovanje_admin) {
            if (indeksRez != -1) {
                izabrani = viseAranzman.getItems().get(indeksRez);
            }

            Alert potvrda = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPotrvda = potvrda.getDialogPane();
            dialogPotrvda.setStyle("-fx-background-color: #D7F2FA; -fx-font-size: 14px;");
            potvrda.setTitle("Potvrda otkazivanja i brisanja");
            potvrda.setHeaderText("Jeste li sigurni da želite otkazati aranžman?");
            potvrda.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);
            Optional<ButtonType> rezultat = potvrda.showAndWait();

            if (rezultat.isPresent() && rezultat.get() == buttonTypeOk) {
                List<Client> rezervisani_klijenti = Database.listaKlijenataZaAranzman(izabrani);
                double placeno = 0;
                for (Client c : rezervisani_klijenti) {
                    boolean indikator = false;
                    for(Rezervacija r:database.listaRezervacija()){
                        if(r.getClient_id()==c.getId() && r.getAranzmanId() == izabrani.getId()){
                            if (Double.parseDouble(r.getPlacena_cijena())==-1){
                                indikator = true;
                                continue;
                            }
                            if (Double.parseDouble(r.getPlacena_cijena())==-2){
                                continue;
                            }
                            placeno = Double.parseDouble(r.getPlacena_cijena());
                            break;
                        }

                    }
                    if (!indikator){
                        database.dodajObrisaniAranzman(c.getJMBG(), izabrani.getPutovanje());
                    }
                    database.otkaziAranzman(izabrani.getId(), c.getId());
                    Database.updateStanjeRacuna(c.getJMBG(),placeno+dohvatiStanjeNovca(c.getJMBG()),11,dohvatiStanjeNovca("1102541293")-placeno);
                }

                database.obrisiAranzman(izabrani.getId());
                viseAranzman.getItems().remove(izabrani);
                visednevna_lista.getItems().remove(izabrani);
                notification("Uspješno otkazan i obrisan aranžman", Alert.AlertType.INFORMATION);
                postavi_aranzmane();

                // Zatvaranje starog prozora
                Stage oldStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                oldStage.close();

                //Refreshovanje admin prozora
                currentAdminStage.close();
                switchToScene4(event);

            }
        }
    }


    //Metoda koja postavlja sve rezervacije klijenta (aktivne, protekle, otkazane)
    private void postavi_rezervacije() {
        klijent_aktivne.getItems().clear();
        klijent_otkazane.getItems().clear();
        klijent_protekle.getItems().clear();
        a_klijent_aktivne.getItems().clear();
        a_klijent_otkazane.getItems().clear();
        a_klijent_protekle.getItems().clear();
        double potrebno_platiti = 0;
        double placeno = 0;
        deadline = false;
        deadline2 = false;
        s_lista = "";

        // Kreiranje DecimalFormat objekta za formatiranje na dvije decimale
        DecimalFormat df = new DecimalFormat("#.##");

        for (Rezervacija r : database.listaRezervacija()) {
            if (client_id == r.getClient_id()) {
                int id_rezervacije = r.getAranzmanId();
                for (Aranzman a : database.listaAranzmana()) {
                    if (a.getId() == id_rezervacije) {
                        if (Double.parseDouble(r.getPlacena_cijena()) > 0) {
                            long brojDana = ChronoUnit.DAYS.between(LocalDate.now(), a.getOdlazakDate().minusDays(14));
                            if (!a.getOdlazakDate().isBefore(LocalDate.now().plusDays(15))) {
                                if (a.getOdlazakDate().isBefore(LocalDate.now().plusDays(18))) {
                                    if (Double.parseDouble(r.getPlacena_cijena())<r.getUkupna_cijena() && Double.parseDouble(r.getPlacena_cijena())!=-1){
                                        deadline = true;
                                    }
                                    s_lista  += "Ostalo Vam je još "+ brojDana +" dana za doplatu aktivnog aranžmana: \'" + a.getPutovanje() +"\'\n\n";
                                    a_klijent_aktivne.getItems().add(a);
                                    klijent_aktivne.getItems().add("ISTIČE--> " + a + " | Plaćeno: " + df.format(Double.parseDouble(r.getPlacena_cijena())) + " | Preostalo: " + df.format(r.getUkupna_cijena() - Double.parseDouble(r.getPlacena_cijena())));
                                    potrebno_platiti += r.getUkupna_cijena() - Double.parseDouble(r.getPlacena_cijena());
                                    placeno += Double.parseDouble(r.getPlacena_cijena());
                                } else {
                                    a_klijent_aktivne.getItems().add(a);
                                    potrebno_platiti += r.getUkupna_cijena() - Double.parseDouble(r.getPlacena_cijena());
                                    placeno += Double.parseDouble(r.getPlacena_cijena());
                                    klijent_aktivne.getItems().add(a + " | Plaćeno: " + df.format(Double.parseDouble(r.getPlacena_cijena())) + " | Preostalo: " + df.format(r.getUkupna_cijena() - Double.parseDouble(r.getPlacena_cijena())));
                                }
                            } else {
                                if (Double.parseDouble(r.getPlacena_cijena())<r.getUkupna_cijena() && Double.parseDouble(r.getPlacena_cijena())!=-1){
                                    placeno =  Double.parseDouble(r.getPlacena_cijena());
                                    double ostatak = placeno - (r.getUkupna_cijena()/2);
                                    a_klijent_otkazane.getItems().add(a);
                                    klijent_otkazane.getItems().add(a.toString());
                                    database.updatePlacenaRezervacija(client_id, a.getId(),-Double.parseDouble(r.getPlacena_cijena())-1 );
                                    database.updateStanjeRacuna(s_jmbg, dohvatiStanjeNovca(s_jmbg)+ostatak,11,dohvatiStanjeNovca("1102541293")-ostatak);
                                    balans.setText(String.valueOf(dohvatiStanjeNovca(s_jmbg)));
                                    s_istekao_aranzman += "- " + a.getPutovanje() + "\n";
                                    deadline2 = true;
                                }
                                else{
                                    a_klijent_protekle.getItems().add(a);
                                    klijent_protekle.getItems().add(a.toString());
                                    database.updatePlacenaRezervacija(client_id, a.getId(), -Double.parseDouble(r.getPlacena_cijena()) - 2);
                                }

                            }
                        } else if (Double.parseDouble(r.getPlacena_cijena()) == -1) {
                            a_klijent_otkazane.getItems().add(a);
                            klijent_otkazane.getItems().add(a.toString());
                        } else if (Double.parseDouble(r.getPlacena_cijena()) == -2) {
                            a_klijent_protekle.getItems().add(a);
                            klijent_protekle.getItems().add(a.toString());
                        }
                    }
                }
            }
        }
        if (a_klijent_aktivne.getItems().isEmpty()) {
            doplata.setText("0.0");
            arange_potroseno.setText("0.0");
        } else {
            doplata.setText(df.format(potrebno_platiti)); // Formatiranje potrebno_platiti na dvije decimale
            arange_potroseno.setText(df.format(placeno)); // Formatiranje placeno na dvije decimale
        }

    }


    // Obican alert
    private void notification(String poruka, Alert.AlertType tip) {
        Alert alert = new Alert(tip);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #D7F2FA; -fx-font-size: 14px;");
        alert.setTitle("Obavijest");
        alert.setHeaderText(null);
        alert.setContentText(poruka);
        dialogPane.getButtonTypes().setAll(buttonTypeOk);
        alert.showAndWait();
    }

    //Dohvatanje stanja novca bankovnih racuna
    @Override
    public double dohvatiStanjeNovca(String jmbg) {
        double stanje = 0.0;
        for(BankAcc b:database.listaBankovnihRacuna()){
            if(b.getJmb().equals(s_jmbg)){
                stanje = b.getBalance();
            }
            else if (b.getJmb().equalsIgnoreCase(jmbg)){
                stanje = b.getBalance();
            }
        }
        return stanje;
    }

    //Namjestanje rezervacije
    @FXML @Override
    public void setRezervacija(String naziv, Set<String> lista){
        naziv_aranzmana.setText(naziv);
        if (!lista.isEmpty()){
            for (String s:lista) {
                lista_klijenata.getItems().add(s);
            }
        }
        else{
            lista_klijenata.getItems().add("NIKO NIJE REZERVISAO");
        }

    }


    // Postavljane novca agencije u GUI-u

    @Override
    public void setAgencyBalance() {
        double balance = Database.getAgencyBalance();
        agency_balance.setText(balance + " KM");
    }


    // Efikasniji unos informacija u listu rezervacija
    @Override
    public String ispis_u_listu(Client c, int broj) {
        if (broj==-1)
            return "Ime: " +c.getName()+"  |  Prezime: "+c.getLname()+"  |  Telefon: "+c.getPhone() +"  |  Status: OTKAZANO";
        else if (broj==-2)
            return "Ime: " +c.getName()+"  |  Prezime: "+c.getLname()+"  |  Telefon: "+c.getPhone() +"  |  Status: PROTEKLA";
        else if (broj==0)
            return "Ime: " +c.getName()+"  |  Prezime: "+c.getLname()+"  |  Telefon: "+c.getPhone() +"  |  Status: PLAĆENO";

        return "Ime: " +c.getName()+"  |  Prezime: "+c.getLname()+"  |  Telefon: "+c.getPhone();
    }


    //SCENE:


    //Login window
    public void switchToScene1(ActionEvent event) throws IOException {
            root = FXMLLoader.load(getClass().getResource("login.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
        //Register window
    public void switchToScene2(ActionEvent event) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

    }

     //Client window
    public void switchToScene3(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ClientWindows.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    //Admin window
    public void switchToScene4(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AdminWindows.fxml"));
        currentAdminStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    //Add admin window
    public void openNewWindow(ActionEvent event) throws IOException {
        if (!isAddAdminOpen){

            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAdmin.fxml"));
            Parent root = loader.load();

            Stage noviProzorStage = new Stage();
            noviProzorStage.setScene(new Scene(root));

            noviProzorStage.setTitle("Dodaj admina");
            noviProzorStage.setResizable(false);
            noviProzorStage.setOnCloseRequest(e -> isAddAdminOpen = false);
            noviProzorStage.initModality(Modality.APPLICATION_MODAL);

            noviProzorStage.show();

            isAddAdminOpen = true;

            noviProzorStage.setOnCloseRequest(e -> {
                try {
                    currentAdminStage.close();
                    switchToScene4(event);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

        }

    }

    //Password change window
    public void changePassWindow(ActionEvent event) throws IOException {
        if (!isPassOpen){

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChangePass.fxml"));
            Parent root = loader.load();

            Stage noviProzorStage = new Stage();
            noviProzorStage.setScene(new Scene(root));

            noviProzorStage.setTitle("Promjena lozinke");
            noviProzorStage.setResizable(false);
            noviProzorStage.setOnCloseRequest(e -> isPassOpen = false);
            noviProzorStage.initModality(Modality.APPLICATION_MODAL);

            noviProzorStage.show();
            isPassOpen = true;

        }
    }


    //New Trip Window
    public void addnewTrip(ActionEvent event) throws IOException {
        if (!isTripOpen){

            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddNewTrip.fxml"));
            Parent root = loader.load();
            Stage noviProzorStage = new Stage();
            noviProzorStage.setScene(new Scene(root));

            noviProzorStage.setTitle("Dodavanje novog putovanja");
            noviProzorStage.setResizable(false);
            noviProzorStage.setOnCloseRequest(e -> isTripOpen = false);
            noviProzorStage.initModality(Modality.APPLICATION_MODAL);

            noviProzorStage.show();
            isTripOpen = true;

            noviProzorStage.setOnCloseRequest(e -> {
                try {
                    currentAdminStage.close();
                    switchToScene4(event);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });


        }
    }

    //New Izlet Window
    public void addnewAccomodation(ActionEvent event) throws IOException {
        if (!isIzletOpen) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddNewIzlet.fxml"));
            Parent root = loader.load();
            Stage noviProzorStage = new Stage();
            noviProzorStage.setScene(new Scene(root));

            noviProzorStage.setTitle("Dodavanje novog izleta");
            noviProzorStage.setResizable(false);

            noviProzorStage.setOnCloseRequest(e -> isIzletOpen = false);
            noviProzorStage.initModality(Modality.APPLICATION_MODAL);

            noviProzorStage.show();
            isIzletOpen = true;

            noviProzorStage.setOnCloseRequest(e -> {
                try {
                    currentAdminStage.close();
                    switchToScene4(event);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

    // Reservation list Window
    public void listReservation(MouseEvent event) throws IOException {
        if (!isReservationOpen) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReservationList.fxml"));
            Parent root = loader.load();
            Stage noviProzorStage = new Stage();
            noviProzorStage.setScene(new Scene(root));

            noviProzorStage.setTitle("Rezervacije");
            noviProzorStage.setResizable(false);

            noviProzorStage.setOnHidden(e -> isReservationOpen = false);
            noviProzorStage.initModality(Modality.APPLICATION_MODAL);

            noviProzorStage.show();

            isReservationOpen = true;
        }
    }

}
