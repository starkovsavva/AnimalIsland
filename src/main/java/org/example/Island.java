package org.example;

import org.example.animals.AnimalList.AnimalManager;
import org.example.animals.Cell;

import java.util.concurrent.*;

public class Island {

    public AnimalManager manager = new AnimalManager();
    public void IslandInitialize() throws InterruptedException {


        ExecutorService executor = Executors.newFixedThreadPool(1); // потоки


        for (int i = 0; i < 10; i++) {
            final int currentI = i;
            Future<?> future = executor.submit(() -> {
                manager.simulateTurn();
                System.out.println("Ход " + (currentI + 1) + ":");
                manager.drawField(manager.getGrid());
                System.out.println();

            });

            try {
                future.get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }

        };
        executor.awaitTermination(1, TimeUnit.HOURS); //ждем
        executor.shutdown();


    }



}


