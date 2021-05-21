package de.dis;

import java.util.UUID;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

class PersistenceManager {    
    static final private PersistenceManager PERSISTENCE_MANAGER;
    static {
        try {
            PERSISTENCE_MANAGER = new PersistenceManager();
        }
        catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private final Logger logger = Main.getLogger();
    private SynchronizedCounter counter = new SynchronizedCounter();

    private int bufferThreshold = 5;
    // HashMap<TAID, PAGEID, DATA, LOGENTRY>
    public HashMap<Integer, String> buffer = new HashMap<Integer, String>();

    private PersistenceManager() {}

    public void setThreshold(int newThreshold) {
        bufferThreshold = newThreshold;
    }

    public int getThreshold() {
        return bufferThreshold;
    }

    /*
        starts a new transaction. The persistence manager creates a unique transaction
        ID and returns it to the client.
    */
    public String beginTransaction() {
        String TAID = UUID.randomUUID().toString();
        int LSN = counter.val();
        String log = "BOT|LSN:"+LSN+"|TAID:"+TAID;
        counter.incr();
        logger.info(log);
        writeBuffer(buffer, LSN, log);
        return TAID;
    }

    /*
        writes the given data with the given page ID on behalf of the given transaction to the buffer. 
        If the given page already exists, its content is replaced completely by the given data.
    */
    public void write(String TAID, int PAGEID, String DATA) {
        if (!DATA.contains("COMMIT|") || !DATA.contains("WRITE|") || !DATA.contains("BOT|")) {
            int LSN = counter.val();
            counter.incr();
            String log = "WRITE|LSN:"+LSN+"|TAID:"+TAID+"|PAGEID:"+PAGEID+"|DATA:"+DATA;
            logger.info(log);
            writeBuffer(buffer, LSN, log);
        }
    }

    /*
        commits the transaction specified by the given transaction ID        
    */
    public void commit(String TAID) {
        int LSN = counter.val();
        counter.incr();
        String log = "COMMIT|LSN:"+LSN+"|TAID:"+TAID;
        logger.info(log);
        writeBuffer(buffer, LSN, log);
    }

    private void writeBuffer(HashMap<Integer, String> Buffer, int LSN, String LogEntry) {
        Buffer.put(LSN, LogEntry);
        if (Buffer.size() > 5) {
            persistenceCheck(Buffer);
        }
    }
    
    private void persistenceCheck(HashMap<Integer, String> Buffer) {
        Set<Integer> keys = Buffer.keySet();
        //store TAIDs of commits
        ArrayList<String> commits = new ArrayList<String>();
        //Store write actions of transactions since BOT and EOT are not written to pages
        ArrayList<String[]> transactions = new ArrayList<String[]>();

        //yield TAIDs of commits
        for (Integer k : keys) {
            String entry = Buffer.get(k);
            String segments[] = entry.split("\\|");

            if (segments[0].equals("COMMIT")) {
                commits.add(segments[2]);
            }        
        }

        //yield transactions from UUIDs
        for (String UUID : commits) {
            for (Integer k : keys) {
                String entry = Buffer.get(k);
                if (entry.contains(UUID)) {
                    String segments[] = entry.split("\\|");
                    transactions.add(segments);
                }
            }
        }
        
        //finally persist committed transactions and remove related entries from buffer
        for (String[] ta : transactions) {
            Integer LSN = Integer.parseInt(ta[1].split(":")[1]);
            if (ta.length == 5)  {
                Integer PAGEID = Integer.parseInt(ta[3].split(":")[1]);
                String DATA = ta[4].split(":")[1];
                try {
                    String path = "./pages/page-"+PAGEID+".txt";
                    File f = new File(path);
                    FileWriter fw = new FileWriter(path);
                    if (f.createNewFile()) {
                        fw.write("PAGEID:"+PAGEID+"|LSN:"+LSN+"|DATA:"+DATA);
                    } else {
                        fw.write("PAGEID:"+PAGEID+"|LSN:"+LSN+"|DATA:"+DATA);
                    }
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Buffer.remove(LSN);
        }
    }

    static public PersistenceManager getInstance(){
        return PERSISTENCE_MANAGER;
    }
}
