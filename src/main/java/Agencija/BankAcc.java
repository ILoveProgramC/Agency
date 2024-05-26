package Agencija;

public class BankAcc {

    private int id;
    private String jmb;
    private String accNUM;
    private double balance;

    public BankAcc(int id, String accNUM, String jmb, double balance) {
        this.id = id;
        this.balance = balance;
        this.accNUM = accNUM;
        this.jmb = jmb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccNUM() {
        return accNUM;
    }

    public String getJmb() {
        return jmb;
    }


    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
