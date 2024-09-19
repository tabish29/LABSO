import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckingAccount {
    private String accountName;
    private double balance;
    private String lastTransaction;

    // Costruttore della classe CheckingAccount
    public CheckingAccount(String accountName, double initialBalance) {
        this.accountName = accountName;
        this.balance = initialBalance;
        lastTransaction = "Nessuna transazione effettuata";
    }

    // Metodi Get
    public String getAccountName() {
        return accountName;
    }

    public double getBalance() {
        return balance;
    }

    public String getlastTransaction() {
        return lastTransaction;
    }
    
    // Metodi Set

    private void setlastTransaction(String s) {
        lastTransaction = s;
    }

    private void deposit(double ammount) {
        balance += ammount;
    }

    public String moveMoney(CheckingAccount receiver, double ammount) {
        if (ammount <= balance) {
            balance -= ammount;
            receiver.deposit(ammount);

            Date dateLastTransaction = new Date();
            this.lastTransaction = "DATA: " + formatDate(dateLastTransaction) + ",AMMONTARE: -" + ammount
                    + ", ALTRO CONTO: " + receiver.getAccountName();

            receiver.setlastTransaction("DATA: " + formatDate(dateLastTransaction) + ",AMMONTARE: +" + ammount
                    + ", ALTRO CONTO: " + this.getAccountName());
            return "Transazione effettuata con successo";
        } else {
            return "Saldo insufficiente per effettuare il trasferimento";
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }

}