package uppgift;

import java.io.File;


public class main {
    public static void main(String[] args) {
        fileHandler test = new fileHandler();

        test.readFile(new File("Exempelfil_betalningsservice.txt"));
        test.readFile(new File("Exempelfil_inbetalningstjansten.txt"));

    }
}
