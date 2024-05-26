module com.example.oopagencija {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens Agencija to javafx.fxml;
    exports Agencija;
    exports Agencija.GUI;
    opens Agencija.GUI to javafx.fxml;
}