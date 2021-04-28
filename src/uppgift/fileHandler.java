package uppgift;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Scanner;

public class fileHandler {
    private static String currency = "";
    private static String accountNumber = "";
    private static String reference = "";
    private static String amount = "";
    private static String postType = "";

    private paymentReciever paymentReciever = new paymentReciever();

    public void readFile(File file) {
        clearVariables();
        String name = fileType(file.getName());
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                postType = getPostType(data,name);
                if(postType.equals("O")){
                    accountNumber = data.substring(2,16);
                    Date date = new SimpleDateFormat("yyyyMMdd").parse(data.substring(40,48));
                    currency = data.substring(48);
                    paymentReciever.startPaymentBundle(
                            accountNumber,
                            date,
                            currency
                    );
                } else if(postType.equals("B")){
                    BigDecimal amountBig = formatAmount(data,2,15);
                    reference = data.substring(16);
                    paymentReciever.payment(amountBig,reference);
                } else if(postType.equals("00")){
                    currency = "SEK";
                    accountNumber = data.substring(15,24);

                    paymentReciever.startPaymentBundle(accountNumber,null,currency);

                } else if(postType.equals("30")){
                    BigDecimal amountBig = formatAmount(data,3,22);
                    reference = data.substring(41);
                    paymentReciever.payment(amountBig,reference);

                }
                //Vad ska jag göra med posttypen 99 ?
                else if(postType.equals("99")){
                    System.out.println("\nTotala pris: " + data.substring(3,22));
                    System.out.println("Antal Betalningspost: " + data.substring(31,38));
                }
            }
            myReader.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } finally {
            paymentReciever.endPaymentBundle();
        }

    }

    private String fileType(String name){
        String fileType = "";
        for(int i = name.length()-1; i>0;i--){
            int compare = Character.compare(name.charAt(i),'_');
            if(compare == 0){
                fileType = name.substring(i);
            }
        }
        return fileType;
    }

    private void clearVariables(){
        String currency = "";
        String accountNumber = "";
        String reference = "";
        String amount = "";
        String postType = "";
    }

    private String getPostType(String data, String name) throws Exception {
        if(name.equals("_betalningsservice.txt")) return data.substring(0,1);
        else if(name.equals("_inbetalningstjansten.txt")) return data.substring(0,2);
        else throw new Exception("Cant handle " + name);
    }

    private BigDecimal formatAmount(String data,int amountStart, int amountEnd){
        String amount = "";
        for(int i = amountStart; i<=amountEnd;i++){
            if(Character.isDigit(data.charAt(i))){
                amount = data.substring(i,amountEnd);
                amount = amount.replace(',','.');
                break;
            }
        }
        BigDecimal amountBig = new BigDecimal(amount);
        return amountBig;
    }
}
