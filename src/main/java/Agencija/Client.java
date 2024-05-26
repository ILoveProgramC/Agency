package Agencija;

public class Client {
    private String name;
    private String lname;
    private String phone;
    private String JMBG;
    private String accNUM;
    private String user;
    private String password;
    private int id;



    public Client(int id,String name, String lname, String phone, String JMBG, String accNUM, String user, String password) {
        this.name = name;
        this.lname = lname;
        this.phone = phone;
        this.JMBG = JMBG;
        this.accNUM = accNUM;
        this.user = user;
        this.password = password;
        this.id=id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getLname() {
        return lname;
    }

    public String getPhone() {
        return phone;
    }

    public String getJMBG() {
        return JMBG;
    }

    public String getAccNUM() {
        return accNUM;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

}
