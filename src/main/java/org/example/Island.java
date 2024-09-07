package org.example;

import org.example.animals.AnimalList.AnimalManager;
import org.example.animals.Cell;

import java.util.concurrent.*;

public class Island {

    public AnimalManager manager = new AnimalManager();
    public void IslandInitialize() throws InterruptedException {

        // Создаем ThreadPool
        ExecutorService executor = Executors.newFixedThreadPool(10); // потоки

        // Запускаем 10 ходов симуляции в разных потоках
        for (int i = 0; i < 10; i++) {
            final int currentI = i; // Сохраняем значение i в final переменную
            Future<?> future = executor.submit(() -> {
                manager.simulateTurn();
                System.out.println("Ход " + (currentI + 1) + ":"); // Используем currentI
                manager.drawField(manager.getGrid());
                System.out.println();

            });

            try {
                future.get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }

        };
        executor.awaitTermination(1, TimeUnit.HOURS); // Задаем время ожидания
        executor.shutdown();


    }

    // Ожидаем завершения всех задач


}


