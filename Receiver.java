import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class Receiver implements Runnable {

    private Socket s;
    private Thread sender;
    private Scanner from;
    private boolean open;

    public Receiver(Socket s, Thread sender) {
        this.s = s;
        this.sender = sender;
    }

    @Override
    public void run() {
        try {
            from = new Scanner(this.s.getInputStream());
            open = true;
            while (open) {  
                String response = from.nextLine();
                if (response.equals("quit")) {
                    open = false;
                    break;
                }
                System.out.println(response);
            }
        } catch (IOException e) {
            System.err.println("(RECEIVER)IOException caught: " + e);
        } catch(NoSuchElementException e){
            System.out.println("Server non raggiungibile");
        } finally {
            System.out.println("Receiver closed");
            this.sender.interrupt();
        }
    }
}
