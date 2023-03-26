import java.util.*;
import java.util.concurrent.Semaphore;

public class Trading extends Thread{
    public Trades t;
    public int brokers;
    public double balance;
    public Account ac;
    public Utility u = new Utility();

    public Trading(Trades tr, int b, double balance, Account a){
        this.t = tr;
        this.brokers = b;
        this.balance = balance;
        this.ac = a;
    }

    private Semaphore semaphore;


    public void run() {
        semaphore = new Semaphore(brokers);
        try {
            semaphore.acquire();
            sleep(t.getTime() * 1000);
            double totalC = t.getTradeAmt() * t.getCost();

            if(t.getTradeAmt() > 0) {
                if (ac.getBalance() >= totalC) {
                    System.out.println("[" + u.getZeroTimestamp() + "] Starting purchase of " + Math.abs(t.getTradeAmt()) + " stocks of " + t.getStock());
                    sleep(2000);
                    synchronized (ac) {
                        ac.buy(t.getCost(), t.getTradeAmt());
                        System.out.println("[" + u.getZeroTimestamp() + "] Finishing purchase of " + Math.abs(t.getTradeAmt()) + " stocks of " + t.getStock());
                        System.out.println("Current Balance after trade: " + ac.getBalance());
                    }
                } else {
                    System.out.println("Transaction failed due to insufficient balance. " +
                            "Unsuccessful purchase of " + t.getTradeAmt() + " stocks of " + t.getStock());
                }
            }
            else if(t.getTradeAmt() < 0){
                System.out.println("[" +u.getZeroTimestamp() +"] Starting sale of " + Math.abs(t.getTradeAmt()) + " stocks of " + t.getStock());
                sleep(3000);
                synchronized (ac) {
                    ac.sell(t.getCost(), Math.abs(t.getTradeAmt()));
                    System.out.println("[" + u.getZeroTimestamp() + "] Finishing sale of " + Math.abs(t.getTradeAmt()) + " stocks of " + t.getStock());
                    System.out.println("Current Balance after trade: " + ac.getBalance());
                }
            }

            //  System.out.println("Current Balance: " + balance);
        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
            System.out.println(e);
        }
        finally{
            semaphore.release();
        }


    }


}
