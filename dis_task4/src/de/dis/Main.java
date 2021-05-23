package de.dis;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;


public class Main {

    public static void main(String[] args) {
        PersistenceManager pm = PersistenceManager.getInstance();
        pm.recoveryCheck();
        System.out.println(Integer.toString(pm.getCounterFromPrevTA()));
        System.out.println(Integer.toString(pm.getBufferSize()));
        /*
        Client c1 = new Client(pm, 0, 9);
        Client c2 = new Client(pm, 10, 19);
        String[] content = {"String1", "String2", "String3", "String4"};
        String[] content2 = {"String5", "String7", "String9", "String8"};
        Client c3 = new Client(pm, 20, 29);
        String[] content3 = {"Das hier ist nicht committed", "Das auch nicht"};
        c1.autoTransaction(content);
        c2.autoTransaction(content2);
        String TAID = c3.startManualTransaction(content3);
        System.out.println("Uncommited TA: "+TAID);
        */
    }

    public static Logger getLogger() {
        Logger logger = Logger.getLogger("TRANSACTION LOGGER");  
        FileHandler fh;  

        try {  
            // This block configure the logger with handler and formatter. Add 'true' as parameter for append mode.
            fh = new FileHandler("transactions.log", true);  
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);

        } catch (SecurityException e) {  
            e.printStackTrace(); 
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return logger;
    }
}

