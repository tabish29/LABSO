import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ClientHandler implements Runnable {

    private Socket s;
    private Operation operation;
    private String senderName;
    private String receiverName;
    private Scanner from;
    private PrintWriter to;
    private Timer clock;
    private boolean sessionActive;
    private boolean closed;

    public ClientHandler(Socket s, Operation o) {
        this.s = s;
        this.operation = o;

    }

    @Override
    public void run() {
        try {

            from = new Scanner(s.getInputStream());
            to = new PrintWriter(s.getOutputStream(), true);

            to.println(
                    "Benvenuto nella nostra banca, dove offriamo due modalit\u00E0 di trasferimento dei fondi tra i conti aperti per soddisfare al meglio le tue esigenze finanziarie.\r\n"
                            + //
                            "1)Trasferimenti Standard: Con questa modalit\u00E0, puoi facilmente trasferire denaro tra i tuoi conti bancari con una semplice operazione. Basta selezionare l'importo da trasferire e specificare il conto mittente e destinatario. Questa opzione \u00E8 perfetta per transazioni immediate e veloci.\r\n"
                            + //
                            "2)Sessione Interattiva: Offriamo anche una sessione interattiva, che ti consente di avviare una comunicazione diretta tra due conti bancari. In questa modalit\u00E0, puoi interagire in tempo reale con il conto mittente e destinatario, facilitando un controllo pi\u00F9 approfondito delle transazioni e garantendo una maggiore sicurezza.\r\n"
                            + //
                            "\r\n" + //
                            "Siamo qui per rendere il trasferimento dei tuoi fondi facile e comodo, indipendentemente dalla modalità che preferisci utilizzare. La nostra banca si impegna a fornirti soluzioni finanziarie personalizzate e un'esperienza bancaria all'avanguardia. Grazie per aver scelto di fare affari con noi, siamo pronti ad assisterti in ogni passo del percorso.\r\n"
                            + //
                            "Per poter vedere la lista dei comandi e come devono essere utilizzati digitare \"info\" ");

            System.out.println("Info sul Thread connesso: " + Thread.currentThread() + " listening...");

            closed = false;
            sessionActive = false;

            while (!closed) {
                String request = from.nextLine();
                try {
                    if (!Thread.interrupted()) {
                        System.out.println("Request da parte del cliente: " + request);
                        String[] parts = request.split(" ");

                        if (sessionActive) {

                            switch (parts[0]) {
                                case ":move":
                                    if (parts.length == 2) {
                                        double amount = Double.parseDouble(parts[1]);
                                        clock();

                                        try {
                                            operation.interactiveSessionMOVE(senderName, receiverName, amount, to);
                                        }  catch (Exception e) { // Per metodo findAccount
                                            to.println(e.getMessage());
                                        }

                                    } else {
                                        to.println("Errore di sintassi. Uso corretto: :move <importo>");
                                    }
                                    break;

                                case ":end":
                                    try {
                                        operation.interactiveSessionEND(senderName, receiverName);
                                    } catch (Exception e) { // Per metodo findAccount
                                        to.println( e.getMessage());
                                    }

                                    clock.cancel();
                                    to.println(
                                            "Sessione interattiva terminata. Ora puoi utilizzare i comandi standard");
                                    sessionActive = false;
                                    break;

                                case "quit":
                                    try {
                                        operation.interactiveSessionEND(senderName, receiverName);
                                    } catch (Exception e) { // Per metodo findAccount
                                        to.println(e.getMessage());
                                    }

                                    to.println("Sessione interattiva terminata. Chiusura in corso...");
                                    sessionActive = false;

                                    closed = true;
                                    to.println("Connection closed");
                                    break;

                                default:
                                    to.println(
                                            "Comando non valido. La sessione interattiva è attiva. Utilizzare solo i comandi :move e :end");

                            }

                        } else {
                            switch (parts[0]) {

                                case "info":
                                    to.println("Lista dei comandi disponibili:");
                                    to.println("- list --> Per poter visualizzare la lista dei conti aperti");
                                    to.println(
                                            "- open --> Per poter aprire un conto (utilizzo: open <NomeConto> <Bilancio Iniziale>)");
                                    to.println(
                                            "- transfer --> Per trasferire dei soldi da un conto all'altro (utilizzo: transfer <importoDaTrasferire> <Mittente> <Destinatario>)");
                                    to.println(
                                            "- transfer_i --> Per avviare una sessione interattiva tra due conti (utilizzo: transfer_i <Mittente> <Destinatario>)");
                                    to.println("- quit --> Per chiudere la connessione con il server");
                                    to.println("Comandi disponibili nella sessione interattiva:");
                                    to.println(
                                            ":move --> Per poter spostare denaro tra il primo conto digitato e il secondo");
                                    to.println(":end --> Per concludere la sessione interattiva");
                                    break;

                                case "quit":
                                    closed = true;
                                    to.println("Connection closed");
                                    break;

                                case "list":
                                    to.println(operation.getAccountslist());
                                    break;

                                case "open":

                                    if (parts.length == 3) {
                                        String accountName = parts[1];
                                        double initialBalance = Double.parseDouble(parts[2]);

                                        if (operation.containsAccount(accountName)) {
                                            to.println("Errore: Il conto " + accountName + " già esiste");
                                            break;
                                        } else {
                                            operation.addAccount(accountName, initialBalance);
                                            to.println("Nuovo conto " + accountName + " creato con saldo iniziale di "
                                                    + initialBalance);
                                        }

                                    } else {
                                        to.println(
                                                "Errore di sintassi. Uso corretto del comando: open <nome_conto> <saldo_iniziale>");
                                    }
                                    break;
                                case "transfer":
                                    if (parts.length == 4) {

                                        senderName = parts[2];
                                        receiverName = parts[3];
                                        double amount = Double.parseDouble(parts[1]);

                                        try {

                                            operation.transferBalance(senderName, receiverName, amount, to);

                                        } catch (Exception e) { // Per metodo findAccount
                                            to.println(e.getMessage());
                                        }
                                    } else {
                                        to.println(
                                                "Errore di sintassi. Uso corretto: transfer <importo> <conto_mittente> <conto_destinatario> ");
                                    }
                                    break;

                                case "transfer_i":
                                    if (parts.length == 3) {
                                        senderName = parts[1];
                                        receiverName = parts[2];

                                        try {
                                            operation.startSection(senderName, receiverName, to);
                                            to.println(
                                                    "Sessione interattiva avviata. Puoi utilizzare i comandi: :move <importo> oppure :end");
                                            clock();
                                            sessionActive = true;
                                        } catch (Exception e) { // Per metodo findAccount
                                            to.println(e.getMessage());
                                        }
                                    } else {
                                        to.println(
                                                "Errore di sintassi. Uso corretto: transfer_i <conto_mittente> <conto_destinatario>");
                                    }
                                    break;
                                default:
                                    to.println(
                                            "Il comando digitato è sbagliato utilizzare il comando info per poter vedere il formato corretto del comando desiderato. Per la sessione interattiva puoi utilizzare i comandi: :move <importo> oppure :end");
                            }
                        }

                    }
                } catch (NoSuchElementException e) {
                    to.println("NoSuchElementException caught: " + e);

                } catch (IllegalArgumentException e) {
                    to.println("Il comando digitato è sbagliato utilizzare il comando info per poter vedere il formato corretto del comando desiderato. Per la sessione interattiva puoi utilizzare i comandi: :move <importo> oppure :end");
                }

            }
            to.println("quit");
            s.close();
            System.out.println("Closed");
        } catch (IOException e) {
            System.err.println("ClientHandler - IOException caught: " + e);

        }
    }

    public void clock() {

        if (clock != null) {
            clock.cancel(); 
        }
        clock = new Timer();
        TimerTask timer = new timer();
        clock.schedule(timer, 30000);

    }

    private class timer extends TimerTask {
        public void run() {
            try {
                operation.interactiveSessionEND(senderName, receiverName);
                sessionActive = false;
                to.println("Sessione interattiva terminata: timer scaduto");
            } catch (InterruptedException e) {
                to.println("InterruptedException caught: " + e);
            } catch (Exception e) {
                to.println(e.getMessage()); //Per findAccount
            }
        }
    }

}