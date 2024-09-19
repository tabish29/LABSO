import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java Client <host> <port>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            Socket s = new Socket(host, port);
            System.out.println("Connected to server");

            Thread sender = new Thread(new Sender(s));
            Thread receiver = new Thread(new Receiver(s, sender));

            sender.start();
            receiver.start();

            try {
                sender.join();
                receiver.join();
                s.close();
                System.out.println("Socket closed");
            } catch (InterruptedException e) {
                return;
            }

        } catch (IOException e) {
            System.err.println("IOException: Connessione con il server non avvenuta");
        }
    }
}
