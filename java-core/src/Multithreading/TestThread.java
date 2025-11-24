package Multithreading;

public class TestThread extends Thread  {
    @Override
    public void run() {
        for(int i = 0 ; i < 10 ; i++){
            System.out.println("TestThread is running " + i);
        }
    }

}
