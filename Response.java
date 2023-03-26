import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Response {
    public static ArrayList<Stock> dataList;

//    public int getBrokerNum(String choice)
//    {
//        for(int i = 0; i < dataList.size(); i++){
//            if (choice.equals(dataList.get(i).getTicker())){
//                // System.out.println(dataList.get(i).getStockBrokers());
//                return dataList.get(i).getStockBrokers();
//            }
//        }
//        return -1 ;
//
//    }
    public void findStock(String choice){
        boolean flag =false;

        for(int i = 0; i < dataList.size(); i++){
            if (choice.equals(dataList.get(i).getTicker())){
                flag = true;
                //System.out.println(dataList.get(i).toStringWithoutDesc());

            }
            else{
                System.out.println("\n" + choice + " could not be found.");
            }
            break;
        }
    }


    public void jsonToJavaOb(String fileName) throws FileNotFoundException {
        try {
            Gson gson = new Gson();
            File f = new File(fileName);
            Reader reader = Files.newBufferedReader(Paths.get(fileName));
            dataList = new Gson().fromJson(reader,new TypeToken<ArrayList<Stock>>(){}.getType());
            //  dataList.forEach(System.out::println);
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public String javaObToJson() {
        String json = new Gson().toJson(dataList);
        json = "{ \"data\": " + json + "}";
        return json;
    }

    public void addStock(Stock newS)
    {
        dataList.add(newS);
    }

    public void removeStock(int removeStock){
        dataList.remove(removeStock);
    }

    public void displayData(String fileName){
        dataList.forEach(System.out::println);
    }

    public ArrayList<Stock> getData() {
        return dataList;
    }

    public void setData(ArrayList<Stock> data) {
        this.dataList = dataList;
    }

}