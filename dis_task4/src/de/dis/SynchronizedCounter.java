package de.dis;

public class SynchronizedCounter {
    private int c = 0;

    public synchronized void incr(){
        c++;
    }

    public synchronized void decr(){
        c--;
    }

    public synchronized int val(){
        return c;
    }

    /*
        Set LSN after crash to latest LSN from log.
    */
    public synchronized void setCounter(int ctr) {
        this.c = ctr;
    }
}
