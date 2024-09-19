import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class Operation{

    private List<CheckingAccount> checkingAccounts; 
    private List<CheckingAccount> busies;
    
    public Operation(){
        checkingAccounts = new ArrayList<>();
        busies = new ArrayList<>();
    }

    public synchronized List<CheckingAccount> getList(){
        return checkingAccounts;
    }

    public synchronized void addAccount(String accountName, double initialBalance){
        checkingAccounts.add(new CheckingAccount(accountName, initialBalance));
    }

    public synchronized String getAccountslist(){
        
        StringBuilder responseList = new StringBuilder("");
        if (checkingAccounts.isEmpty()) {
            return "Non ci sono conti aperti al momento";
        } else {
            for (CheckingAccount account : checkingAccounts) {
                responseList.append("\n").append(account.getAccountName()).append(": ").append(account.getBalance() + " ").append("\n").append(account.getlastTransaction() + " ");
            }
            return "Elenco dei conti presenti:" + responseList.toString();
        }
    }

    public synchronized boolean containsAccount (String accountName){
        for (CheckingAccount account : checkingAccounts) {
            if (account.getAccountName().equals(accountName)) {
                return true;
            }
        }
        return false;
    }

    public synchronized CheckingAccount findAccount(String accountName)throws Exception{
        for (CheckingAccount account : checkingAccounts) {
            if (account.getAccountName().equals(accountName)) {
                return account;
            } 
        }
        throw new Exception("Errore, uno dei due conti è inesistente");
    }

    public synchronized void transferBalance(String s, String r, double amount, PrintWriter to) throws Exception{ 
        CheckingAccount sender = findAccount(s);
        CheckingAccount receiver = findAccount(r);

        while (busies.contains(sender) || busies.contains(receiver)) {
            to.println("Account occupato, attendere...");
            wait();
    
        }

        busies.add(sender);
        busies.add(receiver);
        to.println(sender.moveMoney(receiver, amount));

        busies.remove(sender);
        busies.remove(receiver);
   
    }

    public synchronized void startSection(String s, String r, PrintWriter to) throws Exception{

        CheckingAccount sender = findAccount(s);
        CheckingAccount receiver = findAccount(r);

        while(busies.contains(sender) || busies.contains(receiver)){
            to.println("Account occupato, attendere...");
            wait();
        }

        busies.add(sender);
        busies.add(receiver);

        notifyAll();  
    }

    public synchronized void interactiveSessionMOVE(String s, String r, Double amount, PrintWriter to) throws Exception{
        CheckingAccount sender = findAccount(s);
        CheckingAccount receiver = findAccount(r);
        to.println(sender.moveMoney(receiver, amount));
        
    }

    public synchronized void interactiveSessionEND(String s, String r)throws Exception{
        
        CheckingAccount sender = findAccount(s);
        CheckingAccount receiver = findAccount(r);

        busies.remove(sender);
        busies.remove(receiver);

        notifyAll();
    }
    
    

}