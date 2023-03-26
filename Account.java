public class Account {
    public static int balance;



    public Account(int b) {
        this.balance = b;
    }

    public static int getBalance() {
        return balance;
    }

    public static void setBalance(int balance) {
        Account.balance = balance;
    }

    public void buy(double cost, double totalS){
        double totalC = cost * totalS;
        if(balance >= totalC){
            balance -= totalC;
        }
    }

    public void sell(double cost, double totalS){
        double totalC = cost * totalS;
        balance += totalC;
    }
}
