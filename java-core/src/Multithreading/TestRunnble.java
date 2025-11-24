package Multithreading;

public class TestRunnble implements Runnable {
    @Override
    public void run() {
        for(int i = 0 ; i < 100; i++){
            System.out.println("TestRunnale is running " + i);
        }
    }
}
