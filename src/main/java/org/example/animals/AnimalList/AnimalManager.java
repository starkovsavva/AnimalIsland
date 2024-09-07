package org.example.animals.AnimalList;

import lombok.Data;
import lombok.SneakyThrows;
import org.example.Constants;
import org.example.animals.Animal;
import org.example.JsonService.JsonService;
import org.example.animals.Cell;
import org.example.animals.Grass;
import org.example.animals.Herbivore;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Data
public class AnimalManager {

    public static List<Map.Entry<Animal, HashMap<Animal, Integer>>> animalTable;
    public static List<Animal> animals;
    private static List<Grass> grasses = new ArrayList<Grass>(Constants.grassCount);
    private Cell[][] grid = new Cell[Constants.gridX][Constants.gridY];

    static {
        try {
            animals = (new JsonService()).readAnimalsFromJson();
            animalTable = (new JsonService()).parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public AnimalManager(){
        init();
    }

    public void init(){
        spawnGrasses(grid);
        spawnAnimals(grid);
    }

    public void spawnAnimals(Cell[][] grid) {
        // Используем ThreadLocalRandom для генерации случайных чисел
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (Map.Entry<Animal, HashMap<Animal, Integer>> entry : animalTable) {
            Animal predator = entry.getKey();
            for (Map.Entry<Animal, Integer> preyEntry : entry.getValue().entrySet()) {
                Animal prey = preyEntry.getKey();
                int maxCount = preyEntry.getValue();
                int spawnCount = random.nextInt(maxCount + 1);

                for (int i = 0; i < spawnCount; i++) {
                    Cell randomCell = findRandomFreeCell(grid, random);
                    if (randomCell != null) {
                        // Проверяем, к какому типу относится животное
                        if (prey instanceof Herbivore) {
                            randomCell.addHerbivore((Herbivore) prey);
                        } else {
                            randomCell.addAnimal(prey);
                        }
                    }
                }
            }
        }
    }

    public void spawnGrasses(Cell[][] grid) {
        // Используем ThreadLocalRandom для генерации случайных чисел
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < Constants.MAX_GRASS_SPAWN; i++) {
            Cell randomCell = findRandomFreeCell(grid, random);
            if (randomCell != null) {
                randomCell.addGrass(new Grass());
            }
        }
    }
    private Cell findRandomFreeCell(Cell[][] grid, ThreadLocalRandom random) {
        int rows = grid.length;
        int cols = grid[0].length;
        List<Cell> freeCells = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col]==null) {
                    grid[row][col] = new Cell();
                    freeCells.add(grid[row][col]);
                }
            }
        }
        if (freeCells.isEmpty()) {
            return null;
        }
        return freeCells.get(random.nextInt(freeCells.size()));
    }


    public void drawField(Cell[][] grid) {
        for (int row = 0; row < Constants.gridY; row++) {
            for (int col = 0; col < Constants.gridX; col++) {
                if (!grid[row][col].getGrasses().isEmpty()) {
                    System.out.print("G "); // Трава
                } else if (!grid[row][col].getSyncList().isEmpty() || !grid[row][col].getSyncListAnimals().isEmpty()) {
                    System.out.print("A "); // Животное
                } else {
                    System.out.print(". "); // Пустая клетка
                }
            }
            System.out.println();
        }
    }

    public void simulateTurn() {
        // Используем synchronizedList для списка животных в каждой клетке
        for (int row = 0; row < Constants.gridX; row++) {
            for (int col = 0; col < Constants.gridY; col++) {
                // Обрабатываем Herbivore

                for (Herbivore herbivore : grid[row][col].getSyncList()) {
                    if (herbivore.isAlive()) {
                        if (herbivore.getFood() > 0) {
                            eatGrass(herbivore, grid[row][col]);
                        }
                        moveAnimal(herbivore, grid, row, col);
                        breedAnimal(herbivore, grid, row, col);
                    }
                }
                // Обрабатываем остальные животных (Predator)
                for (Animal animal : grid[row][col].getSyncListAnimals()) {
                    if (animal.isAlive()) {
                        if (animal.getFood() > 0) {
                            eatAnimal(animal, grid[row][col]);
                        }
                        moveAnimal(animal, grid, row, col);
                        breedAnimal(animal, grid, row, col);
                    }
                }
            }
        }
    }

    private void eatGrass(Herbivore herbivore, Cell cell) {
        synchronized (cell.getGrasses()) {
            if (!cell.getGrasses().isEmpty()) {
                cell.removeGrass();

                herbivore.food--;
            }
        }
    }


    private void eatAnimal(Animal animal, Cell cell) {
        // Используем ThreadLocalRandom для генерации случайного числа
        ThreadLocalRandom random = ThreadLocalRandom.current();
        synchronized (cell.getSyncListAnimals()) { // Используем syncListAnimals для хищников
            for (Animal otherAnimal : cell.getSyncListAnimals()) {
                if (otherAnimal != animal && isPredator(animal, otherAnimal) && animal.getFood() > 0) {
                    cell.removeAnimal(otherAnimal); // Используем общий метод для удаления
                    animal.food--;
                    break;
                }
            }
        }
    }
    private boolean isPredator(Animal predator, Animal prey) {
        for (Map.Entry<Animal, HashMap<Animal, Integer>> entry : animalTable) {
            if (entry.getKey() == predator) {
                return entry.getValue().containsKey(prey);
            }
        }
        return false;
    }

    private void moveAnimal(Animal animal, Cell[][] grid, int row, int col) {
        // Используем ThreadLocalRandom для генерации случайного числа
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int direction = random.nextInt(4);
        int newRow = row;
        int newCol = col;

        switch (direction) {
            case 0:
                newRow = Math.max(0, row - animal.getSpeed());
                break;
            case 1:
                newRow = Math.min(Constants.gridX - 1, row + animal.getSpeed());
                break;
            case 2:
                newCol = Math.max(0, col - animal.getSpeed());
                break;
            case 3:
                newCol = Math.min(Constants.gridY - 1, col + animal.getSpeed());
                break;
        }

        if (grid[newRow][newCol].isEmpty()) {
            // Используем соответствующий метод добавления в зависимости от типа животного
            if (animal instanceof Herbivore) {
                synchronized (grid[newRow][newCol].getSyncList()) {
                    grid[newRow][newCol].addHerbivore((Herbivore) animal);
                }
            } else {
                synchronized (grid[newRow][newCol].getSyncListAnimals()) {
                    grid[newRow][newCol].addAnimal(animal);
                }
            }
            // Используем общий метод для удаления животного
            removeAnimal(animal, grid, row, col);
        }
    }

    private void removeAnimal(Animal animal, Cell[][] grid, int row, int col) {
        if (animal instanceof Herbivore) {
            synchronized (grid[row][col].getSyncList()) {
                grid[row][col].removeHerbivore((Herbivore) animal);
            }
        } else {
            synchronized (grid[row][col].getSyncListAnimals()) {
                grid[row][col].removeAnimal(animal);
            }
        }
    }
    private void breedAnimal(Animal animal, Cell[][] grid, int row, int col) {
        // Используем ThreadLocalRandom для генерации случайного числа
        ThreadLocalRandom random = ThreadLocalRandom.current();
        if (random.nextDouble() < 0.5 && animal.getFood() < 0) {
            Cell randomCell = findRandomFreeCell(grid, random);
            if (randomCell != null) {
                // Создаем новое животное
                Animal newAnimal = new Animal(animal.getId(), animal.getWeight(), animal.getMaxCount(), animal.getSpeed(), animal.getFood());
                // Добавляем новое животное на поле, используя правильный метод
                if (newAnimal instanceof Herbivore) {
                    synchronized (randomCell.getSyncList()) {
                        randomCell.addHerbivore((Herbivore) newAnimal);
                    }
                } else {
                    synchronized (randomCell.getSyncListAnimals()) {
                        randomCell.addAnimal(newAnimal);
                    }
                }
            }
        }
    }


}
