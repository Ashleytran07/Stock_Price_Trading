import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Array;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class TradeRoom extends Response {
    public Utility util = new Utility();
    private Vector<ServerThread> serverThreads;
    private static ArrayList<Trades> tradeList = new ArrayList<Trades>();
    private static ArrayList<Trader> tradersList = new ArrayList<Trader>();

    public TradeRoom(int port) throws IOException {
        Scanner scan = new Scanner(System.in);
        tradeList = getScheduleCSV();
        //System.out.println("this is tradelist from schedule: " + tradeList);
        tradersList = getTradersCSV();
        int sizeofTraders = tradersList.size();
        //System.out.println("this is traderslist from traders " + tradersList);
        try {
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Listening on " + port + ".\nWaiting for traders...");
            serverThreads = new Vector<ServerThread>();

            int j = 0;
            while (serverThreads.size() < sizeofTraders) {
                Socket s = ss.accept();
                System.out.println("Connection from: " + s.getInetAddress());
                ServerThread st = new ServerThread(s, this, tradersList.get(j));
                serverThreads.add(st);
                if (serverThreads.size() != sizeofTraders) {
                    System.out.println("Waiting for " + (sizeofTraders - serverThreads.size()) + " more trader(s)...");
                    broadcast((sizeofTraders - serverThreads.size()) + " more trader is needed before the service can begin.\nWaiting...", serverThreads.get(0));
                } else if (serverThreads.size() == sizeofTraders) {
                    System.out.println("Starting Service.\nProcessing Complete.");
                    for (int i = 0; i <= serverThreads.size() - 1; i++) {
                        broadcast("All traders have arrived!\nStarting service.", serverThreads.get(i));
                    }
                }
                j++;
            }
        } catch (IOException ioe) {
            System.out.println("ioe in TradeRoom constructor: " + ioe.getMessage());
        }
    }

    public void broadcast(String message, ServerThread st) {
        if (message != null) {
            //System.out.println(message);
            for (ServerThread threads : serverThreads) {
                if (st == threads) {
                    threads.sendMessage(message);
                }
            }
        }
    }

    public static ArrayList<Trader> getTradersCSV() throws IOException {
        String splitBy = ",";
        String nameCSV = "";
        boolean flag = false;
        ArrayList<Trader> traders = new ArrayList<Trader>();
        while (!flag) {
            try {
                Scanner scan = new Scanner(System.in);
                System.out.println("What is the path of the traders file?");
                nameCSV = scan.nextLine();
                Scanner br = new Scanner(new File(nameCSV));
                String csvLines = br.nextLine();
                while (csvLines != "") {
                    String[] traderz = csvLines.split(splitBy);
                    Trader obj = new Trader(Integer.parseInt(traderz[0]), Double.parseDouble(traderz[1]));
                    traders.add(obj);
                    flag = true;
                    csvLines = "";
                    if (br.hasNextLine()) {
                        csvLines = br.nextLine();
                    }
                }
                System.out.println("The file has been properly read.\n");
                br.close();

            } catch (FileNotFoundException fnfe) {
                System.out.println("The file " + nameCSV + " could not be found\n");
            }
        }
        return traders;
    }

    public static ArrayList<Trades> getScheduleCSV() throws IOException {
        String splitBy = ",";
        String nameCSV = "";

        boolean flag = false;
        ArrayList<Trades> tradesL = new ArrayList<Trades>();
        while (!flag) {
            try {
                Scanner scan = new Scanner(System.in);
                System.out.println("What is the path of the schedule file?");
                nameCSV = scan.nextLine();
                Scanner br = new Scanner(new File(nameCSV));
                String csvLines = br.nextLine();
                while (csvLines != "") {
                    String[] trades = csvLines.split(splitBy);
                    Trades obj = new Trades(Integer.parseInt(trades[0]), trades[1],
                            Integer.parseInt(trades[2]), 0);
                    tradesL.add(obj);
                    flag = true;
                    csvLines = "";
                    if (br.hasNextLine()) {
                        csvLines = br.nextLine();
                    }
                }
                setCosts(tradesL);
                System.out.println("\nThe file has been properly read.\n");
                br.close();

            } catch (FileNotFoundException fnfe) {
                System.out.println("The file " + nameCSV + " could not be found\n");
            }
        }
        return tradesL;
    }

    public static int getBalance(int b) {

        return b;
    }

    public static String getStockString(String s) throws IOException {
        URL curl = new URL("https://finnhub.io/api/v1/quote?symbol=" + s + "&token=cdia9q2ad3i4h8m0hhfgcdia9q2ad3i4h8m0hhg0");
        HttpURLConnection con = (HttpURLConnection) curl.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // print result
            return (response.toString());
        } else {
            return ("GET request not worked");

        }
    }

    public static double getStockPrice(String jsonString) {
        double price = 0;
        Gson g = new Gson();
        JsonObject json = g.fromJson(jsonString, JsonObject.class);
        price = json.get("c").getAsDouble();
        return price;
    }

    public static ArrayList<Trades> setCosts(ArrayList<Trades> tradeList) throws IOException {
        //get stock api urls
        String AAPLurl = getStockString("AAPL");
        String AMDurl = getStockString("AMD");
        String MSFTurl = getStockString("MSFT");
        String TSLAurl = getStockString("TSLA");
        //get stock prices
        Double AAPLprice = getStockPrice(AAPLurl);
        Double AMDprice = getStockPrice(AMDurl);
        Double MSFTprice = getStockPrice(MSFTurl);
        Double TSLAprice = getStockPrice(TSLAurl);
        //insert in map
        Map<String, Double> Stocks = new HashMap<String, Double>();
        Stocks.put("AAPL", AAPLprice);
        Stocks.put("AMD", AMDprice);
        Stocks.put("MSFT", MSFTprice);
        Stocks.put("TSLA", TSLAprice);
        //System.out.println(Stocks);
        for (Trades t : tradeList) {
            if (Stocks.containsKey(t.getStock())) {
                double cost = Stocks.get(t.getStock());
                t.setCost(Stocks.get(t.getStock()));
            }
        }
        return tradeList;
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        TradeRoom t = new TradeRoom(3456);
        Instant start = Instant.now();


        while (!tradeList.isEmpty()) {
            int time = tradeList.get(0).getTime();
          
            while (Duration.between(start, Instant.now()).toMillis() > (time * 1000)) {
                
                    for (ServerThread s : t.serverThreads) {
                        //CLIENT 1
                        if (!s.isTrading) {
                            double payment = Math.abs((tradeList.get(0).getTradeAmt()) * (tradeList.get(0).getCost()));
                            double balance = s.t.getBalance();

                            if (tradeList.get(0).getTradeAmt() >= 0) {
                                if (balance >= payment) {
                                    //ASSIGN
                                    s.sendMessage("[" + t.util.getZeroTimestamp() + "] Assigning purchase of " + Math.abs(tradeList.get(0).getTradeAmt())
                                            + " stock(s) of " + tradeList.get(0).getStock() + ". Total cost estimate = " + tradeList.get(0).getCost() + " * "
                                            + Math.abs(tradeList.get(0).getTradeAmt()) + " = " + payment);
                                    s.WaitingTrades.add(tradeList.get(0));
                                    s.t.balance -= payment;
                                    tradeList.remove(0);
                                    break;
                                    //IF CAN AFFORD, START PURCHASE
                                }
                            } else {
                                s.sendMessage("[" + t.util.getZeroTimestamp() + "] Assigning sale of " + Math.abs(tradeList.get(0).getTradeAmt())
                                        + " stock(s) of " + tradeList.get(0).getStock() + ". Total gain estimate = " + tradeList.get(0).getCost() + " * "
                                        + Math.abs(tradeList.get(0).getTradeAmt()) + " = " + payment);
                                s.WaitingTrades.add(tradeList.get(0));
                                tradeList.remove(0);
                                break;
                            }
//                            s.sendMessage("[" + t.util.getZeroTimestamp() + "] Incomplete trades: (" + tradeList.get(i).getTime() + ", "
//                                    + tradeList.get(i).getStock() + ", " + tradeList.get(i).getTradeAmt()
//                                    + ", " + t.util.getDateAndTime());
                            //TODO: create vector of incomplete trades and print total profit earned at the end
                        }

                    } // ENDFOR serverthreads
                }
                if (!tradeList.isEmpty()) {
                    time = tradeList.get(0).getTime();
}
            }
        }
    } // ends main
}// ends TradeRoom class