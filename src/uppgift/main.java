package uppgift;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class main implements PaymentReceiver {
    public static String currency = "";
    public static String accountNumber = "";
    public static String amount = "";
    public static String reference = "";

    @Override
    public void startPaymentBundle(String accountNumber, Date paymentDate, String currency) {
        System.out.println("Starting payment.");

    }

    @Override
    public void payment(BigDecimal amount, String reference) {
        System.out.println("\nStarting out a payment: " + amount);
        System.out.println("Reference: " + reference);

    }

    @Override
    public void endPaymentBundle() {
        System.out.println("\nPayment complete");

    }


    public static void readFile(File file, main ex) {
        int startAccNumber = 0, endOfAccNumber = 0;


        String name = file.getName();

        if(name.contains("_inbetalningstjansten.txt")){
            currency = "SEK";
            try {
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    String posttyp = data.substring(0,2);

                    if(posttyp.equals("00")){
                        accountNumber = data.substring(15,24);
                        System.out.println("\nAccount number: " + accountNumber);
                        System.out.println("Currency: " + currency);
                    }
                    if(posttyp.equals("30")){
                        System.out.println("\nBelopp i Betalningspost: " + data.substring(3,22));
                        System.out.println("Betalningsreferens: "+ data.substring(41));

                    }
                    if(posttyp.equals("99")){
                        System.out.println("\nTotala pris: " + data.substring(3,22));
                        System.out.println("Antal Betalningspost: " + data.substring(31,38));
                    }
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        } else if(name.contains("_betalningsservice.txt")){
            try {
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    String posttyp = data.substring(0,1);

                    if(posttyp.equals("O")){
                        accountNumber = data.substring(2,16);
                        Date date = new SimpleDateFormat("yyyyMMdd").parse(data.substring(40,48));
                        currency = data.substring(48);

                        ex.startPaymentBundle(
                                accountNumber,
                                date,
                                currency
                        );
                    }

                    if(posttyp.equals("B")){
                        BigDecimal amountBig = formatAmount(data);
                        reference = data.substring(16);

                        ex.payment(amountBig,reference);
                    }
                }
                myReader.close();
            } catch (FileNotFoundException | ParseException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            finally {
                ex.endPaymentBundle();
            }
        } else {
            throw new java.lang.UnsupportedOperationException("Fileformat not supported yet.");
        }
    }

    private static BigDecimal formatAmount(String data){
        String amount = "";
        for(int i = 2; i<=15;i++){
            if(Character.isDigit(data.charAt(i))){
                amount = data.substring(i,15);
                amount = amount.replace(',','.');
                break;
            }
        }
        BigDecimal amountBig = new BigDecimal(amount);
        return amountBig;
    }


    public static void main(String[] args) {
        main ex = new main();
        File file = new File("Exempelfil_betalningsservice.txt");
        readFile(file,ex);

        file = new File("Exempelfil_inbetalningstjansten.txt");
        readFile(file,ex);
    }
}
