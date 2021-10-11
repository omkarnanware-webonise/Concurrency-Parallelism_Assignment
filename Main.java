package array;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    static int philosopher = 5;
    static philosopher philosophers[] = new philosopher[philosopher];
    static chopstick chopsticks[] = new chopstick[philosopher];

    static class chopstick {

        public Semaphore mutex = new Semaphore(1);

        void get() {
            try {
                mutex.acquire();
            }
            catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }

        void release() {
            mutex.release();
        }

        boolean checkIsFree() {
            return mutex.availablePermits() > 0;
        }

    }

    static class philosopher extends Thread {

        public int number;
        public chopstick leftchopstick;
        public chopstick rightchopstick;

        philosopher(int num, chopstick left, chopstick right) {
            number = num;
            leftchopstick = left;
            rightchopstick = right;
        }

        public void run(){

            while (true) {
                leftchopstick.get();
                System.out.println("philosopher " + (number+1) + " gets left chopstick.");
                rightchopstick.get();
                System.out.println("philosopher " + (number+1) + " gets right chopstick.");
                eat();
                leftchopstick.release();
                System.out.println("philosopher " + (number+1) + " releases left chopstick.");
                rightchopstick.release();
                System.out.println("philosopher " + (number+1) + " releases right chopstick.");
            }
        }

        void eat() {
            try {
                int sleepTime = ThreadLocalRandom.current().nextInt(0, 1000);
                System.out.println("philosopher " + number + " eats for " + sleepTime);
                Thread.sleep(sleepTime);
            }
            catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }

    }

    public static void main(String argv[]) {

        for (int i = 0; i < philosopher; i++) {
            chopsticks[i] = new chopstick();
        }

        for (int i = 0; i < philosopher; i++) {
            philosophers[i] = new philosopher(i, chopsticks[i], chopsticks[(i + 1) % philosopher]);
            philosophers[i].start();
        }

        while (true) {
            try {
                
                Thread.sleep(1000);

                boolean deadlock = true;
                for (chopstick f : chopsticks) {
                    if (f.checkIsFree()) {
                        deadlock = false;
                        break;
                    }
                }
                if (deadlock) {
                    Thread.sleep(1000);
                    System.out.println("Everyone Eats");
                    break;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Exit");
        System.exit(0);
    }

}