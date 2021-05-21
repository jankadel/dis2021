package de.dis;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;


public class Main {

    public static void main(String[] args) {
        PersistenceManager pm = PersistenceManager.getInstance();
        // Debug to check for task 1.
        int page = 1;
        String data = "Hallooo";
        String taid = pm.beginTransaction();
        pm.write(taid, page, data);
        pm.commit(taid);
        String taid2 = pm.beginTransaction();
        pm.write(taid2, 2, "schwurbler1");
        pm.write(taid2, 2, "schwurbler2");
        pm.write(taid2, 2, "schwurbler3");
    }

    public static Logger getLogger() {
        Logger logger = Logger.getLogger("TRANSACTION LOGGER");  
        FileHandler fh;  

        try {  
            // This block configure the logger with handler and formatter  
            fh = new FileHandler("transactions.log", true);  
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);  

            // the following statement is used to log any messages  
            logger.info("--- LOG INITIALIZED ---");  

        } catch (SecurityException e) {  
            e.printStackTrace(); 
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return logger;
    }
}

