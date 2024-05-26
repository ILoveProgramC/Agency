package konsultacije;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class Database {
    private static String DB_user = "root";
    private static String DB_password = "";
    private static String connectionUrl;
    private static int port = 3306;
    private static String DB_name = "agencija";
    private static Connection connection;

    public static void DBConnect() throws SQLException /*, ClassNotFoundException*/ {
        //Class.forName("com.mysql.cj.jdbc.Driver");
        connectionUrl = "jdbc:mysql://localhost" + ":" + port + "/" + DB_name;
        connection = DriverManager.getConnection(connectionUrl, DB_user, DB_password);
    }

    public static void main(String[] args) {
        try {
            DBConnect();
            System.out.println("Uspjesno ste se konektovali na bazu:" + connectionUrl);
            ResultSet resultSet = null;
            Statement statement = connection.createStatement();
            String SQLQuery = "SELECT * FROM admin";
            resultSet = statement.executeQuery(SQLQuery);
            System.out.println("--------------------------------------------");
            while (resultSet.next()) {
                String result = resultSet.getString(1) + ", " + resultSet.getString(2)
                        + ", " + resultSet.getString(3) + ", " + resultSet.getString(4)
                        + ", " + resultSet.getString(5);
                System.out.println(result);
                System.out.println("--------------------------------------------");
            }

            statement.close();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        } //catch (ClassNotFoundException e) {
            //throw new RuntimeException(e);
        //}
    }
}
