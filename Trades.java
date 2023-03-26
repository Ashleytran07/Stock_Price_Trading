public class Trades {
    public int time;
    public String stock;
    public int tradeAmt;
    public double cost;
    public Utility util = new Utility();

    public Trades(int t, String s, int trAmt, double c) {
        this.time = t;
        this.stock = s;
        this.tradeAmt = trAmt;
        this.cost = c;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public int getTradeAmt() {
        return tradeAmt;
    }

    public void setTradeAmt(int tradeAmt) {
        this.tradeAmt = tradeAmt;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String toString(){
        return("Trade:" + stock + ", Trade Amount: " + tradeAmt + ", Cost: " + cost);
    }

//    public String printIncompleteTrades(){
//        return("(" + time + ", " + stock + ", " + tradeAmt + ", " + util.getDateAndTime());
//    }
}