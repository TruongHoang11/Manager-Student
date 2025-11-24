package Multithreading;

public class Main {
    public static void main(String[] args) {
        TestThread thread1 = new TestThread();
        TestThread thread2 = new TestThread();
        thread1.start();
        thread2.start();
    }
}
