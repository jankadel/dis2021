package de.dis;

public class Client extends Thread {
    private PersistenceManager pm;
    private final Integer lowerPage;
    private final Integer upperPage;
    private Integer currentPage;

    public Client(PersistenceManager pManager, Integer lowerPage, Integer upperPage) {
        this.pm = pManager;
        this.lowerPage = lowerPage;
        this.upperPage = upperPage;
        this.currentPage = -1;
    }

    public void autoTransaction(String[] content) {
        String TAID = pm.beginTransaction();
        for (String c : content) {
            determinePageID(lowerPage, upperPage, this.currentPage);
            pm.write(TAID, this.currentPage, c);
        }
        pm.commit(TAID);
    }

    public String startManualTransaction(String[] content) {
        String TAID = pm.beginTransaction();
        for (String c : content) {
            determinePageID(lowerPage, upperPage, this.currentPage);
            pm.write(TAID, this.currentPage, c);
        }
        return TAID;
    }

    public void continueManualTransaction(String[] content, String TAID, Boolean commitFlag) {
        for (String c : content) {
            determinePageID(lowerPage, upperPage, this.currentPage);
            pm.write(TAID, this.currentPage, c);
        }
        if (commitFlag == true) {
            pm.commit(TAID);
        }
    }

    private void determinePageID(Integer lowerPage, Integer upperPage, Integer currentPage) {
        if (currentPage < 0) {
            this.currentPage = lowerPage;
        }
        if (currentPage < upperPage) {
            this.currentPage++;
        } else {
            this.currentPage = lowerPage;
        }
    }
}
