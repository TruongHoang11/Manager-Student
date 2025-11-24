package Multithreading;

public class Main2 {
    public static void main(String[] args) throws InterruptedException {
        TestRunnble testRunnble = new TestRunnble();
        Thread thread = new Thread(testRunnble);
        Thread thread1 = new Thread(testRunnble);
        thread.start();
        // tạm ngừng thread hiện tại trong 1 khoảng tg, cụ thể đây là 10 millisecond
        //        Thread.sleep(10);
        // chơ 1 thread ket thuc truoc khi tiếp tục thực hiện công việc khác
        thread.join();
        thread1.start();

    }
}
