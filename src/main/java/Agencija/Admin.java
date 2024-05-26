package Agencija;

public class Admin {
    private int id;
    private String name;
    private String lastName;
    private String username;
    private String password;

    public Admin(int id, String name, String lastName, String username) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.password = "12345678";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }


    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

}
