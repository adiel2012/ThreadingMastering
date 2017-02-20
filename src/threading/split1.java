/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threading;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adiel
 */
public class split1 {

    public static Random rnd = new Random(0);
    public static void main(String[] args) {
        try {
            int cant = 100;
            int arr[] = new int[cant];
            int arrD[] = new int[cant];
            Random rnd = new Random();
            for (int i = 0; i < cant; i++) {
                arr[i] = rnd.nextInt(cant);
            }
            int factor = 2;
            CountDownLatch endController = new CountDownLatch(cant);
            int numThreads = factor * (Runtime.getRuntime().availableProcessors());
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);
            int size = 9;

            for (int i = 0; i < cant/size+(cant%size!=0?1:0); i++) {
                executor.execute(new IndividualDistanceTask(i*(size), Math.min((i+1)*(size)-1,cant-1), arr, arrD, endController));
            }

            endController.await();
            executor.shutdownNow();
            System.out.println("Pase await");
            for (int i : arrD) {
                System.out.println(i + " ");
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(split1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static class IndividualDistanceTask implements Runnable {

        int from, to, arr[], arrdouble[];
        CountDownLatch endController;

        public IndividualDistanceTask(int from, int to, int[] arr, int[] arrdouble, CountDownLatch endController) {
            System.out.println(from+" "+to);
            this.from = from;
            this.to = to;
            this.arr = arr;
            this.arrdouble = arrdouble;
            this.endController = endController;
        }

        @Override
        public void run() {
            for (int i = from; i <= to; i++) {
                arrdouble[i] = 2 * arr[i];
                endController.countDown();
//                try {
//                    Thread.sleep(rnd.nextInt(1000));
//                    //System.out.println("p");
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(split1.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
        }

    }
}
