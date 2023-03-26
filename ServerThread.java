import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class ServerThread extends Thread{
    public Utility utility = new Utility();
    public int count;
    public int profit;
    public Trader t;
    public Vector<Trades> WaitingTrades = new Vector<Trades>();
    static Scanner scan = new Scanner(System.in);
    private BufferedReader br;
    private PrintWriter pw;
    private TradeRoom tr;
    public boolean isTrading;



    public ServerThread(Socket s, TradeRoom tr, Trader t){
        this.t = t;
        try{
            this.tr = tr;
            pw = new PrintWriter(s.getOutputStream());
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.start();
        }catch(IOException ioe){
            System.out.println("ioe in ServerThread constructor:" + ioe.getMessage());
        }
    }

    public void run() {
        try{
            while(true) {
                if(!WaitingTrades.isEmpty()) {
                    count++;
                    double payment = Math.abs(WaitingTrades.get(0).getTradeAmt()) * WaitingTrades.get(0).getCost();
                    if (WaitingTrades.get(0).getTradeAmt() >= 0) {
                        isTrading = true;
                        pw.println("[" + utility.getZeroTimestamp() + "] Starting purchase of " + Math.abs(WaitingTrades.get(0).getTradeAmt())
                                + " stock(s) of " + WaitingTrades.get(0).getStock() + ". Total cost estimate = " +  Math.abs(WaitingTrades.get(0).getCost()) + " * "
                                +  Math.abs(WaitingTrades.get(0).getTradeAmt()) + " = " + payment);
                        sleep(1000);
                        pw.println("[" + utility.getZeroTimestamp() + "] Finished purchase of " + Math.abs(WaitingTrades.get(0).getTradeAmt())
                                + " stock(s) of " + WaitingTrades.get(0).getStock());
                        WaitingTrades.remove(0);
                        isTrading = false;
                    } else {
                        isTrading = true;
                        pw.println("[" + utility.getZeroTimestamp() + "] Starting sale of " + Math.abs(WaitingTrades.get(0).getTradeAmt())
                                + " stock(s) of " + WaitingTrades.get(0).getStock() + ". Total gain estimate = " + WaitingTrades.get(0).getCost() + " * "
                                +  Math.abs(WaitingTrades.get(0).getTradeAmt()) + " = " + payment);
                        sleep(1000);
                        pw.println("[" + utility.getZeroTimestamp() + "] Finished sale of " + Math.abs(WaitingTrades.get(0).getTradeAmt())
                                + " stock(s) of " + WaitingTrades.get(0).getStock());
                        isTrading = false;
                        profit += payment;
                        WaitingTrades.remove(0);

                    }
                }
            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        }


    public void sendMessage(String message) {
        pw.println(message);
        pw.flush();
    }
}

