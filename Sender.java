import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Sender implements Runnable {

    private Socket s;
    private PrintWriter to;
    private Scanner userInput;

    public Sender(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        userInput = new Scanner(System.in);

        try {
            to = new PrintWriter(this.s.getOutputStream(), true);
            while (true) {
                String request = userInput.nextLine();
                if (Thread.interrupted()) {
                    to.println("quit");
                    break;
                }

                to.println(request);

                if (request.equals("quit")) {
                    break;
                }
            }
            System.out.println("Sender closed");
        } catch (IOException e) {
            System.err.println("(SENDER)IOException caught: " + e);
        } finally {
            userInput.close();
        }
    }

}
