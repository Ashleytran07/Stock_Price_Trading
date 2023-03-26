public class Trader extends Thread{
    int serialNum;
    double balance;

    public Trader(int serialNum, double balance) {
        this.serialNum = serialNum;
        this.balance = balance;
    }

    public int getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String toString(){
        return("Serial Number:" + serialNum + ", balance: " + balance);
    }
}
