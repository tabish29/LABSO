import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Server <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);
        Scanner userInput = new Scanner(System.in);

        try {
            ServerSocket server = new ServerSocket(port);
            
            Thread serverThread = new Thread(new SocketListener(server));
            serverThread.start();

            String command = "";

            while (!command.equals("quit")) {
                command = userInput.nextLine();
            }

            try {
                serverThread.interrupt();
                serverThread.join();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught: " + e);
                return;
            }
            System.out.println("Server Chiuso");
        } catch (IOException e) {
            System.err.println("(SERVER)IOException caught: " + e);
        }
        finally {
            userInput.close();
            System.exit(0);
        }
    }
}