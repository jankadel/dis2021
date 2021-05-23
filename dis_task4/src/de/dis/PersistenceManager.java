package de.dis;

import java.util.UUID;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
    // HashMap<Integer LSN, String LOG-Entry>
    private HashMap<Integer, String> buffer = new HashMap<Integer, String>();

    private PersistenceManager() {}

    public void setThreshold(int newThreshold) {
        this.bufferThreshold = newThreshold;
    }

    public int getThreshold() {
        return bufferThreshold;
    }

    public int getBufferSize() {
        return buffer.size();
    }

    public int getCounterFromPrevTA() {
        return counter.val();
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
        if (Buffer.size() > bufferThreshold) {
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

    /*
        Recovery only for uncommited transactions!
    */
    public void recoveryCheck() {
        String path = "transactions.log";
        ArrayList<String> logs = new ArrayList<String>();
        ArrayList<String> commits = new ArrayList<String>();
        ArrayList<String> uncommitted = new ArrayList<String>();
        //Read Logfile and store correctly formatted entries.
        try {
            File logfile = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(logfile));
            String line = reader.readLine();
            while(line != null) {
                if(line.contains("INFORMATION:")) {
                    String log = line.split("NFORMATION:")[1];
                    logs.add(log);
                }
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Yield commits
        for (String l : logs) {
            String segments[] = l.split("\\|");
            if (segments[0].contains("COMMIT")) {
                commits.add(segments[2]);
            }
        }
        //Yield uncommitted transactions by TAID
        String cs[] = new String[commits.size()];
        for (int i = 0; i < commits.size(); i++) {
            cs[i] = commits.get(i);
        }

        for (String l : logs) {
            if(!Arrays.stream(cs).anyMatch(l::contains)) {
                if(!uncommitted.contains(l)) {
                    uncommitted.add(l);
                }
            }
        }
        //Redo write operations
        for (String u : uncommitted) {
            String segments[] = u.split("\\|");
            if (segments[0].contains("WRITE")) {
                Integer LSN = Integer.parseInt(segments[1].split(":")[1]);
                counter.setCounter(LSN);
                writeBuffer(buffer, LSN, u);
            }
        }
    }

    /*
        Recovery for all operations
    */
    public void fullRecovery() {
        String path = "transactions.log";
        ArrayList<String> logs = new ArrayList<String>();
        
        //Read Logfile and store correctly formatted entries.
        try {
            File logfile = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(logfile));
            String line = reader.readLine();
            while(line != null) {
                if(line.contains("INFORMATION:")) {
                    String log = line.split("NFORMATION:")[1];
                    logs.add(log);
                }
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String l : logs) {
            String[] segments = l.split("\\|");
            writeBuffer(buffer, Integer.parseInt(segments[1].split(":")[1]), l.replaceAll(" ", ""));
        }
    }

    static public PersistenceManager getInstance(){
        return PERSISTENCE_MANAGER;
    }
}
