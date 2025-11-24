package Multithreading;

public class Counter {
    private int count;
    public synchronized void increment(){
        this.count++;
    }

    public Counter() {

    }

    public int getCount() {
        return this.count;
    }
}
