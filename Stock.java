import org.apache.commons.text.WordUtils;
import org.apache.commons.lang3.StringUtils;

public class Stock{
    private String ticker;
    private int price;


    public Stock(String ticker, int price) {
        this.ticker = ticker;
        this.price = price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getPrice() {
        return price;
    }

}

//TODO: integrate stock brokers, remove toStrings, (add in main snd clear up main) create a display that tkes
// in stock ticker and number of stocks.