import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client extends Thread {
    static String hostName = "";
    static Scanner scan = new Scanner(System.in);
    private BufferedReader br;
    private PrintWriter pw;
    static int serverPort;
    public Client(String hostName, int port) throws IOException {
        try {
            //System.out.println("Trying to connect to " + hostName + ":" + port);
            Socket s = new Socket(hostName, port);
            //System.out.println("Connected to " + hostName + ":" + port + "\n");
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            pw = new PrintWriter(s.getOutputStream());
            this.start();
        } catch(IOException ioe){
            System.out.println("ioe in Client constructor" + ioe.getMessage());
        }
    }

    public void run(){
        try{
            while(true){
                String line = br.readLine();
                if(line != null) {
                    System.out.println(line);
                }
            }
        } catch(IOException ioe){
            System.out.println("ioe in Client.run(): " + ioe.getMessage());
        }

    }

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to SalStocks v2.0!");
        System.out.println("Enter the name of the hostname:");
        hostName = scan.nextLine();
        System.out.println("Enter the server port:");
        serverPort = Integer.parseInt(scan.nextLine());
        Client c = new Client(hostName, serverPort);
        //System.out.println(sizeofTraders - t + " more trader is needed before the service can begin.\nWaiting...");

    }
}
